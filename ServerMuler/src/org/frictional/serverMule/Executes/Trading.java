package org.frictional.serverMule.Executes;

import org.frictional.serverMule.Enums.Stating;
import org.frictional.serverMule.Main;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.InterfaceOptions;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.chatter.Chat;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;

public class Trading {
    public static void execute() {
        if(!Trade.isOpen() && Inventory.getCount() == 0){
            TradePlayer(Main.Name);
        }else if(Trade.isOpen()){
            Main.updateScriptState(Stating.HANDLETRADE);
        }

        if(Inventory.getCount() > 0 && !Bank.isOpen() && Bank.open()){
            Time.sleepUntil(() -> Bank.isOpen(), 300, 1000);
        }else if(Inventory.getCount() > 0 && Bank.isOpen() && Bank.depositInventory()){
            Time.sleepUntil(() -> Inventory.getCount() == 0, 500, 800);
        }else if(Inventory.getCount() == 0 && Bank.isOpen() && Bank.close()){
            Time.sleepUntil(() -> Bank.isClosed(),700, 1300);
        }
    }

    private static void TradePlayer(String name) {
        Player p = Players.getNearest(name);
        if(p != null && p.distance(Players.getLocal()) < 2 && p.interact("Trade with")){
            Time.sleepUntil(Trade::isOpen, 500, 1000);
        }
    }
}
