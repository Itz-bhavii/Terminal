package com.bhavesh.shell.commands;

import java.io.File;
import java.io.PrintStream;

import com.bhavesh.shell.BuiltInCmdHandler;

public class TypeCommand implements Command{

    @Override
    public void execute(String[] args, PrintStream stdOut, PrintStream stdErr) {
        if(args.length == 0) {
            stdErr.println("type: expected argument");
            return;
        }
        String separator = File.pathSeparator;
        String pathCommandsString = System.getenv("PATH");
        String pathCommands[] = pathCommandsString.split(separator);
        String exe = ".exe";
        if(BuiltInCmdHandler.isBuiltInCommand(args[0])){
            stdOut.println(args[0] + " is a shell built-in");
            return;
        }

        for(int i = 0;i < pathCommands.length;i++){
            File file = new File(pathCommands[i],args[0]+exe);
            if(file.exists() && file.isFile()){
                stdOut.println(args[0] + " is " + file.getAbsolutePath());
                return;
            }
        }
        stdErr.println(args[0] + ": not found");
    }
    
}
