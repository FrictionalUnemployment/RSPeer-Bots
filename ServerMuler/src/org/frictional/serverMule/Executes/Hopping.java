package org.frictional.serverMule.Executes;

import org.frictional.serverMule.Enums.Stating;
import org.frictional.serverMule.Main;
import org.frictional.serverMule.exAPI.ExWorldHopper;
import org.rspeer.runetek.api.Game;
import org.rspeer.script.events.LoginScreen;

public class Hopping {

    public static void execute() {
        if(!Game.isLoggedIn() || Game.getClient().getCurrentWorld() != Main.World){
            LogToWorld(Main.World);
        }else if(Game.isLoggedIn()){
            Main.updateScriptState(Stating.TRADEPLAYER);
        }
    }

    private static void LogToWorld(int World) {
        if(ExWorldHopper.instaHopTo(World));
    }
}
