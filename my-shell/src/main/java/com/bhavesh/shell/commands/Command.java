package com.bhavesh.shell.commands;

import java.io.InputStream;
import java.io.PrintStream;

public interface Command {
    
    void execute(String[] args,InputStream stdIn, PrintStream stdOut,PrintStream stdErr);
}
