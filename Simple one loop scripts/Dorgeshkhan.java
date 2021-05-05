package org.frictional;

import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.PathingEntity;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.input.Keyboard;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptCategory;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;
import java.security.Permission;
import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@ScriptMeta(name =  "Dorgees", developer = "Frictional", desc = "Private", version = 1.1, category = ScriptCategory.MONEY_MAKING)
public class Main extends Script implements RenderListener {

    Area dorgeeshArea = Area.rectangular(2685, 5381, 2751, 5281);
    Area urlunurpenArea = Area.rectangular(2689, 5281, 2750, 5252);
    Area insideurlun = Area.rectangular(2746, 5267, 2749, 5261);
    Area insideMarket = Area.rectangular(2710, 5311, 2724, 5302);
    Area urVass = Area.rectangular(2721, 5335, 2734, 5328);
    Area urMeg = Area.rectangular(2690, 5306, 2692, 5301);
    Area urTag = Area.rectangular(2736, 5363, 2729, 5368);
    Area urtaalurzeek = Area.rectangular(2712, 5358, 2719, 5353);
    Area dorgeeshBank = Area.rectangular(2700, 5354, 2706, 5344);
    Player getLocalPlayer = Players.getLocal();
    int[] sellitems = {7198, 7919};
    int[] gourmet = {2307, 2306, 2305, 2304};
    int NPC_DIALOG = 231;
    int PIE_PRICE = 930;
    int WINE_PRICE = 1500;
    //IF WINE = FALSE IF PIE = TRUE;
    boolean WineorPie;
    String[] TALKTO_NPC = {"Ur-lun", "Ur-meg", "Ur-pel", "Ur-taal", "Ur-tag", "Ur-vass", "Ur-zek"};
    String[] RESPONSE = {"That smells nice; what is it?", "Mmm, that smells good... What is it?",
                        "So what's that then?", "Eww, what's that?", "Yuck, that looks horrid. What is it?"};
    String PERMISSION_NPC = "";
    int PRIORITY_PRICE = 0;
    int PRICE_REDUCTION = 0;
    String[] EXCITED_RESPONSE = {"Ooh, I've never had that before. How exciting!", "Oh, I think I've heard people talking about that."
    , "Oh, of course. I've had that occasionally.", "Oh, we have that all the time.", "That again?"};
                                    //Ur-lun ur-pel
    Position[] NPC_POSITIONS = {new Position(2746,5264, 0)};
    float PRICE_TO_SELL = 0;
    int prev;
    long TIME_FOR_DISCOUNT;
    long startTime;
    public static int soldFood;
    public static int PIE_PRICE_GE = 500;
    public static int WINE_PRICE_GE = 520;
    public static int PIE_COUNTER;
    public static int WINE_COUNTER;
    @Override
    public void onStart() {
        startTime = System.currentTimeMillis();
        super.onStart();

    }
    Timer timer;
    public void Reminder(int seconds, boolean pow) {
        timer = new Timer();
        timer.schedule(new Reset(pow), seconds*1000);
    }

    class Reset extends TimerTask {
        int RESET_PIE;
        String WINE_OR_PIE;
        public Reset(boolean pow) {
            if(pow){
               RESET_PIE = 1;

            }else if(!pow){
                RESET_PIE = 0;
            }
        }

        @Override
        public void run() {

            if(RESET_PIE == 1){
                WINE_OR_PIE = "PIE";
                PIE_COUNTER--;
            }else if(RESET_PIE == 0){
                WINE_OR_PIE = "WINE";
                WINE_COUNTER--;
            }
            Log.fine("60 Seconds has passed: " + formatTime(System.currentTimeMillis() - startTime)
            + "Resetting: " + WINE_OR_PIE);
            timer.cancel();
        }
    }

    @Override
    public int loop() {
        if(Inventory.contains(sellitems) && dorgeeshArea.contains(getLocalPlayer)
        && PERMISSION_NPC.isEmpty()){
            sellItems();
        }

        if(!PERMISSION_NPC.isEmpty()){
            getPermission();
        }

        if(PERMISSION_NPC.isEmpty() && !dorgeeshArea.contains(getLocalPlayer)){
            walkToCenter();
        }

        if(dorgeeshArea.contains(getLocalPlayer) && !Inventory.contains(sellitems)){
            getFoodFromBank();
        }


        return Random.nextInt(100, 900);
    }

    private void getFoodFromBank() {
        SceneObject t = SceneObjects.getNearest("Bank booth");
        if(!Bank.isOpen() && t != null && !Players.getLocal().isMoving() && t.click()){
            Time.sleepUntil(() -> Bank.isOpen(), 500, 1000);
        }else if(Bank.isOpen() && !Inventory.contains(sellitems) &&
        Bank.withdraw("Admiral pie", 14) && Bank.withdraw("Bottle of wine", 14)){
            Time.sleepUntil(() -> Inventory.contains(sellitems), 500, 1000);
        }else if(!Bank.isOpen() && dorgeeshBank.getCenter().distance() > 13
        && !Players.getLocal().isMoving() && Movement.walkTo(dorgeeshBank.getCenter())){
            Time.sleepUntil(() -> dorgeeshBank.contains(getLocalPlayer), 500, 1000);
        }

    }

    private void walkToCenter() {
    if(urlunurpenArea.contains(getLocalPlayer)){
        SceneObject l1 = SceneObjects.newQuery().on(new Position(2713, 5282, 1)).names("Stairs").results().nearest();

        SceneObject l = SceneObjects.newQuery().on(new Position(2713, 5278, 0)).names("Stairs").results().nearest();
        if(l != null && getLocalPlayer.getFloorLevel() == 0 && l.distance() < Random.nextInt(10, 20) && l.click()){
            Time.sleepUntil(() -> getLocalPlayer.getFloorLevel() != 0, 500, 1250);
        }else if(l.distance() > 10 && Movement.walkTo(l.getPosition())){
            Time.sleepUntil(() -> !Players.getLocal().isMoving(), 500, 1250);

        }
    }
        SceneObject l1 = SceneObjects.newQuery().on(new Position(2713, 5282, 1)).names("Stairs").results().nearest();
        if(getLocalPlayer.getFloorLevel() == 1 && l1 != null && l1.distance() < 2 && l1.click()){
            Time.sleepUntil(() -> getLocalPlayer.getFloorLevel() == 0, 500, 1000);
        }
        Log.fine("Hi");
        SceneObject l3 = SceneObjects.newQuery().on(new Position(2729,5346, 1)).names("Stairs").results().nearest();
        if(new Position(2731,5347, 1).distance() > 5 && getLocalPlayer.getFloorLevel() != 0 && Movement.walkTo(new Position(2731,5347, 1))){
            Time.sleepUntil(() -> new Position(2731,5347, 1).distance() <= 5 , 500, 1500);

        }else if (l3 != null && new Position(2731,5347, 1).distance() <= 5 && getLocalPlayer.getFloorLevel() != 0
        && l3.click()){
            Time.sleepUntil(() -> getLocalPlayer.getFloorLevel() == 1 , 500, 1500);

        }

    }

    private void getPermission() {
        Npc p = Npcs.getNearest(PERMISSION_NPC);
        if(p != null && !PERMISSION_NPC.isEmpty() && p.distance() < Random.nextInt(10, 15) &&
        !Interfaces.isOpen(219) && !Interfaces.isOpen(231) && !Interfaces.isOpen(217)
                && PERMISSION_NPC != "Ur-tag" && p.click()
        || p != null && !Interfaces.isOpen(219) && !Interfaces.isOpen(231) && !Interfaces.isOpen(217)
                && PERMISSION_NPC == "Ur-tag" && p.distance() < 5 && p.click()){
            Time.sleepUntil(() -> Interfaces.isOpen(219), 500, 2000);
        }else if(Interfaces.isOpen(219) && Interfaces.getComponent(219,1).getComponent(2).isVisible()
        && Dialog.process("May I have permission to sell food in the market?")
        || Dialog.isOpen() && Dialog.process("Ask for permission to sell food in the market")){
            Time.sleepUntil(() -> !Interfaces.isOpen(219), 500, 1000);

        }else if(Dialog.isOpen() && Dialog.canContinue() && Dialog.getContinue().click()){
            Time.sleepUntil(() -> !Dialog.canContinue(), 500, 1800);
        }

        if(Interfaces.isOpen(217)){
            Keyboard.pressEventKey(32);
            Time.sleepUntil(() -> !Interfaces.isOpen(217), 500, 2000);
            PERMISSION_NPC = "";
        }

        if(PERMISSION_NPC == "Ur-lun" && !Interfaces.isOpen(219) || PERMISSION_NPC == "Ur-pel" && !Interfaces.isOpen(219)){
            if(dorgeeshArea.contains(getLocalPlayer) && getLocalPlayer.getPosition().getFloorLevel() == 0){

                SceneObject l = SceneObjects.newQuery().on(new Position(2713, 5282)).names("Stairs").results().nearest();
                    if(l.distance() < 10 && l.click()){
                        Time.sleepUntil(() -> getLocalPlayer.getPosition().getFloorLevel() != 0, 500, 1500);
                    }else if(Movement.walkToRandomized(l.getPosition())){
                        Time.sleepUntil(() -> !Players.getLocal().isMoving(), 700, 1800);
                    }
            }else if(getLocalPlayer.getPosition().getFloorLevel() != 0){
                SceneObject l = SceneObjects.newQuery().on(new Position(2713, 5279, 1)).names("Stairs").results().nearest();
                if(l.distance() < Random.nextInt(10, 20) && l.click()) {
                    Time.sleepUntil(() -> getLocalPlayer.getPosition().getFloorLevel() == 0, 500, 1500);
                }

                }else if(urlunurpenArea.contains(getLocalPlayer) && !getLocalPlayer.isMoving() && !insideurlun.contains(getLocalPlayer) && Movement.walkToRandomized(insideurlun.getCenter())){
                Time.sleepUntil(() -> insideurlun.contains(getLocalPlayer), 1000, 1500);

            }

        }

        if(PERMISSION_NPC == "Ur-vass" && !Interfaces.isOpen(219) && !Interfaces.isOpen(231) && !Interfaces.isOpen(217)){
            if(!Players.getLocal().isMoving() && Movement.walkTo(urVass.getCenter())){
                Time.sleepUntil(() -> urVass.contains(getLocalPlayer), 500, 2000);
            }
        }
        if(Interfaces.isOpen(231) && Interfaces.getComponent(231, 2).getText().contains(PERMISSION_NPC)){
            Keyboard.pressEventKey(32);
            Time.sleepUntil(() -> !Interfaces.isOpen(217), 500, 2000);

        }

         if(PERMISSION_NPC == "Ur-meg" && !Interfaces.isOpen(219) && !Interfaces.isOpen(231) && !Interfaces.isOpen(217)){
            if(!Players.getLocal().isMoving() && Movement.walkTo(urMeg.getCenter())){
                Time.sleepUntil(() -> urMeg.contains(getLocalPlayer), 500, 2000);
            }
        }
        SceneObject l = SceneObjects.newQuery().on(new Position(2728, 5346)).names("Stairs").results().nearest();
         if(PERMISSION_NPC == "Ur-tag" && !Players.getLocal().isMoving() && new Position(2730, 5366, 1).distance() > 3 && !Interfaces.isOpen(219) && !Interfaces.isOpen(231) && !Interfaces.isOpen(217)
         && getLocalPlayer.getFloorLevel() != 0 && Movement.walkTo(new Position(2730, 5366, 1))){

                Time.sleepUntil(() -> urTag.contains(getLocalPlayer), 500, 1500);
         }else if(PERMISSION_NPC == "Ur-tag" && !Players.getLocal().isMoving() && getLocalPlayer.getFloorLevel() != 1
         && l.distance() > 10 && Movement.walkTo(new Position(2726,5346))){
             Time.sleepUntil(() -> l.distance() < 10, 500, 1500);

         }else if(PERMISSION_NPC == "Ur-tag" && l != null && l.distance() <= 10 && !Players.getLocal().isMoving()
         && getLocalPlayer.getFloorLevel() != 1 && l.click()){
             Time.sleepUntil(() -> getLocalPlayer.getFloorLevel() != 1 , 500, 1500);

         }

         if(PERMISSION_NPC == "Ur-taal" && urtaalurzeek.getCenter().distance() > 10 && !Players.getLocal().isMoving()
         && !Dialog.isOpen() && Movement.walkTo(urtaalurzeek.getCenter())
         || PERMISSION_NPC == "Ur-zek" && urtaalurzeek.getCenter().distance() > 10 && !Players.getLocal().isMoving()
                 && !Dialog.isOpen() && Movement.walkTo(urtaalurzeek.getCenter())){
             Time.sleepUntil(() -> urtaalurzeek.getCenter().distance() <= 10 , 500, 1500);

         }

    }
    int SOLD_TO;
    int ITEM_DISCOUNT = 0;
    int PRICE_WITH_DISCOUNT;
    private void sellItems() {

// Hey, why don't you
            Npc g = Npcs.getNearest(Arrays.stream(gourmet).filter(x -> x != prev && x != SOLD_TO).toArray());
            if (g != null && prev != g.getId() && PERMISSION_NPC.isEmpty() && !Interfaces.isOpen(231)
                    && !Interfaces.isOpen(193) && !Interfaces.isOpen(229)
                    && !Interfaces.isOpen(217) && !Interfaces.isOpen(219)
                    && !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Inventory.getFirst(sellitems).interact("Use")
                    ||
                    Interfaces.isOpen(231) &&  !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Interfaces.getComponent(231, 4).getText().contains(RESPONSE[3]) && Inventory.getFirst(sellitems).interact("Use")
                    || Interfaces.isOpen(231) && !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Interfaces.getComponent(231, 4).getText().contains(RESPONSE[4]) && Inventory.getFirst(sellitems).interact("Use")
                    || Interfaces.isOpen(231)  && !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Interfaces.getComponent(231, 4).getText().contains("Since I'm trying") && Inventory.getFirst(sellitems).interact("Use")
            || Interfaces.isOpen(231)  && !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Interfaces.getComponent(231, 4).getText().contains("I tell you what") && Inventory.getFirst(sellitems).interact("Use")
            || Interfaces.isOpen(231)  && !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Interfaces.getComponent(231, 4).getText().contains("Why don't you try some") && Inventory.getFirst(sellitems).interact("Use")
                    || Interfaces.isOpen(231)  && !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Interfaces.getComponent(231, 4).getText().contains("Here, have some of") && Inventory.getFirst(sellitems).interact("Use")
            || Interfaces.isOpen(231)  && !getLocalPlayer.isMoving() && !Inventory.isItemSelected() && Interfaces.getComponent(231, 4).getText().contains("Surface-dweller") && Inventory.getFirst(sellitems).interact("Use")) {
                Time.sleepUntil(() -> Dialog.isOpen(), 500, 1000);
                PRIORITY_PRICE = 0;
            } else if (Inventory.isItemSelected() && PERMISSION_NPC.isEmpty() && !getLocalPlayer.isMoving() && g.interact("Use")) {
                    prev = g.getId();
                Time.sleepUntil(() -> Dialog.isOpen(), 500, 2500);

            } else if (g == null && !insideMarket.contains(getLocalPlayer) && Movement.walkTo(insideMarket.getCenter())) {
                Time.sleepUntil(() -> !getLocalPlayer.isMoving(), 500, 1000);
            }
        if(Interfaces.isOpen(231) && Interfaces.getComponent(231, 4).getText().contains(RESPONSE[1])){
                PRIORITY_PRICE = 3;
                Log.fine(PRIORITY_PRICE);
                Keyboard.pressEventKey(32);
                Time.sleepUntil(() -> !Interfaces.isOpen(231), 500, 500);
        }else if(Interfaces.isOpen(231) && Interfaces.getComponent(231, 4).getText().contains(RESPONSE[0])){
            PRIORITY_PRICE = 2;
            Log.fine(PRIORITY_PRICE);

            Keyboard.pressEventKey(32);
            Time.sleepUntil(() -> !Interfaces.isOpen(231), 500, 500);

        }else if(Interfaces.isOpen(231) && Interfaces.getComponent(231, 4).getText().contains(RESPONSE[2])){
            PRIORITY_PRICE = 1;
            Log.fine(PRIORITY_PRICE);

            Keyboard.pressEventKey(32);
            Time.sleepUntil(() -> !Interfaces.isOpen(231), 500, 500);

        }
        if(Interfaces.isOpen(193) && Interfaces.getComponent(193,1).getItemId() == 7198){
            Log.fine("Selling pie");
            WineorPie = true;
            Keyboard.pressEventKey(32);
            Time.sleepUntil(() -> !Interfaces.isOpen(193), 500, 500);

        }else if(Interfaces.isOpen(193) && Inventory.getFirst(sellitems).getId() == 7919){
            WineorPie = false;
            Log.fine("Selling Wine");
            Keyboard.pressEventKey(32);
            Time.sleepUntil(() -> !Interfaces.isOpen(193), 500, 500);
        }

        if(Interfaces.isOpen(231) && Interfaces.getComponent(231,4).getText().contains(EXCITED_RESPONSE[0])
       && Interfaces.getComponent(231, 3).click()){
            Time.sleepUntil(() -> Interfaces.isOpen(231), 500, 500);
        }else if(Interfaces.isOpen(231) && Interfaces.getComponent(231, 4).getText().contains("How much do want for each one")
        && Interfaces.getComponent(231, 3).click()){
            Time.sleepUntil(() -> Interfaces.isOpen(231), 500, 500);
        }


            if(Interfaces.isOpen(231) && Interfaces.getComponent(231,4).getText().contains(EXCITED_RESPONSE[0])
                    && Dialog.getContinue().click()
            ||
                    Interfaces.isOpen(231) && Interfaces.getComponent(231,4).getText().contains(EXCITED_RESPONSE[1])
                            && Dialog.getContinue().click()
            ||
                    Interfaces.isOpen(231) && Interfaces.getComponent(231,4).getText().contains(EXCITED_RESPONSE[2])
                            && Dialog.getContinue().click()
            ||
                    Interfaces.isOpen(231) && Interfaces.getComponent(231,4).getText().contains(EXCITED_RESPONSE[3])
                            && Dialog.getContinue().click()
            ||
                    Interfaces.isOpen(231) && Interfaces.getComponent(231,4).getText().contains(EXCITED_RESPONSE[4])
                            && Dialog.getContinue().click()){
                Time.sleepUntil(() -> !Dialog.canContinue(), 500, 500);
        }

            if(Interfaces.isOpen(229) && PRIORITY_PRICE == 3 && !WineorPie){
                PRICE_TO_SELL = Math.round((((1500 * 3) * 0.58) - 1) - PRICE_REDUCTION);
                PRICE_WITH_DISCOUNT = (int) Math.round(PRICE_TO_SELL - (PRICE_TO_SELL * WINE_COUNTER * 0.01));
                Log.fine("Setting price to: " + PRICE_TO_SELL + " Price reduction " + PRICE_WITH_DISCOUNT);
                Keyboard.sendText(Integer.toString(PRICE_WITH_DISCOUNT));
                Keyboard.pressEnter();
                Time.sleep(10, 100);
            }else if(Interfaces.isOpen(229) && PRIORITY_PRICE == 2 && !WineorPie){
                PRICE_TO_SELL = Math.round((((1500 * 2.5) * 0.58) - 1) - PRICE_REDUCTION);
                PRICE_WITH_DISCOUNT = (int) Math.round(PRICE_TO_SELL - (PRICE_TO_SELL * WINE_COUNTER * 0.01));
                Log.fine("Setting price to: " + PRICE_TO_SELL + " Price reduction " + PRICE_WITH_DISCOUNT);
                Keyboard.sendText(Integer.toString(PRICE_WITH_DISCOUNT));
                Keyboard.pressEnter();
                Time.sleep(10, 100);

            }else if(Interfaces.isOpen(229) && PRIORITY_PRICE == 1 && !WineorPie){
                PRICE_TO_SELL = Math.round((((1500 * 2) * 0.58) - 1) - PRICE_REDUCTION);
                PRICE_WITH_DISCOUNT = (int) Math.round(PRICE_TO_SELL - (PRICE_TO_SELL * WINE_COUNTER * 0.01));
                Log.fine("Setting price to: " + PRICE_TO_SELL + " Price reduction " + PRICE_WITH_DISCOUNT);
                Keyboard.sendText(Integer.toString(PRICE_WITH_DISCOUNT));
                Keyboard.pressEnter();
                Time.sleep(10, 100);
            }

        if(Interfaces.isOpen(229) && PRIORITY_PRICE == 3 && WineorPie){
            PRICE_TO_SELL = Math.round((((930 * 3) * 0.58) - 1) - PRICE_REDUCTION);
            PRICE_WITH_DISCOUNT = (int) Math.round(PRICE_TO_SELL - (PRICE_TO_SELL * PIE_COUNTER * 0.01));
            Log.fine("Setting price to: " + PRICE_TO_SELL + " Price reduction " + PRICE_WITH_DISCOUNT);
            Keyboard.sendText(Integer.toString(PRICE_WITH_DISCOUNT));
            Keyboard.pressEnter();
            Time.sleep(10, 100);
        }else if(Interfaces.isOpen(229) && PRIORITY_PRICE == 2 && WineorPie){
            PRICE_TO_SELL = Math.round((((930 * 2.5) * 0.58) - 1) - PRICE_REDUCTION);
            PRICE_WITH_DISCOUNT = (int) Math.round(PRICE_TO_SELL - (PRICE_TO_SELL * PIE_COUNTER * 0.01));
            Log.fine("Setting price to: " + PRICE_TO_SELL + " Price reduction " + PRICE_WITH_DISCOUNT);
            Keyboard.sendText(Integer.toString(PRICE_WITH_DISCOUNT));
            Keyboard.pressEnter();
            Time.sleep(10, 100);

        }else if(Interfaces.isOpen(229) && PRIORITY_PRICE == 1 && WineorPie){
            PRICE_TO_SELL = Math.round((((930 * 2) * 0.58) - 1));
            PRICE_WITH_DISCOUNT = (int) Math.round(PRICE_TO_SELL - (PRICE_TO_SELL * PIE_COUNTER * 0.01));
            Log.fine("Setting price to: " + PRICE_TO_SELL + " Price reduction " + PRICE_WITH_DISCOUNT);
            Keyboard.sendText(Integer.toString(PRICE_WITH_DISCOUNT));
            Keyboard.pressEnter();
            Time.sleep(10, 100);
        }


            if(Interfaces.isOpen(217) && Dialog.canContinue() && Dialog.getContinue().click()){
                Time.sleepUntil(() -> !Dialog.canContinue(),500, 1500);

            }else if(Interfaces.isOpen(231) && Interfaces.getComponent(231, 4).getText().contains("Hmm, okay. I'll buy one.") && Dialog.getContinue().click()
            || Interfaces.isOpen(231) && Interfaces.getComponent(231, 4).getText().contains("That's fair. I'll take one. ") && Dialog.getContinue().click()){
               SOLD_TO = Interfaces.getComponent(231, 1).getModelId();
                Log.fine("Sold to: " + SOLD_TO);
                if(WineorPie){
                    PIE_COUNTER++;
                    Reminder(63, true);
                }else if(!WineorPie){
                    WINE_COUNTER++;
                    Reminder(63, false);
                }
                Time.sleepUntil(() -> !Dialog.canContinue(),500, 1500);
            }

            if(Interfaces.isOpen(219) && Interfaces.getComponent(219,1).getComponent(1).click()){
                Time.sleepUntil(() -> !Interfaces.isOpen(219), 500, 500);
            }

            if(Interfaces.isOpen(231) && Interfaces.getComponent(231, 4).getText() == "that's a bit much for me."
            && Dialog.canContinue() && Dialog.getContinue().click()){
                Time.sleepUntil(() -> !Dialog.canContinue(), 500, 500);
            }


            //IF WINE = FALSE IF PIE = TRUE;
        if(PERMISSION_NPC.isEmpty()) {
            for (int i = 0; i < TALKTO_NPC.length; i++) {
                if (Interfaces.isOpen(231)
                        && Interfaces.getComponent(231, 4).getText().contains(TALKTO_NPC[i])) {
                    PERMISSION_NPC = TALKTO_NPC[i];
                }
            }
        }



    }
    @Override
    public void notify(RenderEvent renderEvent) {
        Graphics g = renderEvent.getSource();

        long runningTime = System.currentTimeMillis() - startTime;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.GREEN);
        g2d.drawString("Runtime: " + formatTime(runningTime), 20, 300);
        g2d.drawString("Pie counter: " + Integer.toString(PIE_COUNTER), 20, 323);



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
}
