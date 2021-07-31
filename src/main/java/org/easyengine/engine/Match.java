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
            if (KICK_OFF == this.ballPlayState) {
                kickOff();
                continue;
            }
            Action action = this.possessionPlayer.decideAction();
            ActionOutcomeDetails actionOutcomeDetails = executeAction(action);
            MatchEvent event = applyOutcome(actionOutcomeDetails);

            ++currentTime;

            reportEvent(event);

            checkHaltingCondition(PitchPosition.A, null, "Ball reached attacking player");
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
                        this.possessionPlayer.setPitchPosition(PitchPosition.C);
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

                    if (PitchPosition.C == actionOutcomeDetails.getTargetPosition()) {
                        this.possessionPlayer = this.possessionTeam.getRandomTaker(this.possessionTeam.getCornerKickTakers());
                        this.possessionPlayer.setPitchPosition(PitchPosition.C);
                    } else if (PitchPosition.GK == outcomePosition) {
                        this.ballPlayState = GOAL_KICK;
                        event.setBallPlayState(GOAL_KICK);
                        this.possessionPlayer =
                                this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(outcomePosition));
                        this.possessionPlayer.setPitchPosition(PitchPosition.GK);
                    } else {
                        this.possessionPlayer =
                                this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(outcomePosition));
                    }
                }
                event.setOutcomePlayer(this.possessionPlayer);
                event.setTime(currentTime);
                event.setDuration(1);
                break;
            case SHOT:
                // GOAL, GK, BLK_C, SAVE_C, BLK_R_A, BLK_Gkr, BLK_R_Mw, SAVE_R_A, SAVE_Gkr
                switch(actionOutcomeDetails.getShotOutcome()) {
                    case GOAL:
                        this.possessionTeam.score();
                        changePossession();
                        this.ballPlayState = KICK_OFF;
                        break;
                    case GK:
                        changePossession();
                        this.ballPlayState = GOAL_KICK;
                        break;
                    case BLK_C:
                        this.ballPlayState = CORNER_KICK;
                        break;
                    case SAVE_C: // Future differentiation on stats
                        this.ballPlayState = CORNER_KICK;
                        break;
                }
            default:
        }

        return event;
    }

    private PitchPosition getSetPiecePosition() {

        PitchPosition setPiecePosition = this.possessionPlayer.getPitchPosition();

        if (CORNER_KICK == this.ballPlayState) {
            setPiecePosition = PitchPosition.C;
        } if (GOAL_KICK == this.ballPlayState) {
            setPiecePosition = PitchPosition.GK;
        }
        else {
            Logger.debug("ERROR: Unknown set piece");
        }
        this.ballPlayState = FREE_PLAY;
        return setPiecePosition;
    }

    public ActionOutcomeDetails executeAction(Action action) {

        ActionOutcome actionOutcome;
        PitchPosition initialPosition =
                FREE_PLAY == this.ballPlayState ? this.possessionPlayer.getPitchPosition() : getSetPiecePosition();
        PitchPosition targetPosition = action.getTarget();

        Logger.debug("Possession team: " + this.possessionTeam.getName());
        Logger.debug("Initial position: " + initialPosition);

        ActionOutcomeDetails outcomeDetails = null;

        switch(action.getType()) {
            case PASS:

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
                outcomeDetails = new ActionOutcomeDetails(action.getType(), initialPosition, targetPosition, actionOutcome);
                break;
            case SHOT:
                Logger.debug("Shot at goal");
                ShotOutcome shotOutcome = ProbabilityModel.getShotOutcome(new Random().nextDouble());
                outcomeDetails = new ActionOutcomeDetails(action.getType(), initialPosition, shotOutcome);
            default:
        }
        this.possessionPlayer.setPitchPosition(null); // Reset to default

        return outcomeDetails;
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
