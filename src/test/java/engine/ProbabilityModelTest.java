package engine;

import org.easyengine.engine.environment.Action;
import org.easyengine.engine.environment.ProbabilityModel;
import org.easyengine.engine.ShotOutcome;
import org.junit.Test;

import java.util.Map;

import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.engine.space.PitchPosition.*;
import static org.junit.Assert.*;

public class ProbabilityModelTest {

    @Test
    public void testGetActionDistributions() {

        Map<Action, Double> actionDistributions = ProbabilityModel.getActionDistribution(Mw);
        Double actionProbability = actionDistributions.get(new Action(PASS, Mw, A));

        assertEquals(0.31, actionProbability, 0.01);
    }

    @Test
    public void testGetPlayerAction() {

        Action action = ProbabilityModel.getAction(Mw, 0.71);

        assert(action != null);
        assertSame(A, action.getTarget());
        assertSame(PASS, action.getType());
    }

    @Test
    public void testGetActionSuccessRate() {
        assertEquals(0.29, ProbabilityModel.getActionSuccessRate(new Action(PASS, M, A)), 0.01);
    }

    @Test
    public void testGetActionSuccessRates() {

        Map<Action, Double> actionSuccessRates = ProbabilityModel.getActionOptionsSuccessRates(Mw);

        assertEquals(6, actionSuccessRates.size());
        assertEquals(0.95, actionSuccessRates.get(new Action(PASS, Mw, M)), 0.01);
    }

    @Test
    public void testExpectedChance() {
        assertEquals(1.0, ProbabilityModel.getExpectedChance(A), 0.01);
        assertEquals(0.83, ProbabilityModel.getExpectedChance(M), 0.01);
        assertEquals(1.0, ProbabilityModel.getExpectedChance(Mw), 0.01);
        assertEquals(0.0, ProbabilityModel.getExpectedChance(D), 0.01);
        assertEquals(0.0, ProbabilityModel.getExpectedChance(Dw), 0.01);
    }

    @Test
    public void testShotOutcomes() {

        Double goalOutcomeIndex = 0.05;
        Double goalKickIndex = 0.2;

        ShotOutcome goalOutcome = ProbabilityModel.getShotOutcome(goalOutcomeIndex);
        ShotOutcome goalKickOutcome = ProbabilityModel.getShotOutcome(goalKickIndex);

        assertEquals(ShotOutcome.GOAL, goalOutcome);
        assertEquals(ShotOutcome.GK, goalKickOutcome);
    }
}
