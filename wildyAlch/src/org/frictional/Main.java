package org.frictional;

import com.beust.jcommander.JCommander;
import org.frictional.Enums.Target;
import org.frictional.Executes.Alching;
import org.frictional.Executes.Banking;
import org.frictional.Executes.Walking;
import org.frictional.HandleGE.RequiredItem;
import org.frictional.exAPI.ExWilderness;
import org.frictional.exAPI.ExWorldHopper;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.event.listeners.ChatMessageListener;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.ChatMessageEvent;
import org.rspeer.runetek.event.types.ChatMessageType;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;
import org.rspeer.ui.Log;

import java.awt.*;
import java.util.HashSet;


@ScriptMeta(developer = "Frictional", name = "wildyAlch", desc = "Automated baby")
public class Main extends Script implements ChatMessageListener {
    private static org.frictional.Enums.State currentState = org.frictional.Enums.State.STARTING;
    private static org.frictional.Enums.State previousState;
    private static boolean onStartCalled = false;
    private final Args args = new Args();

    private static final int MIN_WALK_WAIT = 700;
    private static final int MAX_WALK_WAIT = 2000;

    private static final Area VARROCK_AREA = Area.rectangular(3071, 3518, 3295, 3334);
    public static final Area ALCHING_AREA = Area.rectangular(3353, 3900, 3389, 3869);
    private static Target currentTarget;

    public static String MULE_NAME;
    public static boolean STARTMULER;
    public static int startAmount;
    public static int keepMoney;
    public static String alchingName;
    public static int alchingAmount;
    public static boolean ANTI_SPAM = false;
    public static HashSet<String> whitelist = new HashSet<String>();

    @Override
    public void onStart() {
        Log.fine(getArgs());
        final String userArgs = getArgs(); // Create a String field to store the user arguments.

        JCommander.newBuilder()
                .addObject(args) // Pass the instance of the args class to JCommander.
                .build()
                .parse(userArgs.split("[.]")); // Pass the String of user arguments to JCommander and split them by spaces so they can be parsed.

        STARTMULER = args.startMulingBool;
        MULE_NAME = args.muleName;
        alchingAmount = args.alchAmount;
        alchingName = args.alchName;
        startAmount = args.startMuleMoney;
        keepMoney = args.keepMuleMoney;
        Log.fine("Mule Name: " + MULE_NAME);
        Log.fine("Start muling: " + STARTMULER);
        super.onStart();
    }

    @Override
    public void onStop() {
    //    Alching.RUN_THREAD = false;
        super.onStop();
    }

    @Override
    public int loop() {

        if (Worlds.getLoaded().length > 0) {
            if (Players.getLoaded().length > 1 && ExWilderness.canAnyPlayerAttackLocal()
                    && Players.getNearest(player -> !Main.whitelist.contains(player.getName())) != null
                    || ExWilderness.canNpcAttackLocal()) {

                ExWorldHopper.randomInstaHopInPureP2p();

            }
        }

        if (onStartCalled && !ExWilderness.canNpcAttackLocal() && !ExWilderness.canAnyPlayerAttackLocal()) {
            currentState.execute();

        } else if (Game.isLoggedIn() && !Game.isOnCredentialsScreen() && !onStartCalled && Worlds.getLoaded().length > 0
                && !Players.getLocal().getName().isEmpty()) {
            updateTarget(Target.getBestTarget());
            whitelist.add(Players.getLocal().getName());
            Log.fine(whitelist);
            Banking.onStart();
            //Alching.onStart();
            Log.fine("Done with startup");
            onStartCalled = true;
        }


        if (Time.sleepWhile(() -> ExWilderness.canAnyPlayerAttackLocal() || ExWilderness.canNpcAttackLocal(),
                Random.nextInt(500, 900), 200)) {
            return 0;
        }
        return Random.nextInt(300, 700);
    }






    public static boolean walkTo(Position tile) {
        if (Movement.walkTo(tile)) {
            int mainSleep = Movement.isRunEnabled() ? Random.low(MIN_WALK_WAIT, MAX_WALK_WAIT) : Random.low(MIN_WALK_WAIT * 2, MAX_WALK_WAIT * 2);

            if (!Movement.isRunEnabled()) {
                if ((Players.getLocal().isHealthBarVisible() && Movement.getRunEnergy() > 5) || Movement.getRunEnergy() > Random.nextInt(40, 80))
                    Movement.toggleRun(true);
            }
            //Log.info("Total Walk Sleep: " + mainSleep);

            //Initial sleep is used to allow for player to start moving
            int initialSleep = Players.getLocal().isMoving() ? 0 : MIN_WALK_WAIT;
            Time.sleep(initialSleep);
            mainSleep -= initialSleep;

            if (Movement.getDestination() != null && Movement.getDestination().equals(tile))
                Time.sleepUntil(() -> Players.getLocal().getPosition().equals(tile), Random.nextInt(8000, 12000));
            else
                Time.sleepUntil(() -> (!Players.getLocal().isMoving() && Players.getLocal().getAnimation() == -1)
                        || Players.getLocal().getPosition().equals(tile)
                        || (Players.getLocal().isHealthBarVisible() && !Movement.isRunEnabled() && Movement.getRunEnergy() > 5), mainSleep);
            return true;
        }
        return false;
    }


    public static boolean isInVarrock() {
        return VARROCK_AREA.contains(Players.getLocal());
    }

    public static boolean isInAlchingArea() {
        return ALCHING_AREA.contains(Players.getLocal());
    }

    public static int getCount(Item... items) {
        int count = 0;
        if (items == null || items.length == 0)
            return count;
        for (Item item : items) {
            if (item.isStackable())
                count = count + item.getStackSize();
            else
                count = count + 1;
        }
        return count;
    }


    public static boolean hasItems(RequiredItem[] requiredItems) {
        for (RequiredItem requiredItem : requiredItems) {
            String itemName = requiredItem.getName();
            int reqAmount = requiredItem.getAmountRequired();

            if (itemName.endsWith(")")) {
                itemName = itemName.split("\\(")[0];
            }

            String finalItemName = itemName;
            Item[] invent = Inventory.getItems(x -> x.getName().contains(finalItemName)
                    && !x.isNoted());

            if (invent.length > 0) {
                continue;
            }

            Item[] equipped = Equipment.getItems(x -> x.getName().contains(finalItemName));
            if (equipped.length > 0) {
                if (getCount(equipped) >= reqAmount)
                    continue;
            }
            return false;
        }
        return true;
    }

    public static org.frictional.Enums.State getBestState() {

        if (STARTMULER) {
            return org.frictional.Enums.State.TRADEMULER;
        }
        if (getLevelEvent() == 1) {
            return org.frictional.Enums.State.BANKING;
        }

        Target bestTarget = Target.getBestTarget();
        if (getLevelEvent() < 55 && bestTarget != currentTarget) {
            updateTarget(Target.getBestTarget());
            return org.frictional.Enums.State.SPLASH;
        } else if (getLevelEvent() < 55 && bestTarget == currentTarget) {
            return org.frictional.Enums.State.SPLASH;
        }

        if (getLevelEvent() >= 55) {
            return org.frictional.Enums.State.ALCHING;
        }


        return null;
    }

    public static Target getCurrentTarget() {
        return currentTarget;
    }

    private static void updateTarget(Target inTarget) {
        currentTarget = inTarget;
    }

    public static void Sleeping(boolean condition, int min, int max) {
        Time.sleepUntil(() -> condition, min, max);
    }

    public static void updateScriptState(org.frictional.Enums.State inState) {
        if (inState != null) {
            previousState = currentState;
            //onStartCalled = false;
            currentState = inState;
        }
    }

    public static int getLevelEvent() {
        return Skills.getLevel(Skill.MAGIC);
    }

    public static void addName(String e) {
        Log.fine("Checking hashset: ", e);
        whitelist.add(e);
    }

    @Override
    public void notify(ChatMessageEvent chatMessageEvent) {
        String Message = chatMessageEvent.getMessage();
        if (chatMessageEvent.getType() == ChatMessageType.SERVER &&
        getLevelEvent() <= 55) {
            if (Message.contains("Congratulations, ")) {
                if(getLevelEvent() == 55){
                    Banking.onStart();
                }
                updateScriptState(getBestState());

            }
        }

        if (chatMessageEvent.getType() == ChatMessageType.TRADE_INFO) {
            if (Message.contains("Accepted ")) {
                STARTMULER = false;
                updateScriptState(getBestState());
            }

        }

    }
}

