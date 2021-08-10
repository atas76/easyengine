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
    public void testShotOutcomes() {

        Double goalOutcomeIndex = 0.05;
        Double goalKickIndex = 0.2;

        ShotOutcome goalOutcome = ProbabilityModel.getShotOutcome(goalOutcomeIndex);
        ShotOutcome goalKickOutcome = ProbabilityModel.getShotOutcome(goalKickIndex);

        assertEquals(ShotOutcome.GOAL, goalOutcome);
        assertEquals(ShotOutcome.GK, goalKickOutcome);
    }
}
