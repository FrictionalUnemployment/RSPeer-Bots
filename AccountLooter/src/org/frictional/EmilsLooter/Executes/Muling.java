package org.frictional.EmilsLooter.Executes;

import org.frictional.EmilsLooter.Main;
import org.frictional.EmilsLooter.Muler.Client;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

public class Muling {


    public static boolean sent;
    private static StopWatch muleTimer;
    public static void execute() {
        if (Players.getNearest(player -> player.getName().contains(Main.MULE_NAME)) != null
                && !Trade.isOpen() && Players.getNearest(Main.MULE_NAME).interact("Trade with")) {
            Time.sleepUntil(() -> Trade.isOpen(), 500, 1000);
        } else if (Trade.isOpen() && Trade.isOpen(false) && Inventory.getCount() > 0) {
            tradeOverItems();
        } else if (Trade.isOpen() && Trade.accept() && Inventory.getCount() == 0) {
            Time.sleepUntil(() -> Trade.isOpen(), 1000, 2000);
        } else if (!Main.ANTI_SPAM) {
            muleTimer = StopWatch.start();

                messageMuler(Players.getLocal().getName(), Game.getClient().getCurrentWorld(), "TRADEOVERITEMS");

        } else if (muleTimer.getElapsed().getSeconds() > 60) {
            Main.ANTI_SPAM = false;
            muleTimer.reset();
        }

        if(Inventory.getCount() == 0 && !Trade.isOpen()){
            Main.interruptedWithdrawing = false;
        }

        if(Main.interruptedWithdrawing) {
            Main.updateScriptState(Main.getBestState());
        }else if(Main.finishedWithdrawing && Inventory.getCount() == 0 && !Trade.isOpen()){

        }

    }

    private static void messageMuler(String name, int currentWorld, String type) {
        Client client = new Client("0.0.0.0", 45342);
        client.connect();
        //client.write("Hi");
        sent = true;
        Log.fine("Sending our data to the Muler");
        while (sent) {
            if (Game.isLoggedIn()) {
                client.write(name + " " + currentWorld + " " + type);
            }
            sent = false;
            Main.ANTI_SPAM = true;
        }
        Time.sleep(1000, 5000);
        client.close();
    }

    private static void tradeOverItems() {
        for(Item item: Inventory.getItems()){
            if(item != null){
                if(item.isExchangeable() || item.getId() == 995){
                    if(Trade.offer(item.getId(), a -> a.contains("Offer-All"))){

                    }
                }
            }
        }
    }

}
