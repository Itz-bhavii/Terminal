package com.bhavesh.shell.commands;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

import com.bhavesh.shell.BuiltInCmdHandler;

public class TypeCommand implements Command{

    @Override
    public void execute(String[] args,InputStream stdIn, PrintStream stdOut, PrintStream stdErr) {
        StringBuilder sb = new StringBuilder();
        if(args.length == 0){
            int ch;
            try {
                while ((ch = stdIn.read()) != -1) {
                    sb.append((char)ch);   
                }
            } catch (IOException e) {
                stdErr.println("Error occured : "+e.getMessage());
            }   
        }
        String command;
        if(sb.length() == 0){
            stdErr.println("type: argument expected");
            return;
        }
        command = args[0];
        if(sb.length() > 0){
            command = sb.toString();
        }
        String separator = File.pathSeparator;
        String pathCommandsString = System.getenv("PATH");
        String pathCommands[] = pathCommandsString.split(separator);
        String exe = ".exe";
        if(BuiltInCmdHandler.isBuiltInCommand(command)){
            stdOut.println(command + " is a shell built-in");
            return;
        }

        for(int i = 0;i < pathCommands.length;i++){
            File file = new File(pathCommands[i],command+exe);
            if(file.exists() && file.isFile()){
                stdOut.println(command + " is " + file.getAbsolutePath());
                return;
            }
        }
        stdErr.println(command + ": not found");
    }
    
}
