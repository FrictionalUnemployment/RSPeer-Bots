package org.frictional.serverMule.Enums;

import org.frictional.serverMule.Executes.*;

public enum Stating {

    START {
        @Override
        public void execute() {
            Starting.execute();
        }
    },
    HOPTOWORLD {
        @Override
        public void execute() {
            Hopping.execute();
        }
    },
    TRADEPLAYER{
        @Override
        public void execute() {
            Trading.execute();
        }
    },
    HANDLETRADE {
        @Override
        public void execute() {
            Handletrading.execute();
        }
    },
    LOGOUT {
        @Override
        public void execute() {
            Logout.execute();
        }
    };

    public abstract void execute();

}
