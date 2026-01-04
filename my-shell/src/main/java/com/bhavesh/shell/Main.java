package com.bhavesh.shell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.bhavesh.shell.commands.CatCommand;
import com.bhavesh.shell.commands.CdCommand;
import com.bhavesh.shell.commands.EchoCommand;
import com.bhavesh.shell.commands.ExitCommand;
import com.bhavesh.shell.commands.PwdCommand;
import com.bhavesh.shell.commands.TypeCommand;




public class Main {
    
    
    public static void main(String[] args) throws IOException {
        
        Scanner sc = new Scanner(System.in);
        Terminal terminal = TerminalBuilder
                            .builder()
                            .system(true)
                            .build();

        JLineCustomCompleter completer = new JLineCustomCompleter();
        
        LineReader reader = LineReaderBuilder
                            .builder()
                            .terminal(terminal)
                            .completer(completer)
                            .build();
        

        while(true){
            // System.out.print("$ ");
            // String rawInput = sc.nextLine();
            String rawInput = reader.readLine("$ ");
            if(PipeHandler.isPipeOutsideQuotes(rawInput)){
                PipeHandler pipeHandler =  new PipeHandler(rawInput);
                pipeHandler.execute();
                
            } else {
                
                String tokens[] = new Parser().tokenize(rawInput);
                if(tokens.length == 0) continue;
                String command = tokens[0];
                String cmdArgs[] = Arrays.copyOfRange(tokens, 1, tokens.length);
                RedirectionHandler rh;
                try {
                    rh = new RedirectionHandler(cmdArgs);
                } catch (FileNotFoundException e) {
                    System.err.println("Error: Cannot create file - " + e.getMessage());
                    continue;
                } catch (IllegalArgumentException e) {
                    System.err.println("Error: " + e.getMessage());
                    continue;
                }
                if(command.equals(BuiltInCmdHandler.EXIT)){
                    sc.close();
                    ExitCommand exitCommand = new ExitCommand();
                    exitCommand.execute(cmdArgs, null, null, null);
    
                } else if(command.equals(BuiltInCmdHandler.ECHO)){
                    EchoCommand echoCommand = new EchoCommand();
                    echoCommand.execute(rh.getCleanedArgs(),System.in,rh.getStdOut(),rh.getStdErr());
                    
                } else if(command.equals(BuiltInCmdHandler.TYPE)){
                    TypeCommand typeCommand = new TypeCommand();
                    typeCommand.execute(rh.getCleanedArgs(),System.in,rh.getStdOut() , rh.getStdErr());
    
                } else if(command.equals(BuiltInCmdHandler.PWD)){
                    PwdCommand pwdCommand = new PwdCommand();
                    pwdCommand.execute(rh.getCleanedArgs(),System.in, rh.getStdOut(), rh.getStdErr());
    
                } else if(command.equals(BuiltInCmdHandler.CD)){
                    CdCommand cdCommand = new CdCommand();
                    cdCommand.execute(cmdArgs, null, null, null);
                    
                } else if(command.equals(BuiltInCmdHandler.CAT)){
                        CatCommand catCommand = new CatCommand();
                        catCommand.execute(rh.getCleanedArgs(),System.in,rh.getStdOut(),rh.getStdErr());
                    
                } else if(ExecutableHandler.checkIfItIsAnExecutable(command)){
                    ExecutableHandler.executeTheExecutable(command, cmdArgs);
                    
                } else {
                    System.out.println(command + ": not found");
                }
                rh.close();
            }
        }
    }


}