package org.easyengine.engine;

import javafx.util.Pair;
import org.easyengine.domain.Player;
import org.easyengine.domain.Team;
import org.easyengine.engine.space.Pitch;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.easyengine.engine.ActionOutcome.FAIL;
import static org.easyengine.engine.ActionOutcome.SUCCESS;
import static org.easyengine.engine.BallPlayState.*;
import static org.easyengine.environment.PlayerPosition.PositionX.M;

public class Match {

    private static Random rnd = new Random();

    private Team homeTeam;
    private Team awayTeam;

    private boolean homeTeamKickOff;
    private Team possessionTeam;
    private Player possessionPlayer;

    private BallPlayState ballPlayState = KICK_OFF;

    private List<MatchEvent> matchEvents = new ArrayList<>();

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

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Team getPossessionTeam() {
        return possessionTeam;
    }

    public BallPlayState getBallPlayState() {
        return ballPlayState;
    }

    private void changePossession() {
        if (this.homeTeam.equals(possessionTeam)) {
            this.possessionTeam = this.awayTeam;
        } else {
            this.possessionTeam = this.homeTeam;
        }
    }

    public void setState(MatchState state) {
        this.currentTime = state.getTime();
        this.possessionTeam = state.getPossessionTeam();
        this.possessionPlayer = state.getPossessionPlayer();
        this.ballPlayState = state.getBallPlayState();
    }

    public void playCurrentCycle(MatchState state) {
        setState(state);
        switch (ballPlayState) {
            case KICK_OFF:
                kickOff();
                break;
            case FREE_PLAY:
            default:
        }
    }

    public void play() {
        coinToss();
        kickOff();
        while (currentTime < HALF_TIME_DURATION) {
            Action action = this.possessionPlayer.decideAction();
            ActionOutcomeDetails actionOutcomeDetails = executeAction(action);
            MatchEvent event = applyOutcome(actionOutcomeDetails);

            ++currentTime;

            reportEvent(event);

            checkHaltingCondition(PitchPosition.A, null, "Ball reached attacking player");
            checkHaltingCondition(null, GOAL_KICK, "Goal kick");
            if (halting) break;
        }
    }

    private boolean halting;

    private void checkHaltingCondition(PitchPosition pitchPosition, BallPlayState ballPlayState, String description) {
        if (this.possessionPlayer.getPitchPosition() == pitchPosition || this.ballPlayState == ballPlayState) {
            System.out.printf("%s after %d time steps\n", description, currentTime);
            halting = true;
        }
    }

    public void reportEvent(MatchEvent event) {
        this.matchEvents.add(event);
    }

    public MatchEvent applyOutcome(ActionOutcomeDetails actionOutcomeDetails) {

        MatchEvent event = new MatchEvent(actionOutcomeDetails);

        if (PitchPosition.C == actionOutcomeDetails.getTargetPosition()) {
            this.ballPlayState = CORNER_KICK;
            event.setBallPlayState(CORNER_KICK);
        }

        switch(actionOutcomeDetails.getActionType()) {
            case PASS:
                event.setActionPlayer(this.possessionPlayer);
                if (SUCCESS.equals(actionOutcomeDetails.getActionOutcome())) {
                    if (PitchPosition.C == actionOutcomeDetails.getTargetPosition()) {
                        this.possessionPlayer = this.possessionTeam.getRandomTaker(this.possessionTeam.getCornerKickTakers());
                    } else {
                        this.possessionPlayer =
                                this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(actionOutcomeDetails.getTargetPosition()));
                    }
                } else {

                    changePossession();

                    PitchPosition outcomePosition =
                            ProbabilityModel.getFailedOutcomePosition(
                                new Pair<>(actionOutcomeDetails.getInitialPosition(), actionOutcomeDetails.getTargetPosition())
                    );

                    if (PitchPosition.GK == outcomePosition) {
                        this.ballPlayState = GOAL_KICK;
                        event.setBallPlayState(GOAL_KICK);
                        event.setTime(currentTime);
                        event.setDuration(1);
                        return event;
                    }

                    if (PitchPosition.C == actionOutcomeDetails.getTargetPosition()) {
                        this.possessionPlayer = this.possessionTeam.getRandomTaker(this.possessionTeam.getCornerKickTakers());
                    } else {
                        this.possessionPlayer =
                                this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(outcomePosition));
                    }
                }
                event.setOutcomePlayer(this.possessionPlayer);
                event.setTime(currentTime);
                event.setDuration(1);
                break;
            default:
        }

        return event;
    }

    private PitchPosition getSetPiecePosition() {
        if (CORNER_KICK == this.ballPlayState) {
            this.ballPlayState = FREE_PLAY;
            return PitchPosition.C;
        }
        Logger.debug("ERROR: Unknown set piece");
        return this.possessionPlayer.getPitchPosition();
    }

    public ActionOutcomeDetails executeAction(Action action) {

        ActionOutcome actionOutcome = FAIL;
        PitchPosition initialPosition =
                FREE_PLAY == this.ballPlayState ? this.possessionPlayer.getPitchPosition() : getSetPiecePosition() ;
        PitchPosition targetPosition = action.getTarget();

        switch(action.getType()) {
            case PASS:

                Logger.debug("Possession team: " + this.possessionTeam.getName());
                Logger.debug("Initial position: " + initialPosition);
                Logger.debug("Target position: " + targetPosition);

                double successRate = ProbabilityModel.getSuccessRate(initialPosition, targetPosition);
                double outcomeIndex = rnd.nextDouble();
                if (outcomeIndex < successRate) {
                    actionOutcome = SUCCESS;
                    Logger.debug("SUCCESS");
                } else {
                    actionOutcome = FAIL;
                    Logger.debug("FAIL");
                }
                Logger.debugEnd();
                break;
            default:
        }

        return new ActionOutcomeDetails(action.getType(), initialPosition, targetPosition, actionOutcome);
    }

    private void kickOff() {
        List<Player> players = possessionTeam.getPlayerByPositionX(M);
        int playerIndex = rnd.nextInt(players.size());
        this.possessionPlayer = players.get(playerIndex);
        this.ballPlayState = FREE_PLAY;
    }

    private void coinToss() {
        this.homeTeamKickOff = rnd.nextBoolean();
        if (this.homeTeamKickOff) {
            this.possessionTeam = homeTeam;
        } else {
            this.possessionTeam = awayTeam;
        }
    }
}
