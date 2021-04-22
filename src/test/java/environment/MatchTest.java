package environment;

import org.easyengine.environment.Environment;
import org.easyengine.environment.Match;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.easyengine.environment.PlayerPosition.PositionX.M;
import static org.junit.Assert.assertEquals;

public class MatchTest {

    private static Match match;

    @BeforeClass
    public static void setUp() {
        Environment.load();
        match = new Match(Environment.getTeam("A"), Environment.getTeam("B"));
    }

    @Test
    public void testKickOff() {
        match.play();
        // TODO distinguish between in-play position and nominal position
        assertEquals(M, match.getPossessionPlayer().getPosition().getX());
    }
}
