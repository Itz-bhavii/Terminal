package com.bhavesh.shell;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class DirectoryHandler {

    static String currentDirectory = System.getProperty("user.dir");

    public static void changeDirectory(String receivedPath) {
        if(Objects.equals(receivedPath,"~")){
            String homeDir = System.getProperty("user.home");
            if(!setCurrentWorkingDirectory(homeDir)){
                System.out.println("echo" +":"+ homeDir + ": " + "No such file or directory");
            }
            return;
        }
        else if(receivedPath.startsWith("~/")){
            receivedPath = System.getProperty("user.home") + receivedPath.substring(1);
        }
        
        File file = new File(currentDirectory,receivedPath);
        try{
            String path = file.getCanonicalPath();
            File canonicalFile = new File(path);
            if(canonicalFile.exists() && canonicalFile.isDirectory()){
                if(!setCurrentWorkingDirectory(path)){
                    System.out.println("echo" +":"+ path + ": " + "Could not change directory");
                }
            }
            else {
                System.out.println("echo" +":"+ receivedPath + ": " + "No such file or directory");
            }
            
        } catch (IOException e){
            System.out.println("echo" +":"+ receivedPath + ": " + "No such file or directory");
            
        }                
    }

    public static boolean setCurrentWorkingDirectory(String path){
        File file = new File(path);
        // System.out.println("scwd");
        if(file.isDirectory()){
            currentDirectory = path;
            System.setProperty("user.dir", path);
            return true;
        }
        else {
            return false;
        }
    }

    public static String getCurrentWorkingDirectory(){
        // return Paths.get("").toAbsolutePath().toString(); // also works
        // return System.getProperty("user.dir");
        return currentDirectory;
    }
}
