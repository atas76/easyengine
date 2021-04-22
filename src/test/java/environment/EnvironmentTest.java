package environment;

import org.easyengine.domain.Player;
import org.easyengine.domain.Team;
import org.easyengine.environment.Environment;
import org.easyengine.environment.PlayerPosition;
import org.junit.Test;

import static org.easyengine.environment.PlayerPosition.PositionX.M;
import static org.easyengine.environment.PlayerPosition.PositionX.F;
import static org.easyengine.environment.PlayerPosition.PositionX.GK;
import static org.easyengine.environment.PlayerPosition.PositionY.C_L;
import static org.easyengine.environment.PlayerPosition.PositionY.C_R;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class EnvironmentTest {

    @Test
    public void testLoad() {

        Environment.load();

        Team teamA = Environment.getTeam("A");
        Team teamB = Environment.getTeam("B");
        Player GoalkeeperA = teamA.getPlayerByPosition(new PlayerPosition(GK));
        Player RightForwardA = teamA.getPlayerByPosition(new PlayerPosition(F, C_R));
        Player LeftForwardA = teamA.getPlayerByPosition(new PlayerPosition(F, C_L));
        Player GoalkeeperB = teamB.getPlayerByPosition(new PlayerPosition(GK));
        Player MidfielderB = teamB.getPlayerByPosition(new PlayerPosition(M, C_R));

        assertEquals(2, Environment.getTeamCount());
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
    }
}
