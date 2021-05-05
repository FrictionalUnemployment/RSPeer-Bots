package org.frictional;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.Login;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.DepositBox;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

@ScriptMeta(name = "Amethyst script", desc = "Mines amethyst", developer = "Frictional")
public class Main extends Script implements RenderListener, ChatMessageListener {
   public static boolean isMining;
    private static Position currentRock;
    String status;
    int amethystCount = 0;
    public static int startXp = 0;

    public static Position getCurrentRock() {
        return currentRock;
    }

    public static void setCurrentRock(Position c) {
        currentRock = c;
        isMining = false;
    }


    private static SceneObject getNextRock() {
        try {
            return SceneObjects.getNearest(11388, 11389);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
        startXp = Skills.getExperience(Skill.MINING);
    }



    @Override
    public int loop() {
     /*   if(!isMining && Combat.getSpecialEnergy() == 100){
            if(Combat.toggleSpecial(true)){
                status = "Using special attack";
                Time.sleepWhile(() -> Combat.getSpecialEnergy() == 100, 200, 700);
            }
        } */

        Position cached = getCurrentRock();
        SceneObject next;
       if(cached != null){
           next = SceneObjects.newQuery().on(cached).nameContains("Crystals").results().nearest();
           if(next == null){
               status = "Ore has been mined";
               setCurrentRock(null);
           }
       }else {
           setCurrentRock(getNextRock().getPosition());
       }

        if(getCurrentRock() != null && !Players.getLocal().isMoving() && !isMining && !Inventory.isFull()){
            status = "Mining an ore";
            mineAmethyst();
        }

        if(Inventory.isFull()){
            status = "Banking";
            goBank();
        }
        return Random.nextInt(300, 800);
    }



    private void goBank() {
        SceneObject deposit = SceneObjects.getNearest(10529);
        if(!DepositBox.isOpen() && !Players.getLocal().isMoving()){
            if(deposit.click()){
                isMining = false;
                Time.sleepWhile(() -> Players.getLocal().isMoving(), 800, 1900);
            }
        }else if(DepositBox.isOpen() && Inventory.isFull()){
            if(Interfaces.getComponent(192, 4).click()){
                isMining = false;
                status = "Depositing";
                Time.sleepWhile(() -> Inventory.isFull(), 500, 1000);
            }
        }
    }

    private void mineAmethyst() {
        SceneObject m = getNextRock();
        if(getCurrentRock() != null){
            if(m.click()){
                isMining = true;
                Time.sleepWhile(() -> Players.getLocal().isAnimating(), 2000, 4000);
            }
        }


    }

    private long startTime;
    private final int PRICE_BLOODS = 3654;
    private final int PRICE_SAPPHIRE = 488;
    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics g = renderEvent.getSource();
        long runningTime = System.currentTimeMillis() - startTime;
        int bloodsPerHour = (int) (amethystCount / ((System.currentTimeMillis() - startTime) / 3600000.0D));
        int gpGained = amethystCount * PRICE_BLOODS + sapphireCount * PRICE_SAPPHIRE;
        int xpGained = Skills.getExperience(Skill.MINING) - startXp;
        int xpPerHour = (int) (xpGained / ((System.currentTimeMillis() - startTime) / 3600000.0D));
        int gpPerHour = (int) (gpGained / ((System.currentTimeMillis() - startTime) / 3600000.0D));


        Graphics2D g2d = (Graphics2D) g;

        g2d.setColor(Color.GREEN);
        g2d.drawString("Exp per Hour: " + xpPerHour, 165, 300);
        g2d.drawString("Runtime: " + formatTime(runningTime), 20, 300);
        g2d.drawString("Mined: " + Integer.toString(amethystCount) + " (" + Integer.toString(bloodsPerHour) + ")", 20, 323);
        g2d.drawString("GP Gained: " + gpGained + " (" + gpPerHour + ")", 165, 323);
        g2d.drawString("Status: " + status, 165, 350);
    }
    private String formatTime(long r){

        //long days = TimeUnit.MILLISECONDS.toDays(r);
        long hours = TimeUnit.MILLISECONDS.toHours(r);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(r) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(r));
        long seconds = TimeUnit.MILLISECONDS.toSeconds(r) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(r));
        String res = "";

        //Pretty Print the time so it will always be in this format 00:00:00
        if( hours < 10 ){
            res = res + "0" + hours + ":";
        }
        else{
            res = res + hours + ":";
        }
        if(minutes < 10){
            res = res + "0" + minutes + ":";
        }
        else{
            res = res + minutes + ":";
        }
        if(seconds < 10){
            res = res + "0" + seconds;
        }
        else{
            res = res + seconds;
        }

        return res;
    }
        int sapphireCount = 0;
    @Override
    public void notify(ChatMessageEvent e) {

        if(e.getMessage().contains("You manage to mine some amethyst.")){
            amethystCount++;
        }
        if(e.getMessage().contains("You just found a Sapphire!")){
            sapphireCount++;
        }
        if(e.getMessage().contains("You just found a Ruby!")){
           // sapphireCount++;
        }
        if(e.getMessage().contains("Welcome to Old School RuneScape.")){
            isMining = false;
        }
        if(e.getMessage().contains("You manage to mine some amethyst.")){
            isMining = false;
        }
    }
}
