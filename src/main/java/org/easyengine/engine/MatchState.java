package org.easyengine.engine;

import org.easyengine.engine.agent.Player;
import org.easyengine.engine.input.domain.Team;

public class MatchState {

    private int time = 0;
    private Team possessionTeam;
    private Player possessionPlayer;
    private BallPlayState ballPlayState;

    public MatchState(int time, Team possessionTeam, Player possessionPlayer, BallPlayState ballPlayState) {
        this(possessionTeam, possessionPlayer, ballPlayState);
        this.time = time;
    }

    public MatchState(Team possessionTeam, Player possessionPlayer, BallPlayState ballPlayState) {
        this.possessionTeam = possessionTeam;
        this.possessionPlayer = possessionPlayer;
        this.ballPlayState = ballPlayState;
    }

    public MatchState(Team possessionTeam, Player possessionPlayer) {
        this.possessionTeam = possessionTeam;
        this.possessionPlayer = possessionPlayer;
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
