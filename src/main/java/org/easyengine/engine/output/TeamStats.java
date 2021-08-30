package org.easyengine.engine.output;

public class TeamStats {

    private int shotsOnTarget = 0;
    private int shotsOffTarget = 0;

    public void addShotOnTarget() {
        ++shotsOnTarget;
    }

    public void addShotOffTarget() {
        ++shotsOffTarget;
    }

    public int getShotsOnTarget() {
        return shotsOnTarget;
    }

    public int getShotsOffTarget() {
        return shotsOffTarget;
    }

    public int getTotalShots() {
        return shotsOnTarget + shotsOffTarget;
    }
}
