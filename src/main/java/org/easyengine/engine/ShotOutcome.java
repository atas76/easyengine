package org.easyengine.engine;

public enum ShotOutcome {

    GOAL("Goal"),
    GK("Goal kick"),
    BLK_C("Corner kick after shot block"),
    SAVE_C("Corner kick after save"),
    BLK_R_A("Attacking rebound after shot block"),
    BLK_Gkr("Goalkeeper recovery after shot block"),
    BLK_R_M("Distant attacking rebound after shot block"),
    SAVE_R_A("Attacking rebound after save"),
    SAVE("Goalkeeper holds after save");

    private String description;

    ShotOutcome(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return this.description;
    }
}
