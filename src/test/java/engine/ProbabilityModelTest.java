package engine;

import org.easyengine.engine.environment.Action;
import org.easyengine.engine.environment.ProbabilityModel;
import org.easyengine.engine.ShotOutcome;
import org.easyengine.engine.space.PitchPosition;
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

        assertEquals(0.17, actionProbability, 0.01);
    }

    @Test
    public void testGetPlayerAction() {

        Action action = ProbabilityModel.getAction(Mw, 0.65);

        assert(action != null);
        assertSame(A, action.getTarget());
        assertSame(PASS, action.getType());
    }

    @Test
    public void testGetActionSuccessRate() {
        assertEquals(0.41, ProbabilityModel.getActionSuccessRate(new Action(PASS, M, A)), 0.01);
    }

    @Test
    public void testGetActionSuccessRates() {

        Map<Action, Double> actionSuccessRates = ProbabilityModel.getActionOptionsSuccessRates(Mw);

        assertEquals(8, actionSuccessRates.size());
        assertEquals(1.0, actionSuccessRates.get(new Action(PASS, Mw, M)), 0.01);
    }

    @Test
    public void testExpectedChance() {
        assertEquals(1.0, ProbabilityModel.getExpectedChance(A), 0.01);
        assertEquals(0.9, ProbabilityModel.getExpectedChance(M), 0.01);
        assertEquals(1.0, ProbabilityModel.getExpectedChance(Mw), 0.01);
        assertEquals(0.0, ProbabilityModel.getExpectedChance(D), 0.01);
        assertEquals(0.0, ProbabilityModel.getExpectedChance(Dw), 0.01);
    }

    @Test
    public void testShotOutcomes() {
        Double goalOutcomeIndex = 0.05;
        Double goalKickIndex = 0.2;

        ShotOutcome goalOutcome = ProbabilityModel.getShotOutcome(Ap, goalOutcomeIndex);
        ShotOutcome goalKickOutcome = ProbabilityModel.getShotOutcome(goalKickIndex);

        assertEquals(ShotOutcome.GOAL, goalOutcome);
        assertEquals(ShotOutcome.GK, goalKickOutcome);
    }

    @Test
    public void testGetExpectedShotsDistribution() {

        Map<PitchPosition, Double> expectedShotsDistribution = ProbabilityModel.getExpectedShotsDistribution();

        assertEquals(1.0, expectedShotsDistribution.get(Ap), 0.1);
        assertEquals(0.75, expectedShotsDistribution.get(Ad), 0.1);
        assertEquals(0.96, expectedShotsDistribution.get(A), 0.1);
        assertEquals(0.06, expectedShotsDistribution.get(Aw), 0.1);
        assertEquals(0.0, expectedShotsDistribution.get(M), 0.1);
    }
}
