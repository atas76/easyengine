package org.easyengine.engine.output;

import org.easyengine.engine.input.domain.Player;

public class GoalInfo {

    private int time;
    private Player scorer;

    public GoalInfo(int time, Player scorer) {
        this.time = time;
        this.scorer = scorer;
    }

    public int getTime() {
        return time;
    }

    public Player getScorer() {
        return scorer;
    }

    @Override
    public String toString() {
        return "{ " + this.time + ": " + this.scorer.getName() + " }";
    }
}
