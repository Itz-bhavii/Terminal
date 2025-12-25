package com.bhavesh.shell;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;


public class Main {
    private final static String CD = "cd";
    private final static String PWD = "pwd";
    private final static String EXIT = "exit";
    private final static String ECHO = "echo";
    private final static String TYPE = "type";



    public static void main(String[] args) {
        final String EXE = ".exe";
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("$ ");
            String rawInput = sc.nextLine();
            String tokens[] = tokenize(rawInput);
            if(tokens.length == 0) continue;
            String command = tokens[0];
            String cmdArgs[] = Arrays.copyOfRange(tokens, 1, tokens.length);

            if(command.equals(EXIT)){
                sc.close();
                System.exit(0);
            }
            else if(command.equals(ECHO)){
                System.out.println(String.join(" ", cmdArgs));
            } 
            else if(command.equals(TYPE)){
                //adding exe because windows is not directly finding the normal names
                if(cmdArgs.length > 0){
                    System.out.println(TypeHandler.checkForType(cmdArgs[0]));
                } else {
                    System.out.println("type: expected argument");
                }
            } 
            else if(command.equals(PWD)){
                String currentDirectory = DirectoryHandler.getCurrentWorkingDirectory();
                System.out.println(currentDirectory); //works the smae
            }
            else if(command.equals(CD)){
                if(cmdArgs.length > 0){
                    DirectoryHandler.changeDirectory(command,cmdArgs[0]);
                 } else {
                    DirectoryHandler.changeDirectory(command,"~");
                }
            }
            else if(ExecutableHandler.checkIfItIsAnExecutable(command + EXE)){
                ExecutableHandler.executeTheExecutable(command, cmdArgs);
            }
            else {
                System.out.println(command + ": not found");
            }
        }
    }

    public static String[] tokenize(String rawInput){
        boolean isInsideSingleQuotes = false;
        boolean isInsideDoubleQuotes = false;
        ArrayList<String> tokenList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(char c : rawInput.toCharArray()){
            if(c == '\'' && !isInsideDoubleQuotes){
                isInsideSingleQuotes = !isInsideSingleQuotes;
            } else if(c == '\"' && !isInsideSingleQuotes){
                isInsideDoubleQuotes = !isInsideDoubleQuotes;
            } else if(c == ' ' && (isInsideDoubleQuotes || isInsideSingleQuotes)){
                //append space 
                sb.append(c);
            } else if(c == ' ' && (!isInsideDoubleQuotes && !isInsideSingleQuotes)){
                //complete the token and add to the tokenlist
                if(sb.length() > 0){
                    tokenList.add(sb.toString());
                    sb.setLength(0);
                }
            } else {
                sb.append(c);
            }
        }
        if(sb.length() > 0){
            tokenList.add(sb.toString());
        }
        return tokenList.toArray(new String[0]);

    }
}