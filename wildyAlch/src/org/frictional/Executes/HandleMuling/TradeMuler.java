package org.frictional.Executes.HandleMuling;

import org.frictional.Main;
import org.frictional.Muler.Client;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.StopWatch;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

public class TradeMuler {

    public static boolean sent;
    private static StopWatch muleTimer;

    public static void execute() {
        if (Players.getNearest(player -> player.getName().contains(Main.MULE_NAME)) != null
                && !Trade.isOpen() && Players.getNearest(Main.MULE_NAME).interact("Trade with")) {
            Main.Sleeping(Trade.isOpen(), 500, 1000);
        } else if (Trade.isOpen() && !Main.STARTMULER && Trade.isOpen(false) && !Trade.contains(true, 995)) {
            tradeOverMoney(getMoneyToTrade(Main.keepMoney));
        } else if (Trade.isOpen() && Trade.accept()) {
            Main.Sleeping(!Trade.isOpen(), 1000, 3000);

        } else if (!Main.ANTI_SPAM) {
            muleTimer = StopWatch.start();
            if (Main.STARTMULER) {
                messageMuler(Players.getLocal().getName(), Game.getClient().getCurrentWorld(), "STARTMULER");
            } else {
                messageMuler(Players.getLocal().getName(), Game.getClient().getCurrentWorld(), "TRADEOVERITEMS");
            }
        } else if (muleTimer.getElapsed().getSeconds() > 60) {
            Main.ANTI_SPAM = false;
            muleTimer.reset();
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

    private static void tradeOverMoney(int moneyToTrade) {
        if (!EnterInput.isOpen() && Trade.offer(995, a -> a.contains("Offer-X"))) {
            Time.sleepUntil(() -> EnterInput.isOpen(), 300, 500);
        } else if (EnterInput.isOpen() && EnterInput.initiate(moneyToTrade)) {
            Time.sleepUntil(() -> !EnterInput.isOpen(), 300, 1000);
        }
    }

    public static int getMoneyToTrade(int keepAmount) {
        if (Inventory.contains(995) && Inventory.getFirst(995).getStackSize() > keepAmount) {
            return Inventory.getFirst(995).getStackSize() - keepAmount;
        } else {
            return 0;
        }
    }
}
