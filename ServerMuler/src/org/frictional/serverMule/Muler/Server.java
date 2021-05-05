package org.frictional.serverMule.Muler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.function.Consumer;

public class Server {

    private ServerSocket server;
    private final int PORT;
    private IO ioHandler;
    private Consumer<String> readHandler;

    public Server(int PORT){
        this.PORT = PORT;
    }

    Server(int PORT, Consumer<String> readHandler){
        this.PORT = PORT;
        this.readHandler = readHandler;
    }

    public Consumer<String> getString(){
        return readHandler;
    }

    public void listen(){
        try{
            server = new ServerSocket(PORT);
            Socket client = server.accept();
            ioHandler = new IOHandler(client.getInputStream(), client.getOutputStream());

            if(readHandler != null){
                ioHandler.setReadHandler(readHandler);
            }

            new Thread((Runnable) ioHandler).start();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void write(String msg){
        ioHandler.write(msg);
    }

    public void setReadHandler(Consumer<String> reader){
        ioHandler.setReadHandler(reader);
    }

    public void close(){
        try{
            ioHandler.stopListener();
            server.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}