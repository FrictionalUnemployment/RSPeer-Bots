package org.frictional.Executes;

import org.frictional.Enums.Target;
import org.frictional.Main;
import org.rspeer.runetek.adapter.component.InterfaceComponent;
import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.EnterInput;
import org.rspeer.runetek.api.component.GrandExchange;
import org.rspeer.runetek.api.component.Interfaces;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Magic;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.Players;

public class Splashing {
    private static final Area RIMMINGTON_AREA = Area.rectangular(3023, 3241, 3043, 3223);
    private static final int[] EQUIPMENT = {1115, 1067, 11709, 1191, 1153};
    private static final Position SEAGULL_POS = new Position(3027, 3233, 0);

    public static void execute() {
        Player local = Players.getLocal();
        Target target = Main.getCurrentTarget();
        //Log.fine(Target.getBestTarget());
        if (GrandExchange.isOpen()) {
            closeGrandExchange();
        }
        if (Main.getLevelEvent() < 25) {
            if (Inventory.contains(EQUIPMENT)) {
                wearEquipment(EQUIPMENT);
            }

            if (!Inventory.contains(EQUIPMENT) && !target.getSpell().isAutoCasted()) {
                Main.Sleeping(target.getSpell().isAutoCasted(), 500, 800);
                selectSpell(target);
            }


            if (target.inReach(local)
                    && !local.isAnimating() && Npcs.getNearest(target.getNames()).getTargetIndex() == -1) {
                attackTarget(target, local);
            } else if (!target.inReach(local)) {
                Main.walkTo(SEAGULL_POS);
            }
        }
        if (Main.getLevelEvent() >= 25) {
            useTeleport(local, target);
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

    private static void selectSpell(Target target) {
        if (!target.getSpell().isAutoCasted() && Magic.Autocast.select(Magic.Autocast.Mode.OFFENSIVE, target.getSpell())) {
            Main.Sleeping(target.getSpell().isAutoCasted(), 500, 800);


        }
    }


    private static void attackTarget(Target target, Player local) {
        if (target.isNpc()) {
            Npc[] targetingUser = Npcs.getLoaded(x -> x.getTarget() != null && x.getTarget().equals(Players.getLocal()));
            if (targetingUser.length > 0) {
                if (targetingUser[0] != null && targetingUser[0].interact("Attack"))
                    Main.Sleeping(local.isAnimating(), 1000, 2000);
            } else {
                Npc sea = Npcs.getNearest(target::NpcMatches);
                if (sea != null && sea.interact(target.getAction())) {
                    Main.Sleeping(local.isAnimating(), 1000, 2000);
                }
            }

        }
    }

    private static void wearEquipment(int[] equipment) {
        for (int i = 0; i < EQUIPMENT.length; i++) {
            if (Inventory.getFirst(EQUIPMENT).click()) {
                Time.sleep(700, 900);
            }
        }
    }

    private static void useTeleport(Player local, Target target) {
        if (!local.isAnimating() && Magic.cast(target.getSpell()) && Equipment.contains("Staff of air")) {
            Main.Sleeping(local.isAnimating(), 1300, 2400);
        } else if (!Equipment.contains("Staff of air")) {
            if (Inventory.getFirst("Staff of air").click()) {
                Main.Sleeping(!Inventory.contains("Staff of air"), 100, 300);
            }
        }
    }


}
