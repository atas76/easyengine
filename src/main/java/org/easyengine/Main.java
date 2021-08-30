package org.easyengine;

import org.easyengine.engine.input.domain.Team;
import org.easyengine.engine.Match;
import org.easyengine.context.Context;
import org.easyengine.util.Config;
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
            if (argsList.contains("--ai")) {
                Config.setAI();
            }
        }

        Context.load();

        Team homeTeam = Context.getTeam("A");
        Team awayTeam = Context.getTeam("B");

        Match match = new Match(homeTeam, awayTeam);
        match.play();
        match.displayScore();
        Logger.infoEnd();

        Logger.info(homeTeam.getName(), homeTeam.getMatchInfo().toString());
        Logger.infoEnd();
        Logger.info(awayTeam.getName(), awayTeam.getMatchInfo().toString());
        Logger.infoEnd();
        Logger.infoH2HStat("Shots on target",
                homeTeam.getName(), homeTeam.getMatchInfo().getTeamStats().getShotsOnTarget(),
                awayTeam.getName(), awayTeam.getMatchInfo().getTeamStats().getShotsOnTarget()
        );
        Logger.infoEnd();

        Logger.report(match.getEventReport());
    }
}
