package com.bhavesh.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    

    public static void main(String[] args) {
        final String EXE = ".exe";
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("$ ");
            String input = sc.nextLine();
            String inputStringArray[] = input.split(" ");
            String command = inputStringArray[0];
            String rest[] = Arrays.copyOfRange(inputStringArray, 1, inputStringArray.length);
            String result = String.join(" ",rest);

            if(Objects.equals(command,"exit")){
                System.exit(0);
            }
            else if(Objects.equals(command,"echo")){
                System.out.println(result);
            } 
            else if(Objects.equals(command,"type")){
                //adding exe because windows is not directly finding the normal names
                System.out.println(checkForType(result+EXE));
            } 
            else if(Objects.equals(command,"pwd")){
                System.out.println(getCurrentWorkingDirectory()); //works the smae
            }
            else if(Objects.equals(command,"cd")){
                changeDirectory(command,result);
            }
            else if(checkIfItIsAnExecutable(command + EXE, rest)){
                executeTheExecutable(command, rest);
            }
            else {
                System.out.println(command + ": not found");
            }
        }
    }

    private static void changeDirectory(String command,String receivedPath) {
        if(Objects.equals(receivedPath,"~")){
            String homeDir = System.getProperty("user.home");
            if(!setCurrentWorkingDirectory(homeDir)){
                System.out.println(command +":"+ homeDir + ": " + "No such file or directory");
            }
            return;
        }
        else if(receivedPath.startsWith("~/")){
            receivedPath = System.getProperty("user.home") + receivedPath.substring(1);
        }
        
        File file = new File(receivedPath);
        try{
            String path = file.getCanonicalPath();
            File canonicalFile = new File(path);
            if(canonicalFile.exists() && canonicalFile.isDirectory()){
                if(!setCurrentWorkingDirectory(path)){
                    System.out.println(command +":"+ path + ": " + "Could not change directory");
                }
            }
            else {
                System.out.println(command +":"+ receivedPath + ": " + "No such file or directory");
            }
            
        } catch (IOException e){
            System.out.println(command +":"+ receivedPath + ": " + "No such file or directory");
            
        }                
    }

    static boolean setCurrentWorkingDirectory(String path){
        File file = new File(path);
        // System.out.println("scwd");
        if(file.isDirectory()){
            System.setProperty("user.dir", path);
            return true;
        }
        else {
            return false;
        }
    }

    static String getCurrentWorkingDirectory(){
        // return Paths.get("").toAbsolutePath().toString(); // also works
        return System.getProperty("user.dir");
    }

    static boolean executeTheExecutable(String command,String rest[]){
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

    static boolean checkIfItIsAnExecutable(String command,String rest[]){

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

    static String checkForType(String command){
        String separator = File.pathSeparator;
        String pathCommandsString = System.getenv("PATH");
        String pathCommands[] = pathCommandsString.split(separator);
        
        if(isBuiltInCommand(command)){
            return command + " is a shell built-in";
        }

        for(int i = 0;i < pathCommands.length;i++){
            File file = new File(pathCommands[i],command);
            if(file.exists() && file.isFile()){
                return command + " is " + file.getAbsolutePath(); 
            }
        }
        return command + ": not found";
    }
    
    static boolean isBuiltInCommand(String command){
        String builtInCommands[] = {"exit","echo","type"};
        for(String builtInCommand : builtInCommands){
            if(builtInCommand.equals(command)){
                return true;
            }
        }
        return false;
    }
}