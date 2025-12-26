package com.bhavesh.shell.commands;

import java.io.PrintStream;

import com.bhavesh.shell.DirectoryHandler;

public class PwdCommand implements Command{

    @Override
    public void execute(String[] args, PrintStream stdOut, PrintStream stdErr) {
        if(args.length > 0){
            stdErr.println("pwd: no args expected");
            return;
        }
        String currentDirectory = DirectoryHandler.getCurrentWorkingDirectory();
        stdOut.println(currentDirectory);
    }
    
}
