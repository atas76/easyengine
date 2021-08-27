package org.easyengine.engine;

import org.easyengine.engine.input.domain.Player;
import org.easyengine.engine.output.GoalInfo;
import org.easyengine.engine.output.TeamStats;

import java.util.ArrayList;
import java.util.List;

public class MatchInfo {

    private final List<GoalInfo> goalsScored = new ArrayList<>();
    private final TeamStats teamStats = new TeamStats();

    public void addGoalInfo(int time, Player scorer) {
        this.goalsScored.add(new GoalInfo(time, scorer));
    }

    public void addShotOnTarget() {
        teamStats.addShotOnTarget();
    }

    public void addShotOffTarget() {
        teamStats.addShotOffTarget();
    }

    public List<GoalInfo> getGoalsScored() {
        return goalsScored;
    }

    public TeamStats getTeamStats() {
        return this.teamStats;
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
