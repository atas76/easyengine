package org.easyengine;

import org.easyengine.domain.Team;
import org.easyengine.engine.Match;
import org.easyengine.environment.Environment;

public class Main {
    public static void main(String args[]) {

        Environment.load();

        Team homeTeam = Environment.getTeam("A");
        Team awayTeam = Environment.getTeam("B");

        Match match = new Match(homeTeam, awayTeam);
        match.play();
    }
}
