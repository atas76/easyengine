package org.easyengine.engine;

import org.easyengine.input.domain.Player;

import static java.util.Objects.nonNull;
import static org.easyengine.engine.BallPlayState.FREE_PLAY;

public class MatchEvent extends ActionOutcomeDetails {

    private int time;
    private int duration;
    private Player actionPlayer;
    private Player outcomePlayer;
    protected BallPlayState ballPlayState = FREE_PLAY;
    protected boolean outOfPlay = false;

    public MatchEvent(ActionOutcomeDetails actionOutcomeDetails) {
        this.actionType = actionOutcomeDetails.actionType;
        this.initialPosition = actionOutcomeDetails.initialPosition;
        this.targetPosition = actionOutcomeDetails.targetPosition;
        this.actionOutcome = actionOutcomeDetails.actionOutcome;
        this.shotOutcome = actionOutcomeDetails.shotOutcome;
    }

    public MatchEvent(BallPlayState ballPlayState) {
        this.outOfPlay = true;
        this.ballPlayState = ballPlayState;
    }

    public boolean isOutOfPlay() {
        return this.outOfPlay;
    }

    @Override
    public String toString() {
        return "{ " +
                    this.time + ", " +
                    this.actionType + ", " +
                    this.initialPosition + ", " +
                    this.targetPosition + ", " +
                    (nonNull(this.actionOutcome) ? this.actionOutcome : this.shotOutcome) +
                " }";
    }

    public void setBallPlayState(BallPlayState ballPlayState) {
        this.ballPlayState = ballPlayState;
    }

    public BallPlayState getBallPlayState() {
        return ballPlayState;
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
