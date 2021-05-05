package org.frictional.Executes;

import org.frictional.Enums.State;
import org.frictional.Main;
import org.frictional.exAPI.ExWilderness;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.component.Item;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.ui.Log;

public class Walking {

    private final static Area OBELISK_AREA_50 = Area.rectangular(3304, 3919, 3310, 3913);
    public final static Area FEROX_ENCLAVE = Area.rectangular(3125, 3646, 3160, 3627);

    public static void execute() {
        SceneObject[] obelisks = SceneObjects.getLoaded(x -> x != null && x.getName().contains("Obelisk"));

        if (GrandExchange.isOpen()) {
            closeGrandExchange();
        }


        if (ExWilderness.isInWilderness() && Inventory.contains(Alching.ALCH_ITEMS) && ExWilderness.getLevel() < 45 && obelisks.length > 0
                || ExWilderness.isInWilderness() && ExWilderness.getLevel() > 30 && !Inventory.contains(Alching.ALCH_ITEMS) && obelisks.length > 0
                || Inventory.contains(Alching.ALCH_ITEMS) && FEROX_ENCLAVE.contains(Players.getLocal())) {
            operateObelisk(obelisks);
        } else if (ExWilderness.isInWilderness() && Inventory.contains(Alching.ALCH_ITEMS) && !Main.isInAlchingArea() && ExWilderness.getLevel() >= 45) {
            Main.walkTo(Main.ALCHING_AREA.getCenter());
        } else if (ExWilderness.isInWilderness() && !Inventory.contains(Alching.ALCH_ITEMS) && !OBELISK_AREA_50.contains(Players.getLocal()) && ExWilderness.getLevel() >= 45) {
            Main.walkTo(OBELISK_AREA_50.getCenter().getPosition());
        } else if (ExWilderness.isInWilderness() && ExWilderness.getLevel() <= 30 && !Inventory.contains(Alching.ALCH_ITEMS)) {
            teleporting("Ring of wealth", "Grand Exchange");
        } else if (!ExWilderness.isInWilderness() && Inventory.contains(Alching.ALCH_ITEMS) && !FEROX_ENCLAVE.contains(Players.getLocal())) {
            teleporting("Ring of dueling", "Ferox Enclave.");
        }

        if (Main.isInAlchingArea() && Inventory.contains(Alching.ALCH_ITEMS)) {
            Main.updateScriptState(State.ALCHING);
        }

        if (Main.isInVarrock() && !Inventory.contains(Alching.ALCH_ITEMS)) {
            Banking.onStart();
            Main.updateScriptState(State.BANKING);
        }

    }

    private static void closeGrandExchange() {
        InterfaceComponent closeBtn = Interfaces.getComponent(465, 2).getComponent(11);
        if (closeBtn == null) return;
        if (closeBtn.interact("Close"))
            Time.sleepUntil(() -> !GrandExchange.isOpen(), 3000);

        if (!GrandExchange.isOpen() && EnterInput.initiate("x")) {
            Time.sleep(1000, 2000);
        }
    }

    private static void teleporting(String n, String t) {
        Item[] r = Inventory.getItems(x -> x != null && x.getName().contains(n));
        if (r.length > 0) {
            if (!Dialog.isOpen() && r[0].interact("Rub")) {
                Main.Sleeping(Dialog.isOpen(), 300, 500);
            } else if (Dialog.isOpen() && !Players.getLocal().isAnimating() && Dialog.process(t)) {
                Main.Sleeping(Players.getLocal().isAnimating(), 1000, 2000);
            }
        }
    }

    private static void operateObelisk(SceneObject[] obelisks) {
        if (obelisks.length > 0) {
            Area OBELISK_AREA = Area.rectangular(obelisks[0].getX() + 1, obelisks[0].getY() + 2,
                    obelisks[3].getX() - 1 , obelisks[3].getY() - 2);
            if (obelisks[0].distance() < 5 && obelisks[0].containsAction("Activate") && obelisks[0].interact("Activate")) {
                Main.Sleeping(!obelisks[0].containsAction("Activate"), 500, 600);
            } else if (!OBELISK_AREA.contains(Players.getLocal()) && !obelisks[0].containsAction("Activate")
                    && Movement.walkTo(OBELISK_AREA.getCenter())) {
                Main.Sleeping(OBELISK_AREA.contains(Players.getLocal()), 500, 100);
            } else if (obelisks[0].distance() > 10 && !Players.getLocal().isMoving() && FEROX_ENCLAVE.contains(Players.getLocal()) && Movement.walkTo(obelisks[0].getPosition())) {
                Main.Sleeping(Players.getLocal().isMoving(), 300, 1000);
            }
        }

    }
}
