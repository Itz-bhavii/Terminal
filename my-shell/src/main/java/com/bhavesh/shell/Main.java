package com.bhavesh.shell;

import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

import com.bhavesh.shell.commands.CatCommand;
import com.bhavesh.shell.commands.EchoCommand;
import com.bhavesh.shell.commands.PwdCommand;
import com.bhavesh.shell.commands.TypeCommand;


public class Main {
    private final static String CD = "cd";
    private final static String PWD = "pwd";
    private final static String EXIT = "exit";
    private final static String ECHO = "echo";
    private final static String TYPE = "type";
    private final static String CAT = "cat";

    public static void main(String[] args) {
        final String EXE = ".exe";
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("$ ");
            String rawInput = sc.nextLine();
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
            if(command.equals(EXIT)){
                sc.close();
                System.exit(0);

            } else if(command.equals(ECHO)){
                EchoCommand echoCommand = new EchoCommand();
                echoCommand.execute(rh.getCleanedArgs(),rh.getStdOut(),rh.getStdErr());
                
            } else if(command.equals(TYPE)){
                TypeCommand typeCommand = new TypeCommand();
                typeCommand.execute(rh.getCleanedArgs(),rh.getStdOut() , rh.getStdErr());

            } else if(command.equals(PWD)){
                PwdCommand pwdCommand = new PwdCommand();
                pwdCommand.execute(rh.getCleanedArgs(), rh.getStdOut(), rh.getStdErr());

            } else if(command.equals(CD)){
                if(cmdArgs.length > 0){
                    DirectoryHandler.changeDirectory(command,cmdArgs[0]);
                 } else {
                    DirectoryHandler.changeDirectory(command,"~");
                }
                
            } else if(command.equals(CAT)){
                    CatCommand catCommand = new CatCommand();
                    catCommand.execute(rh.getCleanedArgs(),rh.getStdOut(),rh.getStdErr());
                
            } else if(ExecutableHandler.checkIfItIsAnExecutable(command + EXE)){
                ExecutableHandler.executeTheExecutable(command, cmdArgs);
                
            } else {
                System.out.println(command + ": not found");
            }
            rh.close();
        }
    }


}