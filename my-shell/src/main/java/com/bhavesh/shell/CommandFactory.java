package com.bhavesh.shell;

import com.bhavesh.shell.commands.CatCommand;
import com.bhavesh.shell.commands.CdCommand;
import com.bhavesh.shell.commands.Command;
import com.bhavesh.shell.commands.EchoCommand;
import com.bhavesh.shell.commands.ExitCommand;
import com.bhavesh.shell.commands.PwdCommand;
import com.bhavesh.shell.commands.TypeCommand;

public class CommandFactory {
    public static Command getCommand(String name){
        return switch(name){
            case BuiltInCmdHandler.CAT -> new CatCommand();
            case BuiltInCmdHandler.PWD -> new PwdCommand();
            case BuiltInCmdHandler.ECHO -> new EchoCommand();
            case BuiltInCmdHandler.TYPE -> new TypeCommand();
            case BuiltInCmdHandler.CD -> new CdCommand();
            case BuiltInCmdHandler.EXIT -> new ExitCommand();
            default -> null;
        };
    }
}
