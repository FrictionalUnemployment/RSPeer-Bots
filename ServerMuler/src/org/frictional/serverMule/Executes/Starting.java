package org.frictional.serverMule.Executes;

import org.frictional.serverMule.Enums.Stating;
import org.frictional.serverMule.Main;
import org.rspeer.runetek.api.Game;
import org.rspeer.ui.Log;

public class Starting {


    public static void execute() {
        if(Game.isLoggedIn() || !Game.isLoggedIn()){
            Main.updateScriptState(Stating.HOPTOWORLD);
        }

    }

}
