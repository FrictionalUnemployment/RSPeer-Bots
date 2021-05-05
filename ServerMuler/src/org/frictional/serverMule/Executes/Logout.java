package org.frictional.serverMule.Executes;

import org.frictional.serverMule.Main;
import org.rspeer.runetek.api.Game;
import org.rspeer.runetek.api.commons.Time;
import org.rspeer.runetek.event.EventDispatcher;
import org.rspeer.runetek.event.EventDispatcherProvider;
import org.rspeer.runetek.event.EventListener;
import org.rspeer.runetek.event.types.GameStateEvent;
import org.rspeer.script.Script;
import org.rspeer.script.ScriptBlockingEvent;
import org.rspeer.script.events.LoginScreen;

public class Logout {

    public static void execute() {
        if(Game.isLoggedIn() && Game.logout()) {
            Time.sleepUntil(() -> !Game.isLoggedIn(), 3000, 5000);
            Main.stopServer();
            Main.finishedTrading = false;

        }

    }
}
