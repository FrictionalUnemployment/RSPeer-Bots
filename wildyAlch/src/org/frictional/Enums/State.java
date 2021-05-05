package org.frictional.Enums;

import org.frictional.Executes.*;
import org.frictional.Executes.HandleMuling.TradeMuler;
import org.rspeer.runetek.api.commons.Time;

public enum State {

    STARTING {
        public void execute() {
            Time.sleep(3000);
            Starting.execute();
        }

        @Override
        public void onStart() {

        }
    },
    BANKING {
        public void onStart() {
            Banking.onStart();
        }

        public void execute() {
            Banking.execute();
        }
    },

    BUY_STAFF {
        public void execute() {
            Trading.execute();
        }

        @Override
        public void onStart() {

        }
    },

    SPLASH {
        public void execute() {
            Splashing.execute();
        }

        @Override
        public void onStart() {

        }
    },

    ALCHING {
        public void onStart() {
            Alching.onStart();
        }

        public void execute() {
            Alching.execute();
        }
    },

    WALKING {
        public void execute() {
            Walking.execute();
        }

        @Override
        public void onStart() {

        }
    },
    TRADEMULER {
        @Override
        public void execute() {
            TradeMuler.execute();
        }

        @Override
        public void onStart() {

        }
    };


    public abstract void execute();

    public abstract void onStart();
}
