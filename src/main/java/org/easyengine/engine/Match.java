package org.easyengine.engine;

import org.easyengine.domain.Player;
import org.easyengine.domain.Team;

import java.util.List;
import java.util.Random;

import static org.easyengine.environment.PlayerPosition.PositionX.M;

public class Match {

    private static Random rnd = new Random();

    private Team homeTeam;
    private Team awayTeam;

    private boolean homeTeamKicksOff;
    private Team possessionTeam;
    private Player possessionPlayer;

    // TODO Simulate time
    // Using a turn-based approach for now
    private int currentTime = 0;

    private final int HALF_TIME_DURATION = 100;

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
    }

    public Player getPossessionPlayer() {
        return possessionPlayer;
    }

    public void play() {
        coinToss();
        kickOff();
        while (currentTime < HALF_TIME_DURATION) {
            /* TODO implement pseudocode
            Action action = this.possessionPlayer.decideAction();
            Outcome outcome = executeAction(action);
            applyOutcome(outcome);
             */
        }
    }

    private void kickOff() {
        List<Player> players = possessionTeam.getPlayerByPositionX(M);
        int playerIndex = rnd.nextInt(players.size());
        this.possessionPlayer = players.get(playerIndex);
    }

    private void coinToss() {
        this.homeTeamKicksOff = rnd.nextBoolean();
        if (this.homeTeamKicksOff) {
            this.possessionTeam = homeTeam;
        } else {
            this.possessionTeam = awayTeam;
        }
    }
}
