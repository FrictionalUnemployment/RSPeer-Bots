package org.frictional.EmilsLooter.Executes;

import org.frictional.EmilsLooter.Main;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.api.component.InterfaceOptions;
import org.rspeer.runetek.api.component.tab.Minigames;
import org.rspeer.runetek.api.movement.Movement;
import org.rspeer.runetek.api.scene.Players;

public class Starting {


        public static void execute() {

            if(!Main.CWARS_AREA.contains(Players.getLocal())){
                if(Minigames.teleport(Minigames.Destination.CASTLE_WARS) && !Players.getLocal().isAnimating()){
                    Time.sleepUntil(() -> Main.CWARS_AREA.contains(Players.getLocal()), 3000, 5000);
                }

            }

            if (!Movement.isRunEnabled())
                Movement.toggleRun(true);

            Main.updateScriptState(Main.getBestState());

        }
}
