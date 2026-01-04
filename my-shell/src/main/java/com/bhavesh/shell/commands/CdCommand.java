package com.bhavesh.shell.commands;

import java.io.InputStream;
import java.io.PrintStream;

import com.bhavesh.shell.DirectoryHandler;

public class CdCommand implements Command{

    @Override
    public void execute(String[] args, InputStream stdIn, PrintStream stdOut, PrintStream stdErr) {
        if(args.length > 0){
            DirectoryHandler.changeDirectory(args[0]);
        } else {
            DirectoryHandler.changeDirectory("~");
        }
    }
    
}
