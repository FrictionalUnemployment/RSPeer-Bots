package org.frictional.Enums;

import org.rspeer.runetek.adapter.scene.Npc;
import org.rspeer.runetek.adapter.scene.Player;
import org.rspeer.runetek.adapter.scene.SceneObject;
import org.rspeer.runetek.api.component.tab.Skill;
import org.rspeer.runetek.api.component.tab.Skills;
import org.rspeer.runetek.api.component.tab.Spell;
import org.rspeer.runetek.api.movement.position.Position;
import org.rspeer.runetek.api.scene.Npcs;
import org.rspeer.runetek.api.scene.SceneObjects;

public enum Target {

    SPLASHING_AIR("Seagull", "Attack", Spell.Modern.WIND_STRIKE, new Position(3027, 3233, 0), true, 13),

    SPLASHING_FIRE("Seagull", "Attack", Spell.Modern.FIRE_STRIKE, new Position(3027, 3233, 0), true, 25),

    VARR_TELE("Varrock Teleport", "Varrock Teleport", Spell.Modern.VARROCK_TELEPORT, new Position(3080, 3251, 0), false, 31),
    LUMB_TELE("Lumbridge Teleport", "Lumbridge Teleport", Spell.Modern.LUMBRIDGE_TELEPORT, new Position(3080, 3251, 0), false, 37),
    FALA_TELE("Falador Teleport", "Falador Teleport", Spell.Modern.FALADOR_TELEPORT, new Position(3080, 3251, 0), false, 45),
    CAM_TELE("Camelot Teleport", "Camelot Teleport", Spell.Modern.CAMELOT_TELEPORT, new Position(3080, 3251, 0), false, 55);


    Target(String Names, String Action, Spell Casting, Position Location, boolean isNpc, int targetLevel) {
        this.Names = Names;
        this.Casting = Casting;
        this.Location = Location;
        this.targetLevel = targetLevel;
        this.isNpc = isNpc;
        this.Action = Action;

    }

    private final String Names;

    private final Spell Casting;

    public Spell getSpell() {
        return this.Casting;
    }

    public String getNames() {
        return this.Names;
    }

    private final Position Location;

    public Position getLocation() {
        return Location;
    }

    private final int targetLevel;

    public int getTargetLevel() {
        return targetLevel;
    }

    private final boolean isNpc;

    public boolean isNpc() {
        return this.isNpc;
    }

    private final String Action;

    public String getAction() {
        return Action;
    }

    public final boolean inReach(Player local) {
        if (isNpc) {
            Npc npcs = Npcs.getNearest(this::NpcMatches);
            return npcs != null;
        } else {
            SceneObject[] objects = SceneObjects.getLoaded(this::ObjectMatches);
            return objects.length > 0 && objects[0] != null;
        }
    }

    public final boolean NpcMatches(Npc npc) {
        if (!npc.containsAction(Action))
            return false;

        if (!npc.getPosition().isPositionInteractable())
            return false;

        return npc.getName().equals(Names);
    }

    public final boolean ObjectMatches(SceneObject object) {
        return object.getName().equals(object);
    }


    public static Target getBestTarget() {
        int magicLevel = Skills.getLevel(Skill.MAGIC);

        Target[] allPossibleTargets = Target.values();
        Target bestTarget = allPossibleTargets[0];

        //Assumes targets are in order of max level
        for (Target t : allPossibleTargets) {
            if (magicLevel < t.getTargetLevel()) {
                bestTarget = t;
                break;
            }
        }
        return bestTarget;
    }

}
