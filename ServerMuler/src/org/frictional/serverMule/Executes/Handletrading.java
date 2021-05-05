package org.frictional.serverMule.Executes;

import org.frictional.serverMule.Enums.Stating;
import org.frictional.serverMule.Main;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.InterfaceOptions;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.Trade;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.ui.Log;

public class Handletrading  {

    public static void execute() {
        if(Main.onStartTrade && Trade.isOpen(false) && !Trade.contains(true, 995)){
            offerMoney(Main.startAmount);
        }else if(Trade.isOpen() && Trade.accept()){
            Time.sleepUntil(() -> !Trade.isOpen(), 1000, 3000);
        }

        if(!Trade.isOpen()){
            Main.updateScriptState(Stating.LOGOUT);
            Main.finishedTrading = true;
        }

    }

    private static void offerMoney(int startAmount) {
        if(!EnterInput.isOpen() && Trade.offer(995, a -> a.contains("Offer-X"))){
            Time.sleepUntil(()->EnterInput.isOpen(), 300, 500);
        }else if(EnterInput.isOpen() && EnterInput.initiate(startAmount)){
            Time.sleepUntil(()->!EnterInput.isOpen(), 300, 1000);
        }
    }


}
