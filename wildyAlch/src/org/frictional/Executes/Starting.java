package org.frictional.Executes;

import org.frictional.Main;
import org.rspeer.runetek.api.component.tab.Combat;
import org.rspeer.runetek.api.movement.Movement;

public class Starting {

    private Starting() {

    }

    public static void execute() {

        if (!Combat.isAutoRetaliateOn())
            Combat.toggleAutoRetaliate(true);

        if (!Movement.isRunEnabled())
            Movement.toggleRun(true);

        Main.updateScriptState(Main.getBestState());

    }

}
