package org.easyengine.engine;

import org.easyengine.domain.Player;

import static org.easyengine.engine.BallPlayState.CORNER_KICK;
import static org.easyengine.engine.BallPlayState.FREE_PLAY;

public class MatchEvent extends ActionOutcomeDetails {

    private int time;
    private int duration;
    private Player actionPlayer;
    private Player outcomePlayer;
    private BallPlayState ballPlayState = FREE_PLAY;

    public MatchEvent(ActionOutcomeDetails actionOutcomeDetails) {
        this.actionType = actionOutcomeDetails.actionType;
        this.initialPosition = actionOutcomeDetails.initialPosition;
        this.targetPosition = actionOutcomeDetails.targetPosition;
        this.actionOutcome = actionOutcomeDetails.actionOutcome;
    }

    public void setCornerKick() {
        this.ballPlayState = CORNER_KICK;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Player getActionPlayer() {
        return actionPlayer;
    }

    public void setActionPlayer(Player actionPlayer) {
        this.actionPlayer = actionPlayer;
    }

    public Player getOutcomePlayer() {
        return outcomePlayer;
    }

    public void setOutcomePlayer(Player outcomePlayer) {
        this.outcomePlayer = outcomePlayer;
    }
}
