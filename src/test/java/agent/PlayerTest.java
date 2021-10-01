package agent;

import org.easyengine.engine.Action;
import org.easyengine.engine.agent.Player;
import org.easyengine.engine.input.PlayerPosition;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.util.Config;
import org.junit.Test;

import static org.easyengine.engine.ActionType.MOVE;
import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.engine.input.PlayerPosition.PositionX.M;
import static org.easyengine.engine.input.PlayerPosition.PositionY.R;
import static org.easyengine.engine.space.PitchPosition.A;
import static org.easyengine.engine.space.PitchPosition.Aw;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PlayerTest {

    @Test
    public void testNextActionDecision() {
        Player player = new Player(19, "Luka M", new PlayerPosition(M, R));

        Action action = player.decideAction();

        assertTrue(action.getType() == PASS || action.getType() == MOVE);
    }

    @Test
    public void testNextActionDecisionAgentDriven() {
        Config.setAI();
        Player player = new Player(19, "Luka M", new PlayerPosition(M, R));

        Action action = player.decideAction();

        assertEquals(action.getType(), MOVE);
        assertTrue(action.getTarget() == PitchPosition.M ||
                action.getTarget() == Aw ||
                action.getTarget() == A); // Must be a 'tie' currently
    }
}
