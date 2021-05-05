package org.frictional;

import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.commons.math.Random;
import org.rspeer.runetek.api.component.Bank;
import org.rspeer.runetek.api.component.tab.Equipment;
import org.rspeer.runetek.api.component.tab.EquipmentSlot;
import org.rspeer.runetek.api.component.tab.Inventory;
import org.rspeer.runetek.api.component.tab.Prayers;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.movement.position.Area;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Players;
import org.rspeer.runetek.api.scene.SceneObjects;
import org.rspeer.runetek.event.listeners.RenderListener;
import org.rspeer.runetek.event.types.RenderEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptMeta;

@ScriptMeta(developer = "Frictional", name = "Mort Fungus", desc = "Private")
public class Main extends Script implements RenderListener {

    private Area outsideArea =  Area.rectangular(3425, 3467, 3449, 3458);
    private final int ring[] = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};
    private Area clanWars = Area.rectangular(3344, 3183, 3394, 3137);
    private Area insidePortal = Area.rectangular(3330, 4749, 3322, 4753);
    private Position Tree;
    private Area insideArea;
    int invFungus = 0;
    @Override
    public void onStart() {
        Tree = new Position(Players.getLocal().getX(), Players.getLocal().getY());
        insideArea = Area.rectangular(Tree.getX() + 2, Tree.getY() - 1,
                Tree.getX() - 2, Tree.getY() + 2 );
    }

    @Override
    public int loop() {
        SceneObject treeVis = SceneObjects.newQuery().names("Fungi on log").within(insideArea).results().nearest();
        if(Prayers.getPoints() == 0
                && treeVis == null
        && !clanWars.contains(Players.getLocal().getPosition())
        && !Inventory.contains(19619)
                && !Players.getLocal().isAnimating()
                && !outsideArea.contains(Players.getLocal())
        || Inventory.isFull()
        && !Inventory.contains(19619)
        && !clanWars.contains(Players.getLocal().getPosition())
        && !Players.getLocal().isAnimating()){
            if(EquipmentSlot.RING.interact("Clan Wars")) {
                Time.sleepWhile(() -> Players.getLocal().isAnimating(), 457, 1768);
            }
        }

        if(!Equipment.contains(ring) && Inventory.contains(2552)
        && !Bank.isOpen()){
            if(Inventory.getFirst(ring).click()){
                Time.sleepWhile(() -> Inventory.contains(ring), 762, 1772);
            }
        }

        if(Inventory.contains(19619)
                && !clanWars.contains(Players.getLocal().getPosition()) && !Players.getLocal().isAnimating()){
            if(Inventory.getFirst(19619).click()){
                Time.sleepWhile(() -> Players.getLocal().isAnimating(), 541, 1541);
            }
        }

        if(!Inventory.contains(19619)
                && !Inventory.isFull()
        && outsideArea.contains(Players.getLocal().getPosition())
        && !Players.getLocal().isMoving()
        && !Players.getLocal().isAnimating()){
            moveInside();
        }


        if(!outsideArea.contains(Players.getLocal().getPosition())
                && !Tree.getPosition().equals(Players.getLocal().getPosition())
        && Prayers.getPoints() != 0
        && !Inventory.isFull()
                && treeVis == null
        && !clanWars.contains(Players.getLocal().getPosition())
        && !insidePortal.contains(Players.getLocal().getPosition())){
            walkToPosition();
            }

        if(!outsideArea.contains(Players.getLocal().getPosition())
            && Players.getLocal().getPosition().equals(Tree.getPosition())
        && treeVis == null
        && Prayers.getPoints() != 0
        && !Inventory.isFull()
        && !Players.getLocal().isAnimating()){
            blossom();
        }

        if(!outsideArea.contains(Players.getLocal())
        && treeVis != null
        && !Inventory.isFull()
        && !Players.getLocal().isAnimating()
        && insideArea.contains(treeVis.getPosition())){
            pickUp();
        }

        if(clanWars.contains(Players.getLocal().getPosition())
        && Inventory.contains(2970)
        || !Equipment.contains(ring)
        && clanWars.contains(Players.getLocal().getPosition())
                && !Inventory.contains(ring)
        || clanWars.contains(Players.getLocal().getPosition())
        && !Inventory.contains(2970)
        && !Inventory.contains(19619)){
            handleBanking();
        }

        if(clanWars.contains(Players.getLocal().getPosition())
        && Inventory.contains(19619)
        && !Inventory.contains(2970)){
            goToPortal();
        }


        return Random.nextInt(195, 748);
    }

    private void goToPortal() {
        SceneObject Portal = SceneObjects.getNearest(26645);
        if(Portal != null && !Players.getLocal().isMoving()){
            if(Portal.click()){
                Time.sleepWhile(() -> Players.getLocal().isMoving(), 878,2958);
            }
        }
    }

    private void handleBanking() {
        SceneObject bankChest = SceneObjects.getNearest(26707);
        if(!Bank.isOpen() && !Players.getLocal().isMoving()) {
            if (bankChest.click()) {
                Time.sleepWhile(() -> Players.getLocal().isMoving(), 378,3213);
            }
        }else if(Inventory.contains(2970)){
            if(Bank.depositInventory()){
                Time.sleepWhile(() -> Inventory.contains(2970), 431,859);
            }
        }else if(!Equipment.contains(ring) && !Inventory.contains(ring)){
            if(Bank.withdraw(2552, 1)){
                Time.sleepWhile(() -> !Inventory.contains(2552), 300,1230);
            }
        }else if(!Inventory.contains(2970) && !Inventory.contains(19619)){
            if(Bank.withdraw(19619, 1)) {
                Time.sleepWhile(() -> !Inventory.contains(19619), 231, 890);

            }
        }

    }
    private boolean fungusPicked() {
        return Inventory.getCount(2970) > invFungus;
    }
    private void pickUp() {
        SceneObject treeVis = SceneObjects.getNearest(3509);
        invFungus = Inventory.getCount(2970);
        if(insideArea.contains(treeVis.getPosition())) {
            if (treeVis.click()) {
                Time.sleepUntil(this::fungusPicked, 795, 1857);
            }
        }
    }

    private void blossom() {
        if(Equipment.interact(2963, "Bloom")){
            Time.sleepWhile(() -> Players.getLocal().isAnimating(), 685, 942);
        }
    }

    private void walkToPosition() {

        if(!insideArea.contains(Players.getLocal().getPosition())){
            if(Movement.walkToRandomized(Tree.getPosition())) {
                Time.sleepWhile(() -> Players.getLocal().isMoving(), Random.mid(324, 958));
            }
        }else if(!Players.getLocal().getPosition().equals(Tree.getPosition()) && !Players.getLocal().isMoving()){
         Movement.setWalkFlag(Tree.getPosition());
            Time.sleepUntil(() -> Players.getLocal().getPosition().equals(Tree.getPosition()), Random.mid(458, 1327));
        }
    }

    private void moveInside() {
        SceneObject outsideGate = SceneObjects.getNearest(3507);

        if(outsideGate.interact("Open")){
            Time.sleepWhile(() -> Players.getLocal().isMoving(), 357, 1127);
        }
    }

    @Override
    public void notify(RenderEvent renderEvent) {

    }
}
