package com.bhavesh.shell;

import java.util.Arrays;
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
                System.out.println(TypeHandler.checkForType(result+EXE));
            } 
            else if(Objects.equals(command,"pwd")){
                String currentDirectory = DirectoryHandler.getCurrentWorkingDirectory();
                System.out.println(currentDirectory); //works the smae
            }
            else if(Objects.equals(command,"cd")){
                DirectoryHandler.changeDirectory(command,result);
            }
            else if(ExecutableHandler.checkIfItIsAnExecutable(command + EXE, rest)){
                ExecutableHandler.executeTheExecutable(command, rest);
            }
            else {
                System.out.println(command + ": not found");
            }
        }
    }
}