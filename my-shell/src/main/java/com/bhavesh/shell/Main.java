package main.java.com.bhavesh.shell;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while(true){
            System.out.print("$ ");
            String input = sc.nextLine();
            if(input.contains("exit")){
                // System.out.print(" 0");
                System.exit(0);
            }
            System.out.println(input + ": command not found");
        }
    }
}
