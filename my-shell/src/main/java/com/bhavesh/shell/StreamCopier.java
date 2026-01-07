package com.bhavesh.shell;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class StreamCopier implements Runnable{

    // 4096 bytes = typical OS page size, optimal for I/O
    private static int BUFFER_SIZE = 4096;
    InputStream input;
    OutputStream output;

    
    public StreamCopier(InputStream input,OutputStream output){
        this.input = input;
        this.output = output;
    }

    @Override
    public void run() {
        try {
            byte buffer[] = new byte[BUFFER_SIZE];
            int bytesRead;
            while((bytesRead = input.read(buffer)) != -1){
                output.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Stream copy error: " + e.getMessage());
        } finally{
            try { output.close(); } catch (Exception ignored) {}
            try { input.close(); } catch (Exception ignored) {}
        }
    }
    
}
