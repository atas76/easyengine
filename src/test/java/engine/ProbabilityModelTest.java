package engine;

import org.easyengine.engine.ProbabilityModel;
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
}
