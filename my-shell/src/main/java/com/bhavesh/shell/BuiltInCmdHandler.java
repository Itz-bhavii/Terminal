package com.bhavesh.shell;

import java.util.ArrayList;

public class BuiltInCmdHandler {

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
