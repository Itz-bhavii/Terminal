package com.bhavesh.shell;

import java.util.ArrayList;

public class Parser {

       public String[] tokenize(String rawInput){
        boolean isInsideSingleQuotes = false;
        boolean isInsideDoubleQuotes = false;
        boolean isEscapeSequence = false;
        ArrayList<String> tokenList = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for(char c : rawInput.toCharArray()){
            if(c == '\'' && !isInsideDoubleQuotes && !isEscapeSequence){
                isInsideSingleQuotes = !isInsideSingleQuotes;
            } else if(c == '\"' && !isInsideSingleQuotes && !isEscapeSequence){
                isInsideDoubleQuotes = !isInsideDoubleQuotes;
            } else if(c == ' ' && (isInsideDoubleQuotes || isInsideSingleQuotes)){
                //append space 
                sb.append(c);
            } else if(c == ' ' && (!isInsideDoubleQuotes && !isInsideSingleQuotes) && !isEscapeSequence){
                //complete the token and add to the tokenlist
                if(sb.length() > 0){
                    tokenList.add(sb.toString());
                    sb.setLength(0);
                }
            } else if(c == '\\' && !isEscapeSequence && !isInsideSingleQuotes){
                isEscapeSequence = true;

            } else if(isEscapeSequence){
                //double quotes ke andar sirf yahi characters accept hote hai. if inke alawa koi dusra character aaya to phir \ hi append karo.
                if(isInsideDoubleQuotes && c != '\\' && c != '"' && c != '$' && c != '`'){
                    sb.append('\\');
                }
                sb.append(c);
                isEscapeSequence = false;
            } else {
                sb.append(c);
            }
        }
        if(isEscapeSequence){
            sb.append('\\');
        }
        if(isInsideDoubleQuotes || isInsideSingleQuotes){
            System.out.println("warning: unclosed quotes");
            return new String[0];
        }
        if(sb.length() > 0){
            tokenList.add(sb.toString());
        }

        return tokenList.toArray(new String[0]);

    } 
}
