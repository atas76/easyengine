package org.easyengine;

import org.easyengine.domain.Team;
import org.easyengine.engine.Match;
import org.easyengine.environment.Environment;
import org.easyengine.util.Logger;

import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String args[]) {

        if (args.length > 0 ) {
            List<String> argsList = Arrays.asList(args);

            if (argsList.contains("--debug")) {
                Logger.setDebug();
            }
            if (argsList.contains("--events")) {
                Logger.setEvents();
            }
            if (argsList.contains("--info")) {
                Logger.setInfo();
            }
        }

        Environment.load();

        Team homeTeam = Environment.getTeam("A");
        Team awayTeam = Environment.getTeam("B");

        Match match = new Match(homeTeam, awayTeam);
        match.play();
        match.displayScore();
        Logger.debugEnd();

        Logger.info(homeTeam.getName(), homeTeam.getMatchInfo());
        Logger.infoEnd();
        Logger.info(awayTeam.getName(), awayTeam.getMatchInfo());
        Logger.infoEnd();

        Logger.report(match.getMatchEvents());
    }
}
