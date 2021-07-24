package engine;

import org.easyengine.domain.Player;
import org.easyengine.engine.MatchEvent;
import org.easyengine.engine.MatchState;
import org.easyengine.engine.ActionOutcomeDetails;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.environment.Environment;
import org.easyengine.engine.Match;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.engine.BallPlayState.FREE_PLAY;
import static org.easyengine.engine.BallPlayState.KICK_OFF;
import static org.easyengine.engine.ActionOutcome.FAIL;
import static org.easyengine.engine.ActionOutcome.SUCCESS;
import static org.easyengine.engine.space.PitchPosition.D;
import static org.easyengine.engine.space.PitchPosition.Mw;
import static org.easyengine.environment.PlayerPosition.PositionX.M;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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

        MatchEvent event = match.applyOutcome(new ActionOutcomeDetails(PASS, Mw, D, FAIL));

        assertEquals(match.getPossessionTeam(), match.getAwayTeam());
        assertTrue(match.getPossessionPlayer().getShirtNumber() == 10
            || match.getPossessionPlayer().getShirtNumber() == 39);
        assertEquals(event.getActionType(), PASS);
        assertEquals(event.getInitialPosition(), Mw);
        assertEquals(event.getTargetPosition(), D);
        assertEquals(event.getActionOutcome(), FAIL);
    }
}
