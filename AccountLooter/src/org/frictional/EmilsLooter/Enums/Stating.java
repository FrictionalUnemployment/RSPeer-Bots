package org.frictional.EmilsLooter.Enums;

import org.frictional.EmilsLooter.Executes.*;
import org.rspeer.runetek.api.commons.Time;

public enum Stating {

    STARTING {
        @Override
        public void execute() {
            Time.sleep(3000);
            Starting.execute();
        }

        @Override
        public void onStart() {

        }
    },
    BANKING {
        @Override
        public void execute() {
        Banking.execute();
        }

        @Override
        public void onStart() {
        }
    },
    MULING {
        @Override
        public void execute() {
            Muling.execute();
        }

        @Override
        public void onStart() {

        }
    },
    LOGOUT {
        @Override
        public void execute() {
            Logout.execute();
        }

        @Override
        public void onStart() {

        }
    };



    public abstract void execute();
    public abstract void onStart();
}

