package engine;

import org.easyengine.engine.*;
import org.easyengine.engine.ActionOutcomeDetails;
import org.easyengine.engine.MatchEvent;
import org.easyengine.engine.agent.Player;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.context.Context;
import org.easyengine.engine.input.PlayerPosition;
import org.easyengine.util.Config;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.easyengine.engine.ActionOutcome.FAIL;
import static org.easyengine.engine.ActionOutcome.SUCCESS;
import static org.easyengine.engine.ActionType.*;
import static org.easyengine.engine.BallPlayState.*;
import static org.easyengine.engine.Match.HALF_TIME_DURATION;
import static org.easyengine.engine.ShotOutcome.GOAL;
import static org.easyengine.engine.input.PlayerPosition.PositionY.*;
import static org.easyengine.engine.space.PitchPosition.*;
import static org.easyengine.engine.input.PlayerPosition.PositionX.F;
import static org.easyengine.engine.input.PlayerPosition.PositionX.M;
import static org.easyengine.engine.space.PitchPosition.C;
import static org.junit.Assert.*;

public class MatchTest {

    private static Match match;

    @Before
    public void setUp() {
        Context.load();
        match = new Match(Context.getTeam("A"), Context.getTeam("B"));
    }

    @Test
    public void testSuccessfulMatchEvents() {

        match.play();
        List<MatchEvent> matchEvents = match.getMatchEvents();

        for (int i = 0; i < matchEvents.size() - 1; i++) {
            MatchEvent currentEvent = matchEvents.get(i);
            MatchEvent nextEvent = matchEvents.get(i + 1);

            if (SUCCESS == currentEvent.getActionOutcome() && !nextEvent.isOutOfPlay()) {
                assertEquals(currentEvent.getTargetPosition(), nextEvent.getInitialPosition());
            }
        }
    }

    // TODO Review AI algorithm after finishing with probability model
    /*
    @Test
    public void testAIOutcomes() {
        final int REPETITIONS = 30;

        int dataGoals = 0;
        for (int i = 0; i < REPETITIONS; i++) {
            Match dataMatch = new Match(Context.getTeam("A"), Context.getTeam("B"));
            dataMatch.play();
            dataGoals += dataMatch.getHomeTeam().getGoalsScored() + dataMatch.getAwayTeam().getGoalsScored();
        }
        Config.setAI();
        int aiGoals = 0;
        for (int i = 0; i < REPETITIONS; i++) {
            Match aiMatch = new Match(Context.getTeam("A"), Context.getTeam("B"));
            aiMatch.play();
            aiGoals += aiMatch.getHomeTeam().getGoalsScored() + aiMatch.getAwayTeam().getGoalsScored();
        }

        assertTrue(aiGoals > dataGoals);
    }
     */

    @Test
    public void testKickOff() {

        match.playCurrentCycle(new MatchState(Context.getTeam("A"), null, KICK_OFF));
        Player player = match.getPossessionPlayer();
        PitchPosition pitchPosition = player.getPitchPosition();

        assertEquals(M, player.getPlayerPosition().getX());
        assertTrue(pitchPosition == PitchPosition.M || pitchPosition == Mw);
        assertEquals(FREE_PLAY, match.getBallPlayState());
    }

    @Test
    public void testEventReporting() {
        match.setState(new MatchState(match.getHomeTeam(), null, KICK_OFF));

        match.playTimePeriod(HALF_TIME_DURATION);

        assertEquals(HALF_TIME_DURATION,
                match.getMatchEvents().size() -
                        match.getHomeTeam().getGoalsScored() - match.getAwayTeam().getGoalsScored() - 1);
    }

    @Test
    public void testGoalKickExecution() {
        org.easyengine.engine.input.domain.Player taker = match.getHomeTeam().getPlayerByPosition(new PlayerPosition(PlayerPosition.PositionX.Gk));
        match.setState(new MatchState(match.getHomeTeam(), new Player(taker), GOAL_KICK));

        match.applyOutcome(match.executeAction(new Action(PASS, PitchPosition.M)));

        assertNotSame(GOAL_KICK, match.getBallPlayState());
        assertNotSame(taker, match.getPossessionPlayer());
        assertNotSame(PitchPosition.GK, match.getPossessionPlayer().getPitchPosition());
        assertNotSame(taker, match.getPossessionPlayer());
    }

    @Test
    public void testCornerKickShortPassExecution() {
        // TODO Reverted implementation
        /*
        org.easyengine.engine.input.domain.Player taker = match.getHomeTeam().getRandomTaker(match.getHomeTeam().getCornerKickTakers());
        match.setState(new MatchState(match.getHomeTeam(), new Player(taker), CORNER_KICK));

        match.applyOutcome(match.executeAction(new Action(PASS, Aw)));

        assertNotSame(CORNER_KICK, match.getBallPlayState());
        assertNotSame(taker, match.getPossessionPlayer());
         */
    }

    @Test
    public void testSuccessfulCrossFromCornerKick() {
        org.easyengine.engine.input.domain.Player taker = match.getHomeTeam().getRandomTaker(match.getHomeTeam().getCornerKickTakers());
        match.setState(new MatchState(match.getHomeTeam(), new Player(taker), CORNER_KICK));

        match.applyOutcome(match.executeAction(new Action(CROSS, A)));

        assertNotSame(CORNER_KICK, match.getBallPlayState());
        assertNotSame(taker, match.getPossessionPlayer());
    }

    @Test
    public void testUnsuccessfulCrossFromCornerKick() {
        org.easyengine.engine.input.domain.Player taker = match.getHomeTeam().getRandomTaker(match.getHomeTeam().getCornerKickTakers());
        match.setState(new MatchState(match.getHomeTeam(), new Player(taker))); // Skipping ball play state check, checking directly the action outcome

        match.applyOutcome(new ActionOutcomeDetails(CROSS, C, A, FAIL));

        assertNotSame(taker, match.getPossessionPlayer());
        assertEquals(match.getAwayTeam(), match.getPossessionTeam());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 1);
    }

    @Test
    public void testGoalScored() {
        match.setState(new MatchState(10, match.getHomeTeam(),
                new Player(9, "Robert L", new PlayerPosition(F, C_R)), FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, GOAL));

        assertEquals(1, match.getHomeTeam().getGoalsScored());
        assertNotSame(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(KICK_OFF, match.getBallPlayState());
        assertEquals(1, match.getHomeTeam().getMatchInfo().getGoalsScored().size());
        assertEquals(10, match.getHomeTeam().getMatchInfo().getGoalsScored().get(0).getTime());
        assertEquals(1, match.getHomeTeam().getMatchInfo().getTeamStats().getShotsOnTarget());
        assertEquals(9,
                match.getHomeTeam().getMatchInfo().getGoalsScored().get(0).getScorer().getShirtNumber().intValue());
    }

    @Test
    public void testShotOffTarget() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.GK));

        assertNotSame(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(GOAL_KICK, match.getBallPlayState());
        assertEquals(1, match.getPossessionPlayer().getShirtNumber().intValue());
        assertEquals(PitchPosition.GK, match.getPossessionPlayer().getPitchPosition());
        assertEquals(1, match.getHomeTeam().getMatchInfo().getTeamStats().getShotsOffTarget());
    }

    @Test
    public void testCornerKickAfterShotBlocked() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.BLK_C));

        assertEquals(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(CORNER_KICK, match.getBallPlayState());
    }

    @Test
    public void testCornerKickAfterPass() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(PASS, D, null, ActionOutcome.CORNER_KICK));

        assertEquals(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(CORNER_KICK, match.getBallPlayState());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 19 ||
                match.getPossessionPlayer().getShirtNumber() == 14);
    }

    @Test
    public void testCornerKickAfterGoalkeeperSave() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.SAVE_C));

        assertEquals(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(CORNER_KICK, match.getBallPlayState());
    }

    @Test
    public void testOffensiveReboundAfterShotBlock() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.BLK_R_A));

        assertEquals(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(FREE_PLAY, match.getBallPlayState());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 9 ||
                match.getPossessionPlayer().getShirtNumber() == 29);
    }

    @Test
    public void testOffensiveReboundAfterSave() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.SAVE_R_A));

        assertEquals(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(FREE_PLAY, match.getBallPlayState());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 9 ||
                match.getPossessionPlayer().getShirtNumber() == 29);
    }

    @Test
    public void testDistantOffensiveReboundAfterShotBlock() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.BLK_R_M));

        assertEquals(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(FREE_PLAY, match.getBallPlayState());
        assertTrue(Arrays.asList(13, 12, 19, 14).contains(match.getPossessionPlayer().getShirtNumber()));
    }

    @Test
    public void testGoalkeeperPossessionAfterShotBlock() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.BLK_Gkr));

        assertNotSame(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(FREE_PLAY, match.getBallPlayState());
        assertEquals(1, match.getPossessionPlayer().getShirtNumber().intValue());
    }

    @Test
    public void testGoalkeeperSave() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.SAVE));

        assertNotSame(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(FREE_PLAY, match.getBallPlayState());
        assertEquals(1, match.getPossessionPlayer().getShirtNumber().intValue());
        assertEquals(1, match.getHomeTeam().getMatchInfo().getTeamStats().getShotsOnTarget());
    }

    @Test
    public void testShotDecision() {
        org.easyengine.engine.input.domain.Player player = match.getHomeTeam().getPlayerByPosition(new PlayerPosition(PlayerPosition.PositionX.F, PlayerPosition.PositionY.C_R));
        Player agent = new Player(player);
        match.setState(new MatchState(match.getHomeTeam(), agent, FREE_PLAY));

        Action action = agent.decideAction();

        assertTrue(action.getType() == SHOT
                || (action.getType() == MOVE && action.getTarget() == Aw));
    }

    @Test
    public void testPassSuccess() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(PASS, Mw, D, SUCCESS));

        assertEquals(match.getPossessionTeam(), match.getHomeTeam());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 4
                || match.getPossessionPlayer().getShirtNumber() == 6);
        assertEquals(PASS, event.getActionType());
        assertEquals(Mw, event.getInitialPosition());
        assertEquals(D, event.getTargetPosition());
        assertEquals(SUCCESS, event.getActionOutcome());
    }

    @Test
    public void testCrossSuccess() {
        Player player = new Player(29, "Kingsley C", new PlayerPosition(F, C_L));
        match.setState(new MatchState(match.getHomeTeam(), player, FREE_PLAY));
        player.setPitchPosition(Aw);

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(CROSS, Aw, A, SUCCESS));

        assertEquals(match.getPossessionTeam(), match.getHomeTeam());
        assertEquals(9, (int) match.getPossessionPlayer().getShirtNumber());
        assertEquals(CROSS, event.getActionType());
        assertEquals(Aw, event.getInitialPosition());
        assertEquals(A, event.getTargetPosition());
        assertEquals(SUCCESS, event.getActionOutcome());
    }

    @Test
    public void testPlayerSuccessfulMovementWithBall() {
        match.setState(new MatchState(match.getHomeTeam(),
                new Player(12, "Victor W", new PlayerPosition(M, C_L)), FREE_PLAY));

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(MOVE, PitchPosition.M, A, SUCCESS));

        assertEquals(match.getPossessionTeam(), match.getHomeTeam());
        assertEquals(12, match.getPossessionPlayer().getShirtNumber().intValue());
        assertEquals(MOVE, event.getActionType());
        assertEquals(PitchPosition.M, event.getInitialPosition());
        assertEquals(A, event.getTargetPosition());
        assertEquals(SUCCESS, event.getActionOutcome());
    }

    @Test
    public void testPlayerMovementWithBallToAttackingWing() {
        match.setState(new MatchState(match.getHomeTeam(),
                new Player(14, "Blaise M", new PlayerPosition(M, L)), FREE_PLAY));

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(MOVE, Mw, Aw, SUCCESS));

        assertEquals(match.getPossessionTeam(), match.getHomeTeam());
        assertEquals(14, match.getPossessionPlayer().getShirtNumber().intValue());
        assertEquals(MOVE, event.getActionType());
        assertEquals(Mw, event.getInitialPosition());
        assertEquals(Aw, event.getTargetPosition());
        assertEquals(SUCCESS, event.getActionOutcome());
    }

    @Test
    public void testPlayerMovementWithBallToAttackingPosition() {
        match.setState(new MatchState(match.getHomeTeam(),
                new Player(14, "Blaise M", new PlayerPosition(M, L))));

        ActionOutcomeDetails movementOutcome = match.executeAction(new Action(MOVE, PitchPosition.A));
        match.applyOutcome(movementOutcome);
        Action nextAction = match.getPossessionPlayer().decideAction();

        assertEquals(SUCCESS, movementOutcome.getActionOutcome());
        assertEquals(A, match.getPossessionPlayer().getPitchPosition());
        assertEquals(SHOT, nextAction.getType());
    }

    @Test
    public void testOffBallPlayerReturningToTacticalPosition() {
        Player player = new Player(12, "Victor W", new PlayerPosition(M, C_L));
        match.setState(new MatchState(match.getHomeTeam(), player, FREE_PLAY));

        ActionOutcomeDetails movementOutcome = match.executeAction(new Action(MOVE, PitchPosition.A));
        match.applyOutcome(movementOutcome);
        Action nextAction = match.getPossessionPlayer().decideAction();
        match.applyOutcome(match.executeAction(nextAction));

        assertEquals(PitchPosition.M, player.getPitchPosition());
    }

    @Test
    public void testMoveWithBallFailure() {
        match.setState(new MatchState(match.getHomeTeam(),
                new Player(12, "Victor W", new PlayerPosition(M, C_L)), FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(MOVE, PitchPosition.M, A, FAIL));

        assertEquals(match.getPossessionTeam(), match.getAwayTeam());
        assertEquals(1, match.getPossessionPlayer().getShirtNumber().intValue());
    }

    @Test
    public void testPassFailure() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(PASS, Dw, PitchPosition.M, FAIL));

        assertEquals(match.getPossessionTeam(), match.getAwayTeam());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 8
                    || match.getPossessionPlayer().getShirtNumber() == 17
                    || match.getPossessionPlayer().getShirtNumber() == 10
                    || match.getPossessionPlayer().getShirtNumber() == 39);
        assertEquals(event.getActionType(), PASS);
        assertEquals(event.getInitialPosition(), Dw);
        assertEquals(event.getTargetPosition(), PitchPosition.M);
        assertEquals(event.getActionOutcome(), FAIL);
    }
}
