package org.easyengine.engine;

import java.util.Arrays;
import java.util.List;

public enum ShotOutcome {

    GOAL("Goal scored"),
    GK("Goal kick"),
    BLK_C("Corner kick after shot block"),
    SAVE_C("Corner kick after save"),
    BLK_R_A("Attacking rebound after shot block"),
    BLK_Gkr("Goalkeeper recovery after shot block"),
    BLK_R_M("Distant attacking rebound after shot block"),
    BLK_R_MW("Distant attacking rebound after shot block (Mw)"),
    SAVE_R_A("Attacking rebound after save"),
    SAVE_R_AD("Attacking rebound after save (Ad position)"),
    SAVE("Goalkeeper holds after save");

    private String description;

    ShotOutcome(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public static List<ShotOutcome> getCornerKickOutcomes() {
        return Arrays.asList(BLK_C, SAVE_C);
    }
}
