import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.equipment.Equipment;
import org.dreambot.api.methods.container.impl.equipment.EquipmentSlot;
import org.dreambot.api.methods.filter.Filter;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.methods.map.Tile;
import org.dreambot.api.methods.skills.Skill;
import org.dreambot.api.methods.tabs.Tab;
import org.dreambot.api.methods.walking.impl.Walking;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.Category;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.wrappers.interactive.Entity;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.interactive.Locatable;
import org.dreambot.api.wrappers.interactive.SceneObject;
import org.dreambot.api.wrappers.items.Item;

@ScriptManifest(category = Category.MONEYMAKING, name = "Morty", author = "Fic", version = 1.0, description = "Hi")
public class Main extends AbstractScript {
    private Tile Tree;
    private  Area inside;
    //private final int ring[] = {2552, 2554, 2556, 2558, 2560, 2562, 2564, 2566};

    private Area insidePortal = new Area(3330, 4749, 3322, 4753);
    private Area morty = new Area(3381, 3523, 3535, 3308);

    @Override
    public void onStart() {
        Tree = new Tile(getLocalPlayer().getX(), getLocalPlayer().getY());
        inside = new Area(Tree.getX() + 2, Tree.getY() - 1, Tree.getX() - 2, Tree.getY() + 2);
    }

    @Override
    public int onLoop() {
        GameObject log = getGameObjects().closest(3509);

        if(inventoryFull() && inside.contains(getLocalPlayer()) || getSkills().getBoostedLevels(Skill.PRAYER) == 0
        && inside.contains(getLocalPlayer()) && !getInventory().isFull() && !inside.contains(log)){
            teleportToDuel();
        }
        if(inventoryFull() && !inside.contains(getLocalPlayer())
        || !inventoryFull() && !getInventory().contains(19619) && !inside.contains(getLocalPlayer()) && !morty.contains(getLocalPlayer())
        || !inventoryFull() && !getEquipment().contains(r -> r.getName().contains("Ring of dueling(")) && !getInventory().contains(2552)
        || getSkills().getBoostedLevels(Skill.PRAYER) == 0 && !inside.contains(getLocalPlayer())
        && getInventory().contains(2970)
        || !getEquipment().contains(r -> r.getName().contains("Ring of dueling(")) && getInventory().contains(2552) && getInventory().contains(19619)){
            bankItems();
        }
        GameObject portal = getGameObjects().closest("Free-for-all portal");
        if(portal != null && !insidePortal.contains(getLocalPlayer()) && getInventory().contains(19619) && getEquipment().contains(r -> r.getName().contains("Ring of dueling("))){
            goToPortal();
        }

        if(!getBank().isOpen() && !getEquipment().contains(r -> r.getName().contains("Ring of dueling(")) && getInventory().contains(2552)
                && getInventory().interact(2552, "Wear")){
            sleep(mid(250,600));
        }
        if(insidePortal.contains(getLocalPlayer()) && getInventory().contains(19619)){
            teleportPortal();
        }


        if(!getInventory().contains(19619) && !insidePortal.contains(getLocalPlayer()) && !inside.contains(getLocalPlayer())
        && !inventoryFull() && morty.contains(getLocalPlayer())){
            walkToFungus();
        }
        // 3509

        if(inside.contains(getLocalPlayer()) && getLocalPlayer().getTile().equals(Tree)
         && !inside.contains(log) && !inventoryFull() && getSkills().getBoostedLevels(Skill.PRAYER) != 0){
            blossom();
        }
        if(inside.contains(log) && !inventoryFull()){
                pickUp();
        }

        if(inside.contains(getLocalPlayer()) && !getLocalPlayer().getTile().equals(Tree)
        && !inside.contains(log) && !inventoryFull() && getSkills().getBoostedLevels(Skill.PRAYER) != 0){
            walkToSpot();
        }

        return mid(200, 800);
    }

    private void teleportPortal() {
        if(insidePortal.contains(getLocalPlayer()) && !getLocalPlayer().isAnimating() && getInventory().interact(19619,"Break")){
            sleep(high(1500, 2500));
        }
    }
        int counter = 0;
    private void walkToSpot() {
        if(!getLocalPlayer().isMoving() && getWalking().walkOnScreen(Tree)){
            sleep(low(400,900));
           // counter++;
        }else if(counter > 2 && getCamera().mouseRotateTo(high(100, 200), low(10, 60))){
            sleep(mid(100, 300));
        }
        counter = 0;
    }

    private void pickUp() {
        GameObject log = getGameObjects().closest(3509);
        if(log != null && inside.contains(log) && log.interact("Pick")){
            sleep(mid(500, 1500));
        }
    }

    private void blossom() {
        if(!getLocalPlayer().isMoving() && !getLocalPlayer().isAnimating() && getEquipment().interact(EquipmentSlot.WEAPON, "Bloom")){
            sleep(mid(500, 1000));
        }
    }

    private void walkToFungus() {
        if(getWalking().walk(Tree)){
            sleep(mid(500, 1200));
        }
    }

    private void goToPortal() {
        GameObject portal = getGameObjects().closest("Free-for-all portal");
        if(portal != null && !insidePortal.contains(getLocalPlayer()) && !getBank().isOpen() && portal.interact("Enter")) {
            sleep(mid(1000, 2200));

        }

        if(portal.distance() > 15 && !insidePortal.contains(getLocalPlayer()) && getWalking().walk(portal.getTile())) {
            sleep(high(900, 1700));
        }
    }


    private void bankItems() {
        GameObject b = getGameObjects().closest(26707);
        if(!getBank().isOpen() && b.distance() < 10
        && b.interact("Use")) {
            sleep(mid(1000, 1800));
        }else if(!getBank().isOpen() && b.distance() > 10
        && getWalking().walk(b.getTile())){
            sleep(high(1200,2000));
        }else if(getBank().isOpen() && inventoryFull()
        && getBank().depositAllItems() || getBank().isOpen() && getInventory().contains(2970) && getBank().depositAllItems()){
            sleep(low(400, 900));
        }else if(getBank().isOpen() && !inventoryFull() && !getInventory().contains(19619)
        && getBank().withdraw(19619, 1)){
            sleep(mid(1000, 1890));
        }else if(!getEquipment().contains(r -> r.getName().contains("Ring of dueling(")) && !getInventory().contains(2552) && getBank().isOpen() && getBank().withdraw(2552, 1)){
            sleep(low(500, 900));
        }else if(getBank().isOpen() && getInventory().contains(19619) && getInventory().contains(2552) && getBank().close()){
            sleep(low(100, 400));
        }
    }

    private void teleportToDuel() {
        if(!getTabs().isOpen(Tab.EQUIPMENT)
        && getTabs().open(Tab.EQUIPMENT)){
            sleep(low(100, 500));
        }else if(getTabs().isOpen(Tab.EQUIPMENT)
        && !getLocalPlayer().isAnimating() && getEquipment().interact(EquipmentSlot.RING, "Clan wars")
        ){
        sleep(high(1250, 2000));
        }
    }

    public boolean inventoryFull() {
        return getInventory().isFull();
    }

    public static int nextGaussian(int min, int max, double deviation, double mean){
        java.util.Random random = new java.util.Random();
        double result;
        do {
            result = Math.abs(random.nextGaussian() * (deviation) + (mean));
        }while(result > max || result < min);
        return (int)(result);
    }

    public static int high(int min, int max){
        double deviation = (max - min) * 0.3;
        return nextGaussian(min, max, deviation, max);
    }

    public static int low(int min, int max){
        double deviation = (max - min) * 0.3;
        return nextGaussian(min, max, deviation, min);
    }

    public static int mid(int min, int max){
        int difference = max - min;
        double mean = difference / 2;
        double deviation = difference * 0.3;
        return nextGaussian(min, max, deviation, mean);

    }
}