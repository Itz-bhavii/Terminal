package com.bhavesh.shell.commands;

import java.io.PrintStream;

public class EchoCommand implements Command {

    @Override
    public void execute(String[] args,PrintStream stdOut,PrintStream stdErr) {
        stdOut.println(String.join(" ",args));
    }
    
}
