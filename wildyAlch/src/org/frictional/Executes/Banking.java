package org.frictional.Executes;

import org.frictional.Enums.State;
import org.frictional.HandleGE.ItemBuying;
import org.frictional.HandleGE.RequiredItem;
import org.frictional.Main;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.api.commons.BankLocation;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Magic;
import org.rspeer.runetek.api.component.tab.Spell;
import org.rspeer.runetek.api.input.Keyboard;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

import java.awt.event.KeyEvent;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;

public class Banking {

    private static RequiredItem[] itemsRequired;
    private static RequiredItem[] itemsToBuy;
    private static boolean initialBankCheckComplete;
    private static boolean finalBankCheckComplete;
    private static ItemBuying itemBuyer = null;

    private static final RequiredItem[] REQUIRED_ITEMS = {
            new RequiredItem("Staff of Air", 1),
            new RequiredItem("Fire rune", 1950),
            new RequiredItem("Air rune", 3000),
            new RequiredItem("Mind rune", 4000),
            new RequiredItem("Law rune", 3135),
            new RequiredItem("Amulet of glory(6)", 1),
            new RequiredItem("Water rune", 715),
            new RequiredItem("Earth rune", 315),
            new RequiredItem("Iron platebody", 1),
            new RequiredItem("Iron platelegs", 1),
            new RequiredItem("Iron full helm", 1),
            new RequiredItem("Iron kiteshield", 1),
            new RequiredItem("Varrock teleport", 10)

    };

    private static final RequiredItem[] BUYING_ALCH_ITEMS = {
            new RequiredItem(Main.alchingName, Main.alchingAmount),
            new RequiredItem("Ring of wealth (5)", 1),
            new RequiredItem("Ring of dueling(8)", 1)
    };

    private Banking() {

    }

    public static void onStart() {
        itemBuyer = new ItemBuying();

        if (Main.getBestState() != State.ALCHING) {
            itemsRequired = REQUIRED_ITEMS;
        } else {
            itemsRequired = BUYING_ALCH_ITEMS;
        }
        itemsToBuy = null;
        initialBankCheckComplete = false;
        finalBankCheckComplete = false;
    }

    public static void execute() {


        if (Main.hasItems(itemsRequired) && finalBankCheckComplete) {
            //Got all the items, close the bank and begin hunting
            if (Bank.isOpen()) {
                if (Bank.close())
                    Time.sleepUntil(Bank::isClosed, Random.nextInt(1500, 2500));
            } else {
                //       Log.fine("Got all items, lets go!");
                //      Main.updateScriptState(Main.getBestHuntingState());
            }
            return;
        }

        if (itemBuyer.isFinishedBuying() && Main.hasItems(itemsRequired) && !Bank.isOpen()
                && !GrandExchange.isOpen() ||
                Main.hasItems(itemsRequired)  && !Bank.isOpen() && !GrandExchange.isOpen()) {
            if (Main.getBestState() == State.BANKING) {
                Main.updateScriptState(State.BUY_STAFF);
                return;
            } else if (Main.getBestState() == State.ALCHING) {
                Main.updateScriptState(State.WALKING);
                return;
            }
        } else if (itemsToBuy != null && !Main.hasItems(itemsRequired) && !itemBuyer.isFinishedBuying()) {
            itemBuyer.BuyItems(itemsToBuy);
            return;
        }

        if (!Bank.isOpen()) {
            openNearestBank();
            return;
        }

        if (isGeBuyWindowOpen()) {
            closeGeWindow();
            return;
        }
        if (!initialBankCheckComplete) {
            if (Inventory.getCount() > 0) {
                //Deposit all to avoid walking across RS with your cash stack...
                if (Bank.depositInventory())
                    Time.sleepUntil(() -> Inventory.getCount() == 0, Random.nextInt(1500, 2000));
                return;
            }
            //Find all item we need to buy
            itemsToBuy = ItemBuying.getAllItemsToBuy(itemsRequired);
            initialBankCheckComplete = true;
            if (itemsToBuy.length > 0) {
                Log.fine("Need to buy items");
                RequiredItem.logAll(itemsToBuy);
                return;
            } else
                finalBankCheckComplete = true;

        }

        withdrawRequiredItems();
    }


    private static void withdrawRequiredItems() {
        //Deposit all items once, then withdraw the ones we want.
        if (!finalBankCheckComplete) {
            if (Inventory.getCount() == 0) {
                finalBankCheckComplete = true;
            } else {
                if (Bank.depositInventory())
                    Time.sleepUntil(() -> Inventory.getCount() == 0, Random.nextInt(1500, 3000));
            }
            return;
        }
        for (RequiredItem requiredItem : itemsRequired) {

                Predicate<Item> pred = x -> x.getName().equalsIgnoreCase(requiredItem.getName());


                Item[] inventItem = Inventory.getItems(pred);
                int withdrawnAmount = Main.getCount(inventItem);

                if (inventItem.length > 0) {
                    if (inventItem[0].isNoted()) {
                        if (Bank.depositAll(inventItem[0].getName()))
                            Time.sleepUntil(() -> Main.getCount(inventItem) != withdrawnAmount, Random.nextInt(1500, 2500));
                        return;
                    }
                }

                if (withdrawnAmount == requiredItem.getAmountRequired())
                    continue;


                final BooleanSupplier correctAmountInInvent = () -> Main.getCount(Inventory.getItems(pred)) == requiredItem.getAmountRequired();
                if(requiredItem.getAmountRequired() > 1){
                    if(Bank.setWithdrawMode(Bank.WithdrawMode.NOTE)){
                        Time.sleep(Random.nextInt(500, 1000));

                    }
                }else if(requiredItem.getAmountRequired() == 1){
                    if(Bank.setWithdrawMode(Bank.WithdrawMode.ITEM)){
                        Time.sleep(Random.nextInt(500, 1000));

                    }
                }

                //Log.fine(Bank.withdraw(Bank.getItems(x -> x.getName().contains(requiredItem.getName().split(("\\("))[0]))[0].getName(), requiredItem.getAmountRequired()));
                if (withdrawnAmount == 0) {
                    if (!requiredItem.getName().contains(")") && Bank.withdraw(requiredItem.getName(), requiredItem.getAmountRequired())
                    || requiredItem.getName().contains(")") && Bank.withdraw(Bank.getItems(x -> x.getName().contains(requiredItem.getName().split(("\\("))[0]))[0].getName(), requiredItem.getAmountRequired()))
                        Time.sleepUntil(correctAmountInInvent, Random.nextInt(1500, 3000));
                    continue;
                }

                if (withdrawnAmount < requiredItem.getAmountRequired()) {
                    if (Bank.withdraw(requiredItem.getName(), requiredItem.getAmountRequired() - withdrawnAmount))
                        Time.sleepUntil(correctAmountInInvent, Random.nextInt(1500, 3000));
                    continue;
                }

                if (Bank.deposit(requiredItem.getName(), -(requiredItem.getAmountRequired() - withdrawnAmount)))
                    Time.sleepUntil(correctAmountInInvent, Random.nextInt(1500, 3000));


        }
    }

    private static boolean isGeBuyWindowOpen() {
        InterfaceComponent Window = Interfaces.getComponent(162, 45);
        if (Window == null) return false;
        return Window.isVisible() && Window.getText().contains("What would you like to buy?");
    }

    private static void closeGeWindow() {
        if (!isGeBuyWindowOpen())
            return;
        Keyboard.pressEventKey(KeyEvent.VK_ESCAPE);
        Time.sleepUntil(() -> !isGeBuyWindowOpen(), Random.nextInt(1500, 3000));
    }

    private static void openNearestBank() {
        Position nearestBank = BankLocation.getNearest().getPosition();
        if (Players.getLocal().distance(nearestBank) < 15) {
            if (GrandExchange.isOpen()) {
                closeGrandExchange();
                return;
            }
            if (Bank.open())
                Time.sleepUntil(Bank::isOpen, Random.nextInt(5000, 10000));
            return;
        }
        if (Inventory.contains("Varrock teleport") && !Main.isInVarrock()
                && BankLocation.getNearest().getPosition().distance(Players.getLocal()) > 30) {
            teleportToBank();
            return;
        }
        Main.walkTo(nearestBank);
    }

    private static void teleportToBank() {
        Position startPosition = Players.getLocal().getPosition();
        Item varrockTele = Inventory.getFirst("Varrock teleport");
        final BooleanSupplier teleportSuccessful = () -> startPosition.distance(Players.getLocal()) > 30;
        if (varrockTele != null) {
            if (varrockTele.interact("Break"))
                Time.sleepUntil(teleportSuccessful, Random.nextInt(10000, 13000));
            return;
        }
        Item camelotTele = Inventory.getFirst("Camelot teleport");
        if (camelotTele != null) {
            if (camelotTele.interact("Break"))
                Time.sleepUntil(teleportSuccessful, Random.nextInt(10000, 13000));
            return;
        }
        Item faladorTele = Inventory.getFirst("Falador teleport");
        if (faladorTele != null) {
            if (faladorTele.interact("Break"))
                Time.sleepUntil(teleportSuccessful, Random.nextInt(10000, 13000));
            return;
        }
        if (Magic.interact(Spell.Modern.HOME_TELEPORT, "Cast"))
            Time.sleepUntil(teleportSuccessful, Random.nextInt(14000, 17000));
    }

    private static void closeGrandExchange() {
        InterfaceComponent closeBtn = Interfaces.getComponent(465, 2).getComponent(11);
        if (closeBtn == null) return;
        if (closeBtn.interact("Close"))
            Time.sleepUntil(() -> !GrandExchange.isOpen(), Random.nextInt(1500, 3000));
    }

}
