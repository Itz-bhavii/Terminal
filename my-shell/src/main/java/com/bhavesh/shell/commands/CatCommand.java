package com.bhavesh.shell.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CatCommand implements Command {

    @Override
    public void execute(String[] args,InputStream stdIn, PrintStream stdOut, PrintStream stdErr) {
        if (args.length == 0) {
            try{
                BufferedReader br = new BufferedReader(new InputStreamReader(stdIn));
                String line;
                while((line = br.readLine()) != null){
                    stdOut.println(line);
                }
            } catch (Exception e) {
                stdErr.println("cat: error reading input - " + e.getMessage());
            }
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