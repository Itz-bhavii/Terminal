package com.bhavesh.shell.commands;

import java.io.PrintStream;

public interface Command {
    
    void execute(String[] args, PrintStream stdOut,PrintStream stdErr);
}
