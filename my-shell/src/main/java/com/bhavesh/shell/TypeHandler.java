package com.bhavesh.shell;

import java.io.File;

public class TypeHandler {
    public static String checkForType(String command){
        String separator = File.pathSeparator;
        String pathCommandsString = System.getenv("PATH");
        String pathCommands[] = pathCommandsString.split(separator);
        String exe = ".exe";
        if(BuiltInCmdHandler.isBuiltInCommand(command)){
            return command + " is a shell built-in";
        }

        for(int i = 0;i < pathCommands.length;i++){
            File file = new File(pathCommands[i],command+exe);
            if(file.exists() && file.isFile()){
                return command + " is " + file.getAbsolutePath(); 
            }
        }
        return command + ": not found";
    }
}
