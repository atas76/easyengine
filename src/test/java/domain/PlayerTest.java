package domain;

import org.easyengine.input.domain.Player;
import org.easyengine.engine.Action;
import org.easyengine.input.PlayerPosition;
import org.junit.Test;

import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.input.PlayerPosition.PositionX.M;
import static org.easyengine.input.PlayerPosition.PositionY.R;
import static org.junit.Assert.assertEquals;

public class PlayerTest {

    @Test
    public void testNextActionDecision() {

        Player player = new Player(19, "Luka M", new PlayerPosition(M, R));

        Action action = player.decideAction();

        assertEquals(action.getType(), PASS);
    }
}
