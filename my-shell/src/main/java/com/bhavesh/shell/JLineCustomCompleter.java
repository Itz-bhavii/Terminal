package com.bhavesh.shell;

import java.util.ArrayList;
import java.util.List;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

public class JLineCustomCompleter implements Completer{

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        if(line.wordIndex() == 0 && line.wordCursor() > 0){
            ArrayList<String> matchingCommands = findMatchingCommands(line.word());
            if(matchingCommands.isEmpty()){
                Terminal terminal = reader.getTerminal();
                terminal.puts(InfoCmp.Capability.bell);
                terminal.flush();
            } else {
                for(String command : matchingCommands){
                    candidates.add(new Candidate(command));
                }
            }
        }
    }

    private ArrayList<String> findMatchingCommands(String command){
        ArrayList<String> matchingCommands = BuiltInCmdHandler.checkAndReturnBuiltInCommands(command);
        matchingCommands.addAll(ExecutableHandler.checkAndReturnExecutables(command));
        return matchingCommands;

    }
    
}
