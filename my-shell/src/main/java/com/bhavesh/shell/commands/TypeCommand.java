package com.bhavesh.shell.commands;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import com.bhavesh.shell.BuiltInCmdHandler;

public class TypeCommand implements Command{

    @Override
    public void execute(String[] args,InputStream stdIn, PrintStream stdOut, PrintStream stdErr) {
        String command;
        if(args.length == 0) {
            try {
                if(stdIn.available() > 0){
                    BufferedReader br = new BufferedReader(new InputStreamReader(stdIn));
                    StringBuilder s = new StringBuilder();
                    char ch;
                    while((ch = (char)br.read()) != ' '){
                        s.append(ch);
                    }
                    command = s.toString();
                } else {
                    stdErr.println("type: expected argument");
                    return;
                }
            } catch (Exception e) {
                return;
            }
        } else {
            command = args[0];
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
