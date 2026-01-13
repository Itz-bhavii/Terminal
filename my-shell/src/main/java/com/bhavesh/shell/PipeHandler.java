package com.bhavesh.shell;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.bhavesh.shell.commands.Command;

public class PipeHandler {

    List<String> pipeSplittedCommands;
    List<String> commandsList;
    List<String[]> tokensArray;

    enum CommandTypes{
        BuiltIn,
        Executable
    }
    List<CommandTypes> currentCommandTracker;

    public PipeHandler(String rawInput){
        this.pipeSplittedCommands = splitByPipes(rawInput);
        this.commandsList = getCommandsOnlyAndPopulateTokensArray();
        initializeCurrentCommandsAndNextCommands();
    }

    public void executeBuiltIn(){
        InputStream currentInp = System.in;
        InputStream prevInp = null;
        
        try{

            for(int i = 0 ;i < pipeSplittedCommands.size() ; i++){
                String tokens[] = new Parser().tokenize(pipeSplittedCommands.get(i));
                String commandName = tokens[0];
                String cmdArgs[] = Arrays.copyOfRange(tokens, 1, tokens.length);
                boolean isLast = (i == pipeSplittedCommands.size() - 1);
                Command command = getCommand(commandName);
                if(command == null) {
                    System.err.println(commandName + ": not found");
                    if(prevInp != null && prevInp != System.in) prevInp.close();
                    return;
                }
                if(isLast){
                    command.execute(cmdArgs, currentInp, System.out, System.err);
                } else {
                    if(prevInp != null && prevInp != System.in){
                        prevInp.close();
                    }
                    ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                    PrintStream captureOutput = new PrintStream(buffer);
                    captureOutput.flush();
    
                    command.execute(cmdArgs, currentInp, captureOutput, System.err);
                    captureOutput.flush();
    
                    prevInp = currentInp;
                    currentInp = new ByteArrayInputStream(buffer.toByteArray());
                }
    
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } finally{
            if(currentInp != null && currentInp != System.in){
                 try { currentInp.close(); } catch(Exception ignored){}
             }
        }
    }

    public void executeExecutables(){
        Parser parser = new Parser();
        List<Process> processes = new ArrayList<>();
        List<Thread> copiers = new ArrayList<>();
        try {
            for(int i = 0;i < pipeSplittedCommands.size() ; i++){
                String rawString = pipeSplittedCommands.get(i);
                String tokens[] = parser.tokenize(rawString);
                ProcessBuilder pb = new ProcessBuilder(tokens);
                if(i == 0){
                    pb.redirectInput(ProcessBuilder.Redirect.INHERIT);
                }
                pb.redirectErrorStream(true);
                Process p = pb.start();
                processes.add(p);
                if(processes.size() > 1){
                    Process prev = processes.get(i-1);
                    Thread copier = new Thread(new StreamCopier(
                        prev.getInputStream(),
                        p.getOutputStream()
                    ));
                    copier.start();
                    copiers.add(copier);
                }
                
            }

            Thread outputThread = new Thread(() ->{
                Process finalProcess = processes.get(processes.size() -1);
                try(BufferedReader br = new BufferedReader(new InputStreamReader(finalProcess.getInputStream()))){
                    String line;
                    while((line = br.readLine()) != null){
                        System.out.println(line);
                    }
                } catch(IOException e) {
                    System.err.println("Error: " + e.getMessage());
                }
                
            });
            outputThread.start();

            for(Process p : processes){
                int exitCode = p.waitFor();
                if(exitCode != 0){
                    for(Process process : processes){
                        if(process.isAlive()){
                            process.destroyForcibly();
                        }
                    }
                    System.err.println("Process failed with exit code: " + exitCode);
                    return;
                }
            }
            for(Thread t : copiers){
                t.join();
            }
            outputThread.join();
            
            


        } catch (IOException | InterruptedException e) {
            System.err.println("Error while executing executables " + e.getMessage());
        } finally {

            for(Thread t : copiers){
                if(t.isAlive()){
                    t.interrupt();
                }
            }

            for(Process p : processes){
                if(p.isAlive()){
                    p.destroyForcibly();
                }
            }
            
        }
    }

    public void executeMixedCommands(){
        InputStream currentInput = System.in;
        
        try {
            for(int i = 0; i < tokensArray.size(); i++){
                String[] tokens = tokensArray.get(i);
                boolean isLast = (i == tokensArray.size() - 1);
                
                if(currentCommandTracker.get(i) == CommandTypes.BuiltIn){
                    // Execute built-in command
                    Command command = CommandFactory.getCommand(tokens[0]);
                    String[] cmdArgs = Arrays.copyOfRange(tokens, 1, tokens.length);
                    
                    if(isLast){
                        command.execute(cmdArgs, currentInput, System.out, System.err);
                    } else {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        PrintStream ps = new PrintStream(baos);
                        command.execute(cmdArgs, currentInput, ps, System.err);
                        ps.flush();
                        
                        if(currentInput != System.in) currentInput.close();
                        currentInput = new ByteArrayInputStream(baos.toByteArray());
                    }
                    
                } else {
                    // Execute external command
                    ProcessBuilder pb = new ProcessBuilder(tokens);
                    pb.redirectErrorStream(true);
                    Process p = pb.start();
                    
                    // Feed input to process
                    if(currentInput != System.in){
                        try(OutputStream os = p.getOutputStream()){
                            currentInput.transferTo(os);
                        }
                        currentInput.close();
                    }
                    
                    if(isLast){
                        // Output to System.out
                        try(BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()))){
                            String line;
                            while((line = br.readLine()) != null){
                                System.out.println(line);
                            }
                        }
                    } else {
                        // Capture output for next command
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        p.getInputStream().transferTo(baos);
                        currentInput = new ByteArrayInputStream(baos.toByteArray());
                    }
                    
                    p.waitFor();
                }
            }
        } catch(IOException | InterruptedException e){
            System.err.println("Error: " + e.getMessage());
        } finally {
            if(currentInput != System.in){
                try { currentInput.close(); } catch(Exception ignored){}
            }
        }
    }

    public List<String> splitByPipes(String rawInput){
        List<String> commands = new ArrayList<>();
        boolean isInsideDoubleQuotes = false;
        boolean isInsideSingleQuotes = false;
        StringBuilder sb = new StringBuilder();
        for(char ch : rawInput.toCharArray()){
            if (ch == '\'' && !isInsideDoubleQuotes){
                isInsideSingleQuotes = !isInsideSingleQuotes;
                sb.append(ch);
            } else if (ch == '\"' && !isInsideSingleQuotes ) {
                isInsideDoubleQuotes = !isInsideDoubleQuotes;
                sb.append(ch);
            } else if(ch == ' ' && (isInsideDoubleQuotes || isInsideSingleQuotes)){
                sb.append(ch);
            } else if (ch == '|' && (!isInsideDoubleQuotes && !isInsideSingleQuotes)){
                String segment = sb.toString().trim();
                if(segment.length() > 0){
                    commands.add(segment);
                }
                sb.setLength(0);
            } else {
                sb.append(ch);
            }
        }
        if(sb.length() > 0){
            commands.add(sb.toString());
        }
        return commands;
    }

    public boolean hasPipe(){
        return pipeSplittedCommands.size() > 1;
    }

    public boolean areCommandsValid(){
        for(String s : commandsList){
            if(!BuiltInCmdHandler.isBuiltInCommand(s) && !ExecutableHandler.checkIfItIsAnExecutable(s)){
                System.err.println(s + ": command not found");
                return false;  
            } 
        }
        return true;
    }

    public boolean isBuiltIn(){
        for(String s : commandsList){
            if(!BuiltInCmdHandler.isBuiltInCommand(s)) return false;
        }
        return true;   
    }

    public boolean isExecutable(){
        for(String s : commandsList){
            if(!ExecutableHandler.checkIfItIsAnExecutable(s)) return false;
        }
        return true;
    }

    private Command getCommand(String commandName) {
        return CommandFactory.getCommand(commandName);
        
    }

    private List<String> getCommandsOnlyAndPopulateTokensArray(){
        List<String> cmdList = new ArrayList<>();
        tokensArray = new ArrayList<>();
        Parser parser = new Parser();
        for(String rawLine : pipeSplittedCommands){
            String tokens[] = parser.tokenize(rawLine);
            tokensArray.add(tokens);
            cmdList.add(tokens[0]);
        }
        return cmdList;
    }

    private void initializeCurrentCommandsAndNextCommands(){
        currentCommandTracker = new ArrayList<>();

        for(int i = 0;i<commandsList.size();i++){
            if(BuiltInCmdHandler.isBuiltInCommand(commandsList.get(i))){
                currentCommandTracker.add(CommandTypes.BuiltIn);
            } else {
                currentCommandTracker.add(CommandTypes.Executable);
            }
        }
    }

    
}
