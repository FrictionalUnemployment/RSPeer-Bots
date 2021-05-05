package org.frictional.Executes;

import org.frictional.Enums.State;
import org.frictional.Main;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Dialog;
import org.rspeer.runetek.api.component.Shop;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;

public class Trading {

    private static final Area DRAYNOR_AREA = Area.rectangular(3070, 3265, 3110, 3237);
    private static final Position CENTRE_TILE = new Position(3083, 3250, 0);
    private static final String[] GLORIES = {"Amulet of Glory(6)", "Amulet of Glory(5)", "Amulet of Glory(4)",
            "Amulet of Glory(3)", "Amulet of Glory(2)", "Amulet of Glory(1)"};
    private static final int CURSED_STAFF = 11709;

    public static void execute() {
        if (!DRAYNOR_AREA.contains(Players.getLocal()) && !Players.getLocal().isAnimating()) {
            teleportToDraynor();
        } else if (DRAYNOR_AREA.contains(Players.getLocal()) && !Inventory.contains(CURSED_STAFF) && !Equipment.contains(CURSED_STAFF)) {
            buyStaff();
        } else if (Inventory.contains(CURSED_STAFF) || Equipment.contains(CURSED_STAFF)) {
            Main.updateScriptState(State.SPLASH);
        }
    }

    private static void buyStaff() {
        Npc DIAGO = Npcs.getNearest("Diango");
        if (DIAGO == null) {
            Main.walkTo(CENTRE_TILE);
        } else if (!Shop.isOpen() && DIAGO.interact("Trade")) {
            Time.sleepUntil(() -> Shop.isOpen(), Random.nextInt(1000, 3000));
        } else if (Shop.isOpen() && Shop.buyOne(CURSED_STAFF)) {
            Time.sleepUntil(() -> Inventory.contains(CURSED_STAFF), Random.nextInt(2000, 3000));

        } else if (Shop.isOpen() && Inventory.contains(CURSED_STAFF) && Shop.close()) {
            Main.Sleeping(!Shop.isOpen(), 1000, 2000);
        }


    }

    private static void teleportToDraynor() {
        if (!Dialog.isOpen() && Inventory.getFirst(GLORIES).interact("Rub")) {
            Time.sleepUntil(() -> Dialog.isOpen(), Random.nextInt(300, 800));

        } else if (Dialog.process("Draynor Village")) {
            Time.sleepUntil(() -> !Dialog.isOpen(), Random.nextInt(1000, 3000));
        }
    }
}
