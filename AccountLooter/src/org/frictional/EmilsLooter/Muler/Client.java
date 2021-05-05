package org.frictional.EmilsLooter.Muler;

import java.io.IOException;
import java.net.Socket;
import java.util.function.Consumer;

public class Client {

    private Socket socket;
    private final String HOST;
    private final int PORT;
    private IO ioHandler;
    private Consumer<String> readHandler;

    public Client(String HOST, int PORT) {
        this.HOST = HOST;
        this.PORT = PORT;
    }

    /*
        overloaded constructor to provide a way to handle read events prior to
        connecting to server
    */
    public Client(String HOST, int PORT, Consumer<String> readHandler) {
        this.HOST = HOST;
        this.PORT = PORT;
        this.readHandler = readHandler;
    }

    public void connect() {
        try {
            socket = new Socket(HOST, PORT);
            ioHandler = new IOHandler(socket.getInputStream(), socket.getOutputStream());

            if (readHandler != null) {
                ioHandler.setReadHandler(readHandler);
            }

            new Thread((Runnable) ioHandler).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String msg) {
        ioHandler.write(msg);
    }

    public void setReadHandler(Consumer<String> readHandler) {
        ioHandler.setReadHandler(readHandler);
    }

    public void close() {
        try {
            ioHandler.stopListener();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}