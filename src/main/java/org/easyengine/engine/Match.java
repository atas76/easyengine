package org.easyengine.engine;

import org.easyengine.domain.Player;
import org.easyengine.domain.Team;
import org.easyengine.engine.space.Pitch;
import org.easyengine.engine.space.PitchPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.easyengine.engine.ActionOutcome.FAIL;
import static org.easyengine.engine.ActionOutcome.SUCCESS;
import static org.easyengine.environment.PlayerPosition.PositionX.M;

public class Match {

    private static Random rnd = new Random();

    private Team homeTeam;
    private Team awayTeam;

    private boolean homeTeamKickOff;
    private Team possessionTeam;
    private Player possessionPlayer;

    private BallPlayState ballPlayState;

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

    public int getNumberOfEvents() {
        return this.matchEvents.size();
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

            if (this.possessionPlayer.getPitchPosition() == PitchPosition.A) {
                System.out.printf("Ball reached attacking player after %d time steps\n", currentTime);
                break;
            }
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
                if (SUCCESS.equals(actionOutcomeDetails.getActionOutcome())) {
                    this.possessionPlayer =
                            this.possessionTeam.getPlayerByPosition(Pitch.mapDefaultTacticalPosition(actionOutcomeDetails.getTargetPosition()));
                } else {
                    changePossession();
                    this.possessionPlayer =
                            this.possessionTeam.getPlayerByPosition(
                                    Pitch.mapDefaultTacticalPosition(
                                            Pitch.mapDefendingPitchPosition(actionOutcomeDetails.getTargetPosition())));
                }
                event.setOutcomePlayer(this.possessionPlayer);
                event.setTime(currentTime);
                event.setDuration(1);
                break;
            default:
        }

        return event;
    }

    public ActionOutcomeDetails executeAction(Action action) {

        ActionOutcome actionOutcome = FAIL;
        PitchPosition initialPosition = this.possessionPlayer.getPitchPosition();
        PitchPosition targetPosition = action.getTarget();

        switch(action.getType()) {
            case PASS:

                System.out.println("Possession team: " + this.possessionTeam.getName());
                System.out.println("Initial position: " + initialPosition);
                System.out.println("Target position: " + targetPosition);

                double successRate = ProbabilityModel.getSuccessRate(initialPosition, targetPosition);
                double outcomeIndex = rnd.nextDouble();
                if (outcomeIndex < successRate) {
                    actionOutcome = SUCCESS;
                    System.out.println("SUCCESS");
                } else {
                    actionOutcome = FAIL;
                    System.out.println("FAIL");
                }
                System.out.println();
                break;
            default:
        }

        return new ActionOutcomeDetails(action.getType(), initialPosition, targetPosition, actionOutcome);
    }

    private void kickOff() {
        List<Player> players = possessionTeam.getPlayerByPositionX(M);
        int playerIndex = rnd.nextInt(players.size());
        this.possessionPlayer = players.get(playerIndex);
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
