package com.bhavesh.shell;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.bhavesh.shell.commands.Command;

public class PipeHandler {

    List<String> pipeSplittedCommands;

    public PipeHandler(String rawInput){
        this.pipeSplittedCommands = splitByPipes(rawInput);
    }

    public void execute(){
        InputStream currentInp = System.in;
        InputStream prevInp = null;
        
        try{

            for(int i = 0 ;i < pipeSplittedCommands.size() ; i++){
                String tokens[] = new Parser().tokenize(pipeSplittedCommands.get(i));
                String commandName = tokens[0];
                String cmdArgs[] = Arrays.copyOfRange(tokens, 1, tokens.length);
                boolean isLast = (i == pipeSplittedCommands.size() - 1);
                Command command = getCommand(commandName);
                if(command == null) {
                    System.err.println(commandName + ": not found");
                    if(prevInp != null && prevInp != System.in) prevInp.close();
                    return;
                }
                if(isLast){
                    command.execute(cmdArgs, currentInp, System.out, System.err);
                } else {
                    if(prevInp != null && prevInp != System.in){
                        prevInp.close();
                    }
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    PrintStream captureOutput = new PrintStream(buffer);
    
                    command.execute(cmdArgs, currentInp, captureOutput, System.err);
                    captureOutput.flush();
    
                    prevInp = currentInp;
                    currentInp = new ByteArrayInputStream(buffer.toByteArray());
                }
    
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally{
            if(currentInp != null && currentInp != System.in){
                 try { currentInp.close(); } catch(Exception ignored){}
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
                sb.append(ch);
            } else if (ch == '|' && (!isInsideDoubleQuotes && !isInsideSingleQuotes)){
                String segment = sb.toString().trim();
                if(segment.length() > 0){
                    commands.add(segment);
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

    public boolean hasPipe(){
        return pipeSplittedCommands.size() > 1;
    }

    private Command getCommand(String commandName) {
        return CommandFactory.getCommand(commandName);
        
    }
}
