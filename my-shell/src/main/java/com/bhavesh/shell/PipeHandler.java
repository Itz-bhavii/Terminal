package com.bhavesh.shell;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.bhavesh.shell.commands.CatCommand;
import com.bhavesh.shell.commands.CdCommand;
import com.bhavesh.shell.commands.Command;
import com.bhavesh.shell.commands.EchoCommand;
import com.bhavesh.shell.commands.ExitCommand;
import com.bhavesh.shell.commands.PwdCommand;
import com.bhavesh.shell.commands.TypeCommand;

public class PipeHandler {

    List<String> pipeSplittedCommands;

    public PipeHandler(String rawInput){
        this.pipeSplittedCommands = splitByPipes(rawInput);
    }

    public void execute(){
        InputStream currentInp = System.in;

        for(int i = 0 ;i < pipeSplittedCommands.size() ; i++){
            String tokens[] = new Parser().tokenize(pipeSplittedCommands.get(i));
            String commandName = tokens[0];
            String cmdArgs[] = Arrays.copyOfRange(tokens, 1, tokens.length);
            boolean isLast = (i == pipeSplittedCommands.size() - 1);
            Command command = getCommand(commandName);
            if(Objects.equals(command,null)) {
                System.out.println(commandName + ": not found");
                return;
            }
            if(isLast){
                command.execute(cmdArgs, currentInp, System.out, System.err);
            } else {
                ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                PrintStream captureOutput = new PrintStream(buffer);

                command.execute(cmdArgs, currentInp, captureOutput, System.err);
                captureOutput.flush();

                currentInp = new ByteArrayInputStream(buffer.toByteArray());
            }

        }
    }

    public List<String> splitByPipes(String rawInput){
        List<String> commands = new ArrayList<>();
        boolean isInsideDoubleQuotes = false;
        boolean isInsideSingleQuotes = false;
        StringBuilder sb = new StringBuilder();
        for(char ch : rawInput.toCharArray()){
            if (ch == '\'' && !isInsideDoubleQuotes){
                isInsideSingleQuotes = !isInsideSingleQuotes;
                sb.append(ch);
            } else if (ch == '\"' && !isInsideSingleQuotes ) {
                isInsideDoubleQuotes = !isInsideDoubleQuotes;
                sb.append(ch);
            } else if(ch == ' ' && (isInsideDoubleQuotes || isInsideSingleQuotes)){
                //append space 
                sb.append(ch);
            } else if (ch == '|' && (!isInsideDoubleQuotes && !isInsideSingleQuotes)){
                if(sb.toString().trim().length() > 0){
                    commands.add(sb.toString());
                }
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        if(sb.length() > 0){
            commands.add(sb.toString());
        }
        return commands;
    }

    public static boolean isPipeOutsideQuotes(String rawInput){
        boolean isInsideDoubleQuotes = false;
        boolean isInsideSingleQuotes = false;
        boolean isPipeOutside = true;
        for(char ch : rawInput.toCharArray()){
            if (ch == '\'' && !isInsideDoubleQuotes){
                isInsideSingleQuotes = !isInsideSingleQuotes;
            } else if (ch == '\"' && !isInsideSingleQuotes ) {
                isInsideDoubleQuotes = !isInsideDoubleQuotes;
            } else if (ch == '|' ){
                if(!isInsideSingleQuotes && !isInsideDoubleQuotes){
                    isPipeOutside = true;
                    return isPipeOutside;
                } else {
                    isPipeOutside =  false;
                }
            }
        }
        return isPipeOutside;
    }

    private Command getCommand(String commandName) {
        if (commandName.equals(BuiltInCmdHandler.CAT)){
            return new CatCommand();
        } else if (commandName.equals(BuiltInCmdHandler.PWD)){
            return new PwdCommand();
        } else if (commandName.equals(BuiltInCmdHandler.TYPE)){
            return new TypeCommand();
        } else if (commandName.equals(BuiltInCmdHandler.ECHO)){
            return new EchoCommand();
        } else if (commandName.equals(BuiltInCmdHandler.EXIT)){
            return new ExitCommand();
        } else if (commandName.equals(BuiltInCmdHandler.CD)){
            return new CdCommand();
        }
        return null;
        
    }
}
