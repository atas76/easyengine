package org.easyengine.engine;

import javafx.util.Pair;
import org.easyengine.engine.agent.Player;
import org.easyengine.engine.input.domain.Team;
import org.easyengine.engine.environment.ProbabilityModel;
import org.easyengine.engine.space.Pitch;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.engine.input.PlayerPosition;
import org.easyengine.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.easyengine.engine.ActionOutcome.FAIL;
import static org.easyengine.engine.ActionOutcome.SUCCESS;
import static org.easyengine.engine.BallPlayState.*;
import static org.easyengine.engine.input.PlayerPosition.PositionX.F;
import static org.easyengine.engine.input.PlayerPosition.PositionX.M;

public class Match {

    private static Random rnd = new Random();

    private Team homeTeam;
    private Team awayTeam;

    private boolean homeTeamKickOff;
    private Team possessionTeam;
    private Player possessionPlayer;

    private BallPlayState ballPlayState = KICK_OFF;

    private final List<MatchEvent> matchEvents = new ArrayList<>();

    // TODO Simulate time
    // Using a turn-based approach for now
    private int currentTime = 0;

    public static final int HALF_TIME_DURATION = 100;

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

    public List<MatchEvent> getMatchEvents() {
        return this.matchEvents;
    }

    public List<String> getEventReport() {
        return this.matchEvents.stream().map(MatchEvent::toString).collect(Collectors.toList());
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

    public void displayScore() {
        System.out.println(
                this.homeTeam.getName() + " - " + this.awayTeam.getName() + " " +
                this.homeTeam.getGoalsScored() + " - " + this.awayTeam.getGoalsScored()
        );
    }

    public void play() {
        coinToss();
        kickOff();
        playTimePeriod(HALF_TIME_DURATION);
        Logger.debug("Half time");
        Logger.debugEnd();

        setSecondHalfKickOffTeam();
        kickOff();
        playTimePeriod(2 * HALF_TIME_DURATION);
        Logger.debug("Full time");
        Logger.debugEnd();
    }

    public void playTimePeriod(int finalTime) {
        while (currentTime < finalTime) {
            if (KICK_OFF == this.ballPlayState) {
                kickOff();
                continue;
            }
            Action action = this.possessionPlayer.decideAction();
            ActionOutcomeDetails actionOutcomeDetails = executeAction(action);
            MatchEvent event = applyOutcome(actionOutcomeDetails);

            ++currentTime;

            reportEvent(event);

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

        switch(actionOutcomeDetails.getActionType()) {
            case PASS:
                event.setActionPlayer(this.possessionPlayer);
                if (ActionOutcome.CORNER_KICK.equals(actionOutcomeDetails.getActionOutcome())) {
                    this.possessionPlayer = new Player(this.possessionTeam.getRandomTaker(this.possessionTeam.getCornerKickTakers()));
                    this.possessionPlayer.setPitchPosition(PitchPosition.C);
                    this.ballPlayState = CORNER_KICK;
                    event.setBallPlayState(CORNER_KICK);
                } else if (SUCCESS.equals(actionOutcomeDetails.getActionOutcome())) {
                    this.possessionPlayer =
                            new Player(this.possessionTeam.getPlayerByPosition(
                                    Pitch.mapDefaultTacticalPosition(actionOutcomeDetails.getTargetPosition())));
                } else {

                    changePossession();

                    PitchPosition outcomePosition =
                            ProbabilityModel.getFailedOutcomePosition(
                                new Pair<>(actionOutcomeDetails.getInitialPosition(), actionOutcomeDetails.getTargetPosition())
                    );

                    if (PitchPosition.C == outcomePosition) {
                        this.possessionPlayer = new Player(this.possessionTeam.getRandomTaker(this.possessionTeam.getCornerKickTakers()));
                        this.possessionPlayer.setPitchPosition(PitchPosition.C);
                    } else if (PitchPosition.GK == outcomePosition) {
                        this.ballPlayState = GOAL_KICK;
                        event.setBallPlayState(GOAL_KICK);
                        this.possessionPlayer =
                                new Player(this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(outcomePosition)));
                        this.possessionPlayer.setPitchPosition(PitchPosition.GK);
                    } else {
                        this.possessionPlayer =
                                new Player(this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(outcomePosition)));
                    }
                }
                event.setOutcomePlayer(this.possessionPlayer);
                break;
            case SHOT:
                switch(actionOutcomeDetails.getShotOutcome()) {
                    case GOAL:
                        this.possessionTeam.score(this.currentTime, this.possessionPlayer);
                        changePossession();
                        this.ballPlayState = KICK_OFF;
                        break;
                    case GK:
                        changePossession();
                        this.ballPlayState = GOAL_KICK;
                        this.possessionPlayer =
                                new Player(this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(PitchPosition.GK)));
                        this.possessionPlayer.setPitchPosition(PitchPosition.GK);
                        break;
                    case BLK_C:
                        applyCornerKick();
                        break;
                    case SAVE_C: // Future differentiation on stats
                        applyCornerKick();
                        break;
                    case BLK_R_A:
                        applyRebound(F);
                        break;
                    case BLK_Gkr:
                        applyGoalkeeperPossession();
                        break;
                    case BLK_R_M:
                        applyRebound(M);
                        break;
                    case SAVE_R_A:
                        applyRebound(F);
                        break;
                    case SAVE:
                        applyGoalkeeperPossession();
                        break;
                }
            default:
        }
        event.setTime(currentTime);
        event.setDuration(1);

        return event;
    }

    private void applyCornerKick() {
        this.ballPlayState = CORNER_KICK;
        this.possessionPlayer = new Player(this.possessionTeam.getRandomTaker(this.possessionTeam.getCornerKickTakers()));
        this.possessionPlayer.setPitchPosition(PitchPosition.C);
    }

    private void applyGoalkeeperPossession() {
        changePossession();
        this.possessionPlayer = new Player(this.getPossessionTeam().getGoalkeeper());
    }

    private void applyRebound(PlayerPosition.PositionX positionX) {
        List<org.easyengine.engine.input.domain.Player> possibleRebounders = this.possessionTeam.getPlayersByPositionX(positionX);
        this.possessionPlayer = new Player(possibleRebounders.get(new Random().nextInt(possibleRebounders.size())));
    }

    private PitchPosition getSetPiecePosition() {

        PitchPosition setPiecePosition = this.possessionPlayer.getPitchPosition();

        if (CORNER_KICK == this.ballPlayState) {
            setPiecePosition = PitchPosition.C;
        } else if (GOAL_KICK == this.ballPlayState) {
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
                Double cornerKickRate = ProbabilityModel.getCornerKickRate(initialPosition);
                Double successRate = ProbabilityModel.getSuccessRate(initialPosition, targetPosition);
                if (nonNull(cornerKickRate) && rnd.nextDouble() < cornerKickRate) {
                    actionOutcome = ActionOutcome.CORNER_KICK;
                    Logger.debug("Corner kick won");
                } else if (rnd.nextDouble() < successRate) {
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
                Logger.debug("Shot outcome: " + shotOutcome);
                Logger.debugEnd();
                outcomeDetails = new ActionOutcomeDetails(action.getType(), initialPosition, shotOutcome);
            default:
        }
        this.possessionPlayer.setPitchPosition(null); // Reset to default

        return outcomeDetails;
    }

    private void kickOff() {
        this.matchEvents.add(new OutOfPlayEvent(KICK_OFF));
        List<org.easyengine.engine.input.domain.Player> players = possessionTeam.getPlayersByPositionX(M);
        int playerIndex = rnd.nextInt(players.size());
        this.possessionPlayer = new Player(players.get(playerIndex));
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

    private void setSecondHalfKickOffTeam() {
        if (this.homeTeamKickOff) {
            this.possessionTeam = awayTeam;
        } else {
            this.possessionTeam = homeTeam;
        }
    }
}
