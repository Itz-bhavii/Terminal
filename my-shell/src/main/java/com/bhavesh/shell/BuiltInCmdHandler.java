package com.bhavesh.shell;

import java.util.ArrayList;

public class BuiltInCmdHandler {

    public final static String CD = "cd";
    public final static String PWD = "pwd";
    public final static String EXIT = "exit";
    public final static String ECHO = "echo";
    public final static String TYPE = "type";
    public final static String CAT = "cat";

    static String builtInCommands[] = {"exit","echo","type","pwd","cat","cd"};

    public static boolean isBuiltInCommand(String command){
        for(String builtInCommand : builtInCommands){
            if(builtInCommand.equals(command)){
                return true;
            }
        }
        return false;
    }
    
    public static ArrayList<String> checkAndReturnBuiltInCommands(String command){
        ArrayList<String> matchingCommands = new ArrayList<>();
        for(String builtInCommand : builtInCommands){
            if(builtInCommand.startsWith(command)){
                matchingCommands.add(builtInCommand);
            }
        }
        return matchingCommands;
    }
}
