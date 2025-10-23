package com.bhavesh.shell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExecutablesHandler {
    public static boolean executeTheExecutable(String command,String rest[]){
        try{
            List<String> commands = new ArrayList();
            commands.add(command);
            for(String data : rest){
                commands.add(data);
            }
            ProcessBuilder processBuilder = new ProcessBuilder(commands);
            processBuilder.inheritIO();
            Process process = processBuilder.start();
            // String ans = new String(process.getInputStream().readAllBytes());
            // System.out.println(ans);
            int exitCode = process.waitFor();
            if(exitCode != 0){
                return false;
            }
        } catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public static boolean checkIfItIsAnExecutable(String command,String rest[]){

        String separator = File.pathSeparator;
        String pathCommandsString = System.getenv("PATH");
        String pathCommands[] = pathCommandsString.split(separator);

        for(String pathCommand : pathCommands){
            File file = new File(pathCommand,command);
            if(file.exists() && file.canExecute()){
                return true;
            }
        }

        return false;
    }
}
