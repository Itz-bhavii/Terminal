package com.bhavesh.shell;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class RedirectionHandler {
    private String cleanedArgs[];
    private PrintStream stdOut;
    private PrintStream stdErr;
    private enum Operation{
        Truncate,
        Append,
        Error,
        AppendError,
        Both
    }

    public RedirectionHandler(String tokens[]) throws FileNotFoundException,IllegalArgumentException{
        stdOut = System.out;
        stdErr = System.err;

        List<String> cleanArgs = new ArrayList<>();
        int n = tokens.length;
        boolean hasStdOut = false;
        boolean hasStdErr = false;

        for(int i = 0;i<n;i++){
            if(tokens[i].equals(">")){
                if(hasStdOut){
                    throw new IllegalArgumentException("Duplicate Redirectors");
                }
                if(++i >= n) throw new IllegalArgumentException("Missing filename after >");
                setPrintStreams(tokens[i], Operation.Truncate);
                hasStdOut = true;
                
            } else if(tokens[i].equals(">>")){
                if(hasStdOut){
                    throw new IllegalArgumentException("Duplicate Redirectors");
                }
                if(++i >= n) throw new IllegalArgumentException("Missing filename after >>");
                setPrintStreams(tokens[i], Operation.Append);
                hasStdOut = true;
                
            } else if(tokens[i].equals("2>")){
                if(hasStdErr){
                    throw new IllegalArgumentException("Duplicate Redirectors");
                }
                if(++i >= n) throw new IllegalArgumentException("Missing filename after 2>");
                setPrintStreams(tokens[i], Operation.Error);
                hasStdErr = true;

            } else if(tokens[i].equals("2>>")){
                if(hasStdErr){
                    throw new IllegalArgumentException("Duplicate Redirectors");
                }
                if(++i >= n) throw new IllegalArgumentException("Missing filename after 2>>");
                setPrintStreams(tokens[i], Operation.AppendError);
                hasStdErr = true;

            } else if(tokens[i].equals("&>")){
                if(hasStdErr || hasStdOut){
                    throw new IllegalArgumentException("Duplicate Redirectors");
                }
                if(++i >= n) throw new IllegalArgumentException("Missing filename after &>");
                setPrintStreams(tokens[i], Operation.Both);
                hasStdErr = true;
                hasStdOut = true;

            } else {
                cleanArgs.add(tokens[i]);
            }
        }

        cleanedArgs = cleanArgs.toArray(new String[0]);
    }

    private void setPrintStreams(String fileName,Operation type) throws FileNotFoundException{
        if(type == Operation.Truncate){
            stdOut = new PrintStream(new FileOutputStream(fileName));
        } else if(type == Operation.Append){
            stdOut = new PrintStream(new FileOutputStream(fileName,true));
        } else if(type == Operation.Error){
            stdErr = new PrintStream(new FileOutputStream(fileName));
        } else if(type == Operation.AppendError){
            stdErr = new PrintStream(new FileOutputStream(fileName,true));
        } else if(type == Operation.Both){
            PrintStream both = new PrintStream(new FileOutputStream(fileName));
            stdOut = both;
            stdErr = both;
        }
    }
    
    public String[] getCleanedArgs(){
        return cleanedArgs;
    }

    public PrintStream getStdOut(){
        return stdOut;
    }
    
    public PrintStream getStdErr(){
        return stdErr;
    }

    public void close(){
        if(stdOut != System.out && stdOut != null) {
             stdOut.close();
         }
         if(stdErr != System.err && stdErr != null) {
             stdErr.close();
         }
    }
}
