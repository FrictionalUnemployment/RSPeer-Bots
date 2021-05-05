package org.frictional.serverMule;


import org.frictional.serverMule.Enums.Stating;
import org.frictional.serverMule.Muler.Server;
import org.rspeer.runetek.api.Login;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.InterfaceOptions;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptBlockingEvent;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.events.LoginScreen;
import org.rspeer.ui.Log;

import java.util.HashSet;


@ScriptMeta(name = "Server Muler", developer = "Frictional", desc = "Automated Muler baby")
public class Main extends Script implements ChatMessageListener {
    private static Stating currentState = null;
    private static Stating previousState;

    public static int World;
    public static String Name;
    public static int startAmount = 500000;
    public static boolean finishedTrading = false;
    public static boolean onStartTrade = true;


    public static HashSet<String> names = new HashSet<String>();
    static Server server = new Server(45342);
    LoginScreen loginScreen = new LoginScreen(this);
    @Override
    public void onStart() {
        stopLogging();
        super.onStart();
    }

    public static void addName(String e) {

        names.add(e);

        for(String n : names) {
            Log.fine("We're trying to send info " + n);

            server.write(n);
        }

    }


    public void stopLogging() {
        removeBlockingEvent(LoginScreen.class);
    }
    public void startLogging() {
        addBlockingEvent(loginScreen);

    }



    @Override
    public void onStop() {
        stopServer();
    }

    @Override
    public int loop() {
        server.listen();
        //Log.fine(names);
        if(currentState != null){
            if(currentState == Stating.HOPTOWORLD){
                startLogging();
            }else if(currentState == Stating.LOGOUT){
                stopLogging();
            }
            Log.fine(currentState);
            currentState.execute();

        }
        return Random.nextInt(500, 1000);
    }

    public static void updateScriptState(Stating inState) {
        if(inState != null) {
            previousState = currentState;
            currentState = inState;
        }
    }
    public static void stopServer() { server.close(); }

    @Override
    public void notify(ChatMessageEvent chatMessageEvent) {
        String Message = chatMessageEvent.getMessage();
        if(chatMessageEvent.getType() == ChatMessageType.TRADE_INFO) {
            if (Message.contains("Accepted ")) {
                    updateScriptState(Stating.LOGOUT);
                    finishedTrading = true;
            }
            }
    }
}
