package com.bhavesh.shell;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import com.bhavesh.shell.commands.Command;




public class Main {
    
    
    public static void main(String[] args) throws IOException {
        
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
            String rawInput = reader.readLine("$ ");
            PipeHandler pipeHandler =  new PipeHandler(rawInput);
            if(pipeHandler.hasPipe()){
                if(pipeHandler.areCommandsValid()){
                    if(pipeHandler.isBuiltIn()){
                        pipeHandler.executeBuiltIn();
                    } else if(pipeHandler.isExecutable()){
                        pipeHandler.executeExecutables();
                    } else {
                        pipeHandler.executeMixedCommands();
                    }
                } else {
                    continue;
                }

                
            } else {
                
                String tokens[] = new Parser().tokenize(rawInput);
                if(tokens.length == 0) continue;
                String commandName = tokens[0];
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
                Command command = CommandFactory.getCommand(commandName);
                if(command != null){
                    command.execute(cmdArgs, System.in, rh.getStdOut(), rh.getStdErr());
                } else {
                    if(ExecutableHandler.checkIfItIsAnExecutable(commandName)){
                        ExecutableHandler.executeTheExecutable(commandName, cmdArgs);
                    } else {
                        System.out.println(commandName + ": not found");
                    }
                }
                rh.close();
            }
        }
    }


}