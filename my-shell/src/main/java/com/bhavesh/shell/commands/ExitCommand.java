package com.bhavesh.shell.commands;

import java.io.InputStream;
import java.io.PrintStream;

public class ExitCommand implements Command{

    @Override
    public void execute(String[] args, InputStream stdIn, PrintStream stdOut, PrintStream stdErr) {
        System.exit(0);
    }
    
}
