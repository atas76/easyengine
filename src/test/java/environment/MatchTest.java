package environment;

import org.easyengine.domain.Player;
import org.easyengine.engine.MatchState;
import org.easyengine.engine.space.Position;
import org.easyengine.environment.Environment;
import org.easyengine.engine.Match;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.easyengine.engine.BallPlayState.KICK_OFF;
import static org.easyengine.engine.space.Position.Mw;
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
    public void testKickOffFullMatch() {
        match.play();
        Player player = match.getPossessionPlayer();
        Position pitchPosition = player.getPitchPosition();

        assertEquals(M, player.getPlayerPosition().getX());
        assertTrue(pitchPosition == Position.M || pitchPosition == Mw);
    }

    @Test
    public void testKickOff() {
        match.playCurrentCycle(new MatchState(Environment.getTeam("A"), null, KICK_OFF));
        Player player = match.getPossessionPlayer();
        Position pitchPosition = player.getPitchPosition();
        assertEquals(M, player.getPlayerPosition().getX());
        assertTrue(pitchPosition == Position.M || pitchPosition == Mw);
    }
}
