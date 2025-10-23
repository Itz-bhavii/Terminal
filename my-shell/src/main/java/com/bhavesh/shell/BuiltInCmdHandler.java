package com.bhavesh.shell;

public class BuiltInCmdHandler {
    public static boolean isBuiltInCommand(String command){
        String builtInCommands[] = {"exit","echo","type"};
        for(String builtInCommand : builtInCommands){
            if(builtInCommand.equals(command)){
                return true;
            }
        }
        return false;
    }
}
