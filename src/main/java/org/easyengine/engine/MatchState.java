package org.easyengine.engine;

import org.easyengine.domain.Player;
import org.easyengine.domain.Team;

public class MatchState {

    private int time;
    private Team possessionTeam;
    private Player possessionPlayer;
    private BallPlayState ballPlayState;

    public MatchState(int time, Team possessionTeam, Player possessionPlayer, BallPlayState ballPlayState) {
        this.time = time;
        this.possessionTeam = possessionTeam;
        this.possessionPlayer = possessionPlayer;
        this.ballPlayState = ballPlayState;
    }

    public int getTime() {
        return time;
    }

    public Team getPossessionTeam() {
        return possessionTeam;
    }

    public Player getPossessionPlayer() {
        return possessionPlayer;
    }

    public BallPlayState getBallPlayState() {
        return ballPlayState;
    }
}
