package org.frictional.EmilsLooter;

import org.frictional.EmilsLooter.Enums.Stating;
import org.frictional.EmilsLooter.GUI.InterfaceGUI;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Login;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.input.Keyboard;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.event.Event;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.script.events.LoginScreen;
import org.rspeer.ui.Log;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@ScriptMeta(developer = "Frictional", name = "Emils Personal Looter ", desc = "Automated baby SHEEEESH")
public class Main extends Script implements ChatMessageListener {

    private static Stating currentState = Stating.STARTING;
    private static boolean onStartCalled = false;
    public static final Area CWARS_AREA = Area.rectangular(2434, 3098, 2448, 3080);
    public static boolean ANTI_SPAM = false;
    public static boolean interruptedWithdrawing = false;
    public static boolean finishedWithdrawing = false;
    public static String MULE_NAME = null;
    public static List<String> accounts = new ArrayList<String>();
    public LoginScreen n = new LoginScreen(this);
    @Override
    public void onStart() {
        stopLogging();
        new InterfaceGUI();
        super.onStart();
    }

    @Override
    public int loop() {


        if(MULE_NAME != null && Game.isLoggedIn()) {
            currentState.execute();
        }

        if(!Game.isLoggedIn() && !accounts.isEmpty()){
            String[] temp = accounts.get(0).split(":");
            loginToAccount(temp[0], temp[1]);
            Time.sleep(500, 1000);
        }

        return Random.nextInt(500, 1000);
    }

    public static Stating getBestState() {

       if(!Inventory.isFull() || interruptedWithdrawing){
           return Stating.BANKING;
       }


        return null;
    }

    public static void updateScriptState(Stating inState) {
        if (inState != null) {
            //onStartCalled = false;
            currentState = inState;
        }
    }

    public static void readFile(String l) throws FileNotFoundException {
        File myFile = new File(l);
        Scanner myReader = new Scanner(myFile);

        while(myReader.hasNext()){
            String element = myReader.next();
            accounts.add(element);
        }
        myReader.close();

    }

    private static void loginToAccount(String s, String s1) {
        if(!Game.isLoggedIn()){
            Login.enterCredentials(s,s1);
            Keyboard.pressEnter();
            Keyboard.pressEnter();
            Time.sleep(2000, 4000);
        }

        if(Login.getState() == Login.STATE_INVALID_CREDENTIALS){
            accounts.remove(0);
            Time.sleep(1000, 2000);
        }
    }

    public void stopLogging() {
        removeBlockingEvent(LoginScreen.class);
    }

    public void startLogging() {
        addBlockingEvent(n);

    }

    public static void addName(String s) {
    }


    @Override
    public void notify(ChatMessageEvent chatMessageEvent) {
        String Message = chatMessageEvent.getMessage();

        if (chatMessageEvent.getType() == ChatMessageType.SERVER) {
            if (Message.contains("Your account is currently restricted")) {
                updateScriptState(Stating.LOGOUT);
            }
        }
    }
}
