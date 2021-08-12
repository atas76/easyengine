package engine;

import org.easyengine.engine.environment.ProbabilityModel;
import org.easyengine.engine.ShotOutcome;
import org.easyengine.engine.space.PitchPosition;
import org.junit.Test;

import java.util.Map;

import static org.easyengine.engine.space.PitchPosition.*;
import static org.junit.Assert.assertEquals;

public class ProbabilityModelTest {

    @Test
    public void testGetTargetDistributions() {

        Map<PitchPosition, Double> targetDistributions = ProbabilityModel.getTargetsDistribution(Mw);
        Double positionProbability = targetDistributions.get(A);

        assertEquals(0.34, positionProbability, 0.01);
    }

    @Test
    public void testGetPlayerDecision() {

        PitchPosition targetPosition = ProbabilityModel.getTargetPosition(Mw, 0.71);

        assertEquals(A, targetPosition);
    }

    @Test
    public void testGetSuccessRate() {

        Double successRate = ProbabilityModel.getSuccessRate(M, A);

        assertEquals(0.29, successRate, 0.01);
    }

    @Test
    public void testGetSuccessRates() {

        Map<PitchPosition, Double> successRates = ProbabilityModel.getSuccessRates(Mw);

        assertEquals(4, successRates.size());
        assertEquals(0.95, successRates.get(M), 0.01);
    }

    @Test
    public void testExpectedChance() {
        assertEquals(1.0, ProbabilityModel.getExpectedChance(A), 0.01);
        assertEquals(0.29, ProbabilityModel.getExpectedChance(M), 0.01);
        assertEquals(0.21, ProbabilityModel.getExpectedChance(Mw), 0.01);
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
