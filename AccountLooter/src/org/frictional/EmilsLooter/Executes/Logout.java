package org.frictional.EmilsLooter.Executes;

import org.frictional.EmilsLooter.Enums.Stating;
import org.frictional.EmilsLooter.Main;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.ui.Log;

public class Logout {
    public static void execute() {
        if(Game.isLoggedIn()){
            Logout();
        }
        if(!Game.isLoggedIn()){
            Main.updateScriptState(Stating.STARTING);
        }
    }

    public static void Logout() {
        if(Game.logout()){
            Log.fine("How many times have we been here?");
            Main.accounts.remove(0);
            Main.finishedWithdrawing = false;
            Main.interruptedWithdrawing = false;
            Time.sleep(5000, 8000);
        }

    }
}
