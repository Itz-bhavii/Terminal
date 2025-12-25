package com.bhavesh.shell.commands;

public class EchoCommand implements Command {

    @Override
    public void execute(String[] args) {
        
        System.out.println(String.join(" ",args));
    }
    
}
