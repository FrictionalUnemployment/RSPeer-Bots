package org.frictional.Executes;

import org.frictional.Enums.State;
import org.frictional.Main;
import org.frictional.exAPI.ExWilderness;
import org.frictional.exAPI.ExWorldHopper;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.Worlds;
import org.rspeer.runetek.api.component.tab.*;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.WebWalker;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.ui.Log;

import java.util.Random;


public class Alching {

    private static final Position[] WALKABLE_TILES = {
            new Position(3378, 3888, 0),
            new Position(3378, 3889, 0),
            new Position(3377, 3889, 0),
            new Position(3377, 3890, 0),
            new Position(3378, 3890, 0),
            new Position(3379, 3890, 0),
            new Position(3379, 3889, 0),
            new Position(3379, 3891, 0),
            new Position(3380, 3891, 0),
            new Position(3381, 3891, 0),
            new Position(3382, 3891, 0),
            new Position(3383, 3891, 0),
            new Position(3384, 3891, 0),
            new Position(3384, 3892, 0),
            new Position(3385, 3892, 0),
            new Position(3385, 3893, 0),
            new Position(3384, 3893, 0),
            new Position(3384, 3894, 0),
            new Position(3383, 3894, 0),
            new Position(3382, 3894, 0),
            new Position(3381, 3894, 0),
            new Position(3380, 3894, 0),
            new Position(3379, 3894, 0),
            new Position(3378, 3894, 0),
            new Position(3378, 3895, 0),
            new Position(3377, 3896, 0),
            new Position(3376, 3897, 0),
            new Position(3376, 3898, 0),
            new Position(3376, 3899, 0),
            new Position(3375, 3899, 0),
            new Position(3375, 3900, 0),
            new Position(3374, 3899, 0),
            new Position(3374, 3898, 0),
            new Position(3373, 3898, 0),
            new Position(3372, 3898, 0),
            new Position(3371, 3898, 0),
            new Position(3370, 3898, 0),
            new Position(3369, 3898, 0),
            new Position(3368, 3898, 0),
            new Position(3367, 3898, 0),
            new Position(3368, 3899, 0),
            new Position(3368, 3897, 0),
            new Position(3369, 3897, 0),
            new Position(3370, 3897, 0),
            new Position(3370, 3896, 0),
            new Position(3370, 3895, 0),
            new Position(3369, 3895, 0),
            new Position(3369, 3894, 0),
            new Position(3368, 3894, 0),
            new Position(3369, 3893, 0),
            new Position(3370, 3893, 0),
            new Position(3370, 3894, 0),
            new Position(3371, 3894, 0),
            new Position(3372, 3894, 0),
            new Position(3372, 3893, 0),
            new Position(3372, 3892, 0),
            new Position(3371, 3892, 0),
            new Position(3370, 3892, 0),
            new Position(3369, 3892, 0),
            new Position(3368, 3892, 0),
            new Position(3367, 3892, 0),
            new Position(3367, 3891, 0),
            new Position(3367, 3890, 0),
            new Position(3368, 3890, 0),
            new Position(3368, 3891, 0),
            new Position(3369, 3891, 0),
            new Position(3369, 3890, 0),
            new Position(3370, 3890, 0),
            new Position(3370, 3891, 0),
            new Position(3371, 3891, 0),
            new Position(3371, 3890, 0),
            new Position(3372, 3890, 0),
            new Position(3372, 3890, 0),
            new Position(3372, 3891, 0),
            new Position(3373, 3891, 0),
            new Position(3373, 3890, 0),
            new Position(3374, 3890, 0),
            new Position(3374, 3890, 0),
            new Position(3374, 3891, 0),
            new Position(3375, 3891, 0),
            new Position(3375, 3890, 0),
            new Position(3376, 3891, 0),
            new Position(3376, 3890, 0),
            new Position(3378, 3888, 0),
            new Position(3384, 3887, 0),
            new Position(3384, 3886, 0),
            new Position(3385, 3886, 0),
            new Position(3385, 3885, 0),
            new Position(3384, 3885, 0),
            new Position(3383, 3885, 0),
            new Position(3383, 3892, 0),
            new Position(3383, 3893, 0),
            new Position(3382, 3893, 0),
            new Position(3382, 3892, 0),
            new Position(3380, 3892, 0),
            new Position(3381, 3892, 0),
            new Position(3381, 3893, 0),
            new Position(3380, 3893, 0),
            new Position(3379, 3893, 0),
            new Position(3379, 3892, 0),
            new Position(3378, 3892, 0),
            new Position(3378, 3891, 0),
            new Position(3377, 3891, 0),
            new Position(3376, 3892, 0),
            new Position(3377, 3892, 0),
            new Position(3377, 3892, 0),
            new Position(3377, 3893, 0),
            new Position(3378, 3893, 0),
            new Position(3377, 3894, 0),
            new Position(3377, 3895, 0),
            new Position(3376, 3895, 0),
            new Position(3376, 3896, 0),
            new Position(3375, 3896, 0),
            new Position(3375, 3897, 0),
            new Position(3375, 3898, 0),
            new Position(3374, 3896, 0),
            new Position(3373, 3897, 0),
            new Position(3374, 3897, 0),
            new Position(3372, 3897, 0),
            new Position(3371, 3897, 0),
            new Position(3371, 3896, 0),
            new Position(3372, 3896, 0),
            new Position(3373, 3896, 0),
            new Position(3372, 3895, 0),
            new Position(3371, 3895, 0),
            new Position(3373, 3895, 0),
            new Position(3376, 3893, 0),
            new Position(3376, 3894, 0),
            new Position(3375, 3895, 0),
            new Position(3374, 3895, 0)
    };

    public static final String[] ALCH_ITEMS = {Main.alchingName, "Ring of wealth (",
    "Ring of dueling("};
    private static Random rand = new Random();
    public static void onStart() {
        Log.fine("Anti-PK and Anti-NPC listener activated");
      /*  if (!RUN_THREAD) {
            Alching.AntiHopper object = new Alching.AntiHopper();
            RUN_THREAD = true;
            object.start(); */


    }


    // 14829
//Obelisk 14831, 14826
    //position 3118, 3749
    /*public static class AntiHopper extends Thread {
        public void run() {
            while (RUN_THREAD) {
                // Log.fine("We will make sure we dont die");
                if (Worlds.getLoaded().length > 0 && ExWilderness.isInWilderness()) {
                    if (Players.getLoaded().length > 1 && ExWilderness.canAnyPlayerAttackLocal()
                            && Players.getNearest(player -> !Main.whitelist.contains(player.getName())) != null
                            || ExWilderness.canNpcAttackLocal()) {
                        ExWorldHopper.randomInstaHopInPureP2p();

                    }
                }
            }
            RUN_THREAD = false;
        }
    }*/

    //public static boolean RUN_THREAD = false;

    public static void execute() {

        Player local = Players.getLocal();
        if (Main.isInAlchingArea() && Inventory.contains(ALCH_ITEMS) && Magic.canCast(Spell.Modern.HIGH_LEVEL_ALCHEMY)) {
            alchItems(local);

        } else if (Main.isInAlchingArea() && Inventory.contains(ALCH_ITEMS) && !Magic.canCast(Spell.Modern.HIGH_LEVEL_ALCHEMY)) {
            inTile(local, WALKABLE_TILES, rand);

        } else if (!Main.isInAlchingArea() && !Inventory.contains(ALCH_ITEMS) && !ExWilderness.isInWilderness()) {
            Main.updateScriptState(State.BANKING);
        } else if (!Inventory.contains(ALCH_ITEMS) && ExWilderness.isInWilderness() || Inventory.contains(ALCH_ITEMS) && !Main.isInAlchingArea()) {
            Main.updateScriptState(State.WALKING);
        }

    }

    private static void alchItems(Player local) {
        if (!Magic.isSpellSelected() && Magic.cast(Spell.Modern.HIGH_LEVEL_ALCHEMY)) {
            Main.Sleeping(local.isAnimating(), 200, 400);
        } else if (!local.isAnimating() && Magic.isSpellSelected() && Inventory.getFirst(ALCH_ITEMS).interact("Cast")) {
            Main.Sleeping(local.isAnimating(), 100, 300);
        }

    }

    private static void inTile(Player local, Position[] w, Random rand) {
        if (!Tabs.isOpen(Tab.MAGIC) && Tabs.open(Tab.MAGIC)) {
            Main.Sleeping(Tabs.isOpen(Tab.MAGIC), 300, 800);
        }

        int random = rand.nextInt(w.length);
        if (!local.isMoving() && Movement.walkTo(w[random])) {
            Main.Sleeping(Players.getLocal().isMoving(), 1500, 3000);
        }

    }
}
