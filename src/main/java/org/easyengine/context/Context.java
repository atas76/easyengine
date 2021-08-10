package org.easyengine.context;

import org.easyengine.engine.input.domain.Player;
import org.easyengine.engine.input.PlayerPosition;
import org.easyengine.engine.input.domain.Team;

import java.util.HashMap;
import java.util.Map;

import static org.easyengine.engine.input.PlayerPosition.PositionX.*;
import static org.easyengine.engine.input.PlayerPosition.PositionY.*;
import static org.easyengine.engine.input.Tactics.TACTIC_4_4_2;

public class Context {

    public static Map<String, Team> teams = new HashMap<>();

    public static Team getTeam(String teamName) {
        return teams.get(teamName);
    }

    public static int getTeamCount() {
        return teams.size();
    }

    public static void load() {

        Team teamA = new Team("Red", TACTIC_4_4_2);
        Team teamB = new Team("Blue", TACTIC_4_4_2);

        teams.put("A", teamA);
        teams.put("B", teamB);

        teamA.addPlayer(new Player(1, "Hugo L", new PlayerPosition(Gk)));

        teamA.addPlayer(new Player(2, "Kieran T", new PlayerPosition(D, R)));
        teamA.addPlayer(new Player(21, "Lucas H", new PlayerPosition(D, L)));
        teamA.addPlayer(new Player(4, "Toby A", new PlayerPosition(D, C_R)));
        teamA.addPlayer(new Player(6, "Nch", new PlayerPosition(D, C_L)));

        teamA.addPlayer(new Player(13, "NG K", new PlayerPosition(M, C_R)));
        teamA.addPlayer(new Player(12, "Victor W", new PlayerPosition(M, C_L)));
        teamA.addPlayer(new Player(19, "Luka M", new PlayerPosition(M, R)));
        teamA.addPlayer(new Player(14, "Blaise M", new PlayerPosition(M, L)));

        teamA.addPlayer(new Player(9, "Robert L", new PlayerPosition(F, C_R)));
        teamA.addPlayer(new Player(29, "Kingsley C", new PlayerPosition(F, C_L)));

        // Team instructions
        teamA.addCornerKickTaker(19);
        teamA.addCornerKickTaker(14);

        teamB.addPlayer(new Player(1, "Keylor N", new PlayerPosition(Gk)));

        teamB.addPlayer(new Player(4, "Thilo K", new PlayerPosition(D, R)));
        teamB.addPlayer(new Player(3, "Ivan S", new PlayerPosition(D, L)));
        teamB.addPlayer(new Player(2, "Thiago S", new PlayerPosition(D, C_R)));
        teamB.addPlayer(new Player(21, "Doma V", new PlayerPosition(D, C_L)));

        teamB.addPlayer(new Player(17, "Moussa S", new PlayerPosition(M, C_R)));
        teamB.addPlayer(new Player(8, "Leandro P", new PlayerPosition(M, C_L)));
        teamB.addPlayer(new Player(23, "Christian E", new PlayerPosition(M, R)));
        teamB.addPlayer(new Player(29, "Andres I", new PlayerPosition(M, L)));

        teamB.addPlayer(new Player(10, "Harry K", new PlayerPosition(F, C_R)));
        teamB.addPlayer(new Player(39, "Nmr", new PlayerPosition(F, C_L)));

        // Team instructions
        teamB.addCornerKickTaker(23);
        teamB.addCornerKickTaker(29);

        teamA.validateFormation();
        teamB.validateFormation();
    }
}
