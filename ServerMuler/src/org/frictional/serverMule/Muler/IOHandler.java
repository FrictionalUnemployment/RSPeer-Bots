package org.frictional.serverMule.Muler;

import org.frictional.serverMule.Enums.Stating;
import org.frictional.serverMule.Main;
import org.rspeer.ui.Log;

import java.io.*;
import java.util.HashSet;
import java.util.function.Consumer;

public class IOHandler implements IO, Runnable {

    private BufferedReader reader;
    private PrintWriter writer;
    private Consumer<String> readHandler;
    private boolean listen = true;

    public IOHandler(InputStream inputStream, OutputStream outputStream){
        reader = new BufferedReader(new InputStreamReader(inputStream));
        writer = new PrintWriter(outputStream, true);
        readHandler = Main::addName; // default read behaviour

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


    private void read(Consumer<String> readHandler, String string, Stating t){
        if(t != null) {

            if(string.contains("STARTMULER")){

                Main.onStartTrade = true;
            }else{
                Main.onStartTrade = false;
            }

            Main.Name = string.split(" ")[0];

            Main.World = Integer.parseInt(string.split(" ")[1]);
            Log.fine(" World: " + Main.World + " Player Name: " + Main.Name + " Start Trading: " +  Main.onStartTrade);
            Main.updateScriptState(t);
            readHandler.accept(Main.Name);
        }
    }

    @Override
    public void run() {
        String input;

        try{
            while(listen && (input = reader.readLine()) != null){

                    read(readHandler, input, Stating.START);

            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}