package org.easyengine.engine;

import org.easyengine.domain.Player;
import org.easyengine.engine.output.GoalInfo;

import java.util.ArrayList;
import java.util.List;

public class MatchInfo {

    private final List<GoalInfo> goalsScored = new ArrayList<>();

    public void addGoalInfo(int time, Player scorer) {
        this.goalsScored.add(new GoalInfo(time, scorer));
    }

    public List<GoalInfo> getGoalsScored() {
        return goalsScored;
    }

    @Override
    public String toString() {

        StringBuilder retVal = new StringBuilder();

        retVal.append("Goals scored:\n");
        this.goalsScored.forEach(goalInfo -> {
            retVal.append(goalInfo.toString()).append("\n");
        });

        return retVal.toString();
    }
}
