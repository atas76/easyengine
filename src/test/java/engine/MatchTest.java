package engine;

import org.easyengine.domain.Player;
import org.easyengine.engine.*;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.environment.Environment;
import org.easyengine.environment.PlayerPosition;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Arrays;

import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.engine.ActionOutcome.FAIL;
import static org.easyengine.engine.ActionOutcome.SUCCESS;
import static org.easyengine.engine.ActionType.SHOT;
import static org.easyengine.engine.BallPlayState.*;
import static org.easyengine.engine.ShotOutcome.GOAL;
import static org.easyengine.engine.space.PitchPosition.*;
import static org.easyengine.environment.PlayerPosition.PositionX.M;
import static org.junit.Assert.*;

public class MatchTest {

    private static Match match;

    @BeforeClass
    public static void setUp() {
        Environment.load();
        match = new Match(Environment.getTeam("A"), Environment.getTeam("B"));
    }

    @Test
    public void testKickOff() {
        match.playCurrentCycle(new MatchState(Environment.getTeam("A"), null, KICK_OFF));
        Player player = match.getPossessionPlayer();
        PitchPosition pitchPosition = player.getPitchPosition();
        assertEquals(M, player.getPlayerPosition().getX());
        assertTrue(pitchPosition == PitchPosition.M || pitchPosition == Mw);
        assertEquals(FREE_PLAY, match.getBallPlayState());
    }

    @Test
    public void testCornerKickOutcome() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(PASS, Mw, C, SUCCESS));

        assertEquals(CORNER_KICK, match.getBallPlayState());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 19 ||
                match.getPossessionPlayer().getShirtNumber() == 14);
    }

    @Test
    public void testGoalKickExecution() {
        Player taker = match.getHomeTeam().getPlayerByPosition(new PlayerPosition(PlayerPosition.PositionX.Gk));
        match.setState(new MatchState(match.getHomeTeam(), taker, GOAL_KICK));

        match.applyOutcome(match.executeAction(new Action(PASS, PitchPosition.M)));

        assertNotSame(GOAL_KICK, match.getBallPlayState());
        assertNotSame(taker, match.getPossessionPlayer());
        assertNotSame(PitchPosition.GK, match.getPossessionPlayer().getPitchPosition());
    }

    @Test
    public void testCornerKickExecution() {
        Player taker = match.getHomeTeam().getRandomTaker(match.getHomeTeam().getCornerKickTakers());
        match.setState(new MatchState(match.getHomeTeam(), taker, CORNER_KICK));

        match.applyOutcome(match.executeAction(new Action(PASS, A)));

        assertNotSame(CORNER_KICK, match.getBallPlayState());
        assertNotSame(taker, match.getPossessionPlayer());
    }

    @Test
    public void testGoalScored() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, GOAL));

        assertEquals(1, match.getHomeTeam().getGoalsScored());
        assertNotSame(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(KICK_OFF, match.getBallPlayState());
    }

    @Test
    public void testShotOffTarget() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.GK));

        assertNotSame(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(GOAL_KICK, match.getBallPlayState());
        assertEquals(1, match.getPossessionPlayer().getShirtNumber().intValue());
    }

    @Test
    public void testCornerKickAfterShotBlocked() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        match.applyOutcome(new ActionOutcomeDetails(SHOT, A, ShotOutcome.BLK_C));

        assertEquals(match.getHomeTeam(), match.getPossessionTeam());
        assertEquals(CORNER_KICK, match.getBallPlayState());
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
    }

    @Test
    public void testShotDecision() {
        Player player = match.getHomeTeam().getPlayerByPosition(new PlayerPosition(PlayerPosition.PositionX.F, PlayerPosition.PositionY.C_R));
        match.setState(new MatchState(match.getHomeTeam(), player, FREE_PLAY));

        Action action = player.decideAction();

        assertEquals(SHOT, action.getType());
    }

    @Test
    public void testPassSuccess() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(PASS, Mw, D, SUCCESS));

        assertEquals(match.getPossessionTeam(), match.getHomeTeam());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 4
                || match.getPossessionPlayer().getShirtNumber() == 6);
        assertEquals(event.getActionType(), PASS);
        assertEquals(event.getInitialPosition(), Mw);
        assertEquals(event.getTargetPosition(), D);
        assertEquals(event.getActionOutcome(), SUCCESS);
    }

    @Test
    public void testPassFailure() {
        match.setState(new MatchState(match.getHomeTeam(), null, FREE_PLAY));

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(PASS, Dw, PitchPosition.M, FAIL));

        assertEquals(match.getPossessionTeam(), match.getAwayTeam());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 8
            || match.getPossessionPlayer().getShirtNumber() == 17);
        assertEquals(event.getActionType(), PASS);
        assertEquals(event.getInitialPosition(), Dw);
        assertEquals(event.getTargetPosition(), PitchPosition.M);
        assertEquals(event.getActionOutcome(), FAIL);
    }
}
