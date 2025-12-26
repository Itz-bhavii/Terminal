package com.bhavesh.shell.commands;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CatCommand implements Command {

    @Override
    public void execute(String[] args, PrintStream stdOut, PrintStream stdErr) {
        if (args.length == 0) {
            stdErr.println("cat: missing file operand");
            return;
        }

        for (String filename : args) {
            Path filePath = Paths.get(filename);
            try {
                String content = Files.readString(filePath);
                stdOut.print(content);
            } catch (IOException e) {
                stdErr.println("cat: " + filename + ": No such file or directory");
            }
        }
    }
}