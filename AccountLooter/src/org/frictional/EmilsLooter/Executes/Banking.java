package org.frictional.EmilsLooter.Executes;

import org.frictional.EmilsLooter.Enums.Stating;
import org.frictional.EmilsLooter.HandlePrices.PriceFetcher;
import org.frictional.EmilsLooter.Main;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.providers.RSGrandExchangeOffer;
import org.rspeer.ui.Log;

public class Banking {
    private static int THRESHOLD = 1000;
    public static void execute() {

        if(Game.getClient().getGrandExchangeOffers() != null && !GrandExchange.isOpen() &&
                SceneObjects.getNearest(x -> x != null && x.getName().contains("Bank")).interact("Collect")){
            Time.sleepUntil(() -> Interfaces.getComponent(402, 2).getComponent(1) == null, 300, 1000);
        }

        if(Game.getClient().getGrandExchangeOffers() != null && Interfaces.getComponent(402, 2).getComponent(1) != null
        && GrandExchange.collectAll(true)){
            Time.sleepUntil(() -> Game.getClient().getGrandExchangeOffers() == null, 800, 1200);
        }



        if(!Bank.isOpen() && !Players.getLocal().isMoving() && Bank.open()){
            Time.sleepUntil(() -> Bank.isOpen(), 500, 1000);
        }

        if(Bank.isOpen() && Inventory.getCount() > 0 && !Main.finishedWithdrawing && Bank.depositInventory()){
            Time.sleepUntil(() -> Inventory.getCount() == 0, 500, 1000);
        }
        if(Bank.isOpen() && Equipment.getOccupiedSlots().length > 0 && Bank.depositEquipment()){
            Time.sleepUntil(() -> Equipment.getOccupiedSlots().length == 0, 500, 1000);
        }

        if(Bank.isOpen() && Bank.getWithdrawMode() != Bank.WithdrawMode.NOTE
        && Bank.setWithdrawMode(Bank.WithdrawMode.NOTE)){
            Time.sleepUntil(() -> Bank.getWithdrawMode() == Bank.WithdrawMode.NOTE, 500, 1000);
        }
        if(Bank.isOpen() && !Main.finishedWithdrawing || Bank.isOpen() && !Main.interruptedWithdrawing) {

                withdrawItemsOverThreshold();


        }

        if(Main.finishedWithdrawing && Bank.close() || Main.interruptedWithdrawing && Bank.close()){
            Main.updateScriptState(Stating.MULING);
        }

    }

    private static void withdrawItemsOverThreshold() {
             for (Item item : Bank.getItems()) {
                 if (item.isExchangeable() && !item.getName().contains("Member")) {
                     if (item.getStackSize() == 1) {
                         if (PriceFetcher.getPricedItem(item.getId()).getOverallAverage() >= THRESHOLD) {
                             if (Bank.withdrawAll(item.getId())) {
                                 Time.sleep(1000, 1500);
                             }
                         }
                     }


                     if (item.getStackSize() > 1) {
                         if (PriceFetcher.getPricedItem(item.getId()).getOverallAverage()
                                 * item.getStackSize() >= THRESHOLD) {

                         if (Bank.withdrawAll(item.getId())) {
                             Time.sleep(1000, 1500);
                         }
                         }
                     }

                 }
                 if (Inventory.isFull()) {
                     Main.interruptedWithdrawing = true;
                     break;
                 }
             }

            if(Bank.contains(995) && Bank.withdrawAll(995)){
                Time.sleep(300, 500);
            }
            if(!Main.interruptedWithdrawing) {
                Main.finishedWithdrawing = true;
            }


        }





}
