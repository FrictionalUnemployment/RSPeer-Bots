package org.frictional.EmilsLooter.Muler;

import org.frictional.EmilsLooter.Main;
import org.rspeer.ui.Log;

import java.io.*;
import java.util.function.Consumer;

public class IOHandler implements IO, Runnable {

    private BufferedReader reader;
    private PrintWriter writer;
    private Consumer<String> readHandler;
    private boolean listen = true;

    public IOHandler(InputStream inputStream, OutputStream outputStream) {
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new PrintWriter(outputStream, true);
        readHandler = Main::addName;
    }

    @Override
    public void write(String output) {
        writer.println(output);
    }

    @Override
    public void setReadHandler(Consumer<String> readHandler) {
        this.readHandler = readHandler;
    }

    @Override
    public void stopListener() {
        this.listen = false;
    }

    private void read(Consumer<String> readHandler, String string) {
        readHandler.accept(string);
    }

    @Override
    public void run() {
        String input;
        try {
            while (listen && (input = reader.readLine()) != null) {
                read(readHandler, input);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}