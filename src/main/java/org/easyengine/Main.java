package org.easyengine;

import org.easyengine.domain.Team;
import org.easyengine.engine.Match;
import org.easyengine.environment.Environment;
import org.easyengine.util.Logger;

public class Main {
    public static void main(String args[]) {

        if (args.length > 0 && "--debug".equals(args[0])) {
            Logger.setDebug();
        }

        Environment.load();

        Team homeTeam = Environment.getTeam("A");
        Team awayTeam = Environment.getTeam("B");

        Match match = new Match(homeTeam, awayTeam);
        match.play();
        match.displayScore();
    }
}
