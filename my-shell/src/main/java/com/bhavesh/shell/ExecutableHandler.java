package com.bhavesh.shell;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ExecutableHandler {

    final static String EXE = ".exe";
    public static boolean executeTheExecutable(String command,String cmdArgs[]){
        try{
            if(command.endsWith(EXE)) command = command.substring(0,command.length() - 4);
            List<String> commands = new ArrayList<>();
            commands.add(command);
            for(String data : cmdArgs){
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

    public static boolean checkIfItIsAnExecutable(String command){
        if(!command.endsWith(EXE)) command = command + EXE;
    
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

    public static ArrayList<String> checkAndReturnExecutables(String command){
        ArrayList<String> matchingExe = new ArrayList<>();
        String separator = File.pathSeparator;
        String pathCommandsString = System.getenv("PATH");
        String pathCommands[] = pathCommandsString.split(separator);

        for(String pathCommand : pathCommands){
            File file = new File(pathCommand);
            File[] listOfFiles = file.listFiles();
            if(listOfFiles == null) continue;
            for(File f : listOfFiles){
                if(f.getName().startsWith(command) && f.canExecute() && f.getName().endsWith(".exe")){
                    String filename = f.getName();
                    matchingExe.add(filename.substring(0,filename.length()-4)); // remove exe from file name
                }
            }
        }

        return matchingExe;
    }
}
