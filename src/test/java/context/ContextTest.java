package context;

import org.easyengine.input.domain.Player;
import org.easyengine.input.domain.Team;
import org.easyengine.context.Context;
import org.easyengine.context.PlayerPosition;
import org.junit.Test;

import static org.easyengine.context.PlayerPosition.PositionX.M;
import static org.easyengine.context.PlayerPosition.PositionX.F;
import static org.easyengine.context.PlayerPosition.PositionX.Gk;
import static org.easyengine.context.PlayerPosition.PositionY.*;
import static org.junit.Assert.*;

public class ContextTest {

    @Test
    public void testLoad() {

        Context.load();

        Team teamA = Context.getTeam("A");
        Team teamB = Context.getTeam("B");
        Player GoalkeeperA = teamA.getPlayerByPosition(new PlayerPosition(Gk));
        Player RightForwardA = teamA.getPlayerByPosition(new PlayerPosition(F, C_R));
        Player LeftForwardA = teamA.getPlayerByPosition(new PlayerPosition(F, C_L));
        Player GoalkeeperB = teamB.getPlayerByPosition(new PlayerPosition(Gk));
        Player MidfielderB = teamB.getPlayerByPosition(new PlayerPosition(M, C_R));

        assertEquals(2, Context.getTeamCount());
        assertNotNull(teamA);
        assertNotNull(teamB);
        assertEquals(11, teamA.getPlayersCount());
        assertEquals(11, teamB.getPlayersCount());
        assertNotNull(GoalkeeperA);
        assertEquals(1, GoalkeeperA.getShirtNumber().intValue());
        assertEquals(9, RightForwardA.getShirtNumber().intValue());
        assertEquals(29, LeftForwardA.getShirtNumber().intValue());
        assertEquals(1, GoalkeeperB.getShirtNumber().intValue());
        assertEquals(17, MidfielderB.getShirtNumber().intValue());
        assertEquals("Moussa S", MidfielderB.getName());
        assertTrue(teamA.getCornerKickTakers().contains(teamA.getPlayerByPosition(new PlayerPosition(M, R))));
    }

    @Test(expected=java.lang.AssertionError.class)
    public void testNonExistentPlayerInstructions() {

        Context.load();
        Team teamA = Context.getTeam("A");

        teamA.addCornerKickTaker(15);
    }
}
