package main.java.com.bhavesh.shell;

import java.io.File;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    

    public static void main(String[] args) {
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
                System.out.println(checkForType(result));
            } 
            else {
                System.out.println(result + ": not found");
            }
        }
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
