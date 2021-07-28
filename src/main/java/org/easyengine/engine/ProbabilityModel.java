package org.easyengine.engine;

import javafx.util.Pair;
import org.easyengine.engine.space.PitchPosition;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.easyengine.engine.space.PitchPosition.*;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

import static org.easyengine.engine.space.PitchPosition.Gk;

public class ProbabilityModel {

    static Map<Pair<PitchPosition, PitchPosition>, Double> actionDistribution = Map.ofEntries(
            entry(new Pair<>(Gk, Dw), 0.22),
            entry(new Pair<>(Gk, D), 0.16),
            entry(new Pair<>(Gk, Mw), 0.37),
            entry(new Pair<>(Gk, M), 0.16),
            entry(new Pair<>(Gk, A), 0.09),
            entry(new Pair<>(GK, Dw), 0.125),
            entry(new Pair<>(GK, D), 0.5),
            entry(new Pair<>(GK, M), 0.375),
            entry(new Pair<>(Dw, Gk), 0.21),
            entry(new Pair<>(Dw, D), 0.12),
            entry(new Pair<>(Dw, Mw), 0.26),
            entry(new Pair<>(Dw, M), 0.35),
            entry(new Pair<>(Dw, A), 0.06),
            entry(new Pair<>(D, Gk), 0.19),
            entry(new Pair<>(D, Dw), 0.23),
            entry(new Pair<>(D, Mw), 0.23),
            entry(new Pair<>(D, M), 0.27),
            entry(new Pair<>(D, A), 0.04),
            entry(new Pair<>(D, C), 0.04),
            entry(new Pair<>(Mw, Dw), 0.02),
            entry(new Pair<>(Mw, D), 0.07),
            entry(new Pair<>(Mw, M), 0.54),
            entry(new Pair<>(Mw, A), 0.34),
            entry(new Pair<>(Mw, C), 0.03),
            entry(new Pair<>(M, Dw), 0.09),
            entry(new Pair<>(M, D), 0.07),
            entry(new Pair<>(M, Mw), 0.25),
            entry(new Pair<>(M, A), 0.59),
            entry(new Pair<>(C, Gk), 0.1),
            entry(new Pair<>(C, M), 0.2),
            entry(new Pair<>(C, A), 0.7)
        );

    public static Map<PitchPosition, Double> getTargetsDistribution(PitchPosition source) {
        Map<Pair<PitchPosition, PitchPosition>, Double> distributions = actionDistribution.entrySet().stream()
                .filter(entry -> entry.getKey().getKey() == source)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return distributions.entrySet().stream()
                .map(distEntry -> entry(distEntry.getKey().getValue(), distEntry.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static PitchPosition getTargetPosition(PitchPosition source, double decisionWeightIndex) {
        Map<PitchPosition, Double> targetsDistribution = getTargetsDistribution(source);
        TreeMap<PitchPosition, Double> sortedTargetsDistribution = new TreeMap<>(targetsDistribution);
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        for (Map.Entry<PitchPosition, Double> targetEntry: sortedTargetsDistribution.entrySet()) {
            sum.updateAndGet(v -> v + targetEntry.getValue());
            if (decisionWeightIndex < sum.get()) {
                return targetEntry.getKey();
            }
        }
        return null; // This will be handled at the action execution layer
    }

    static Map<Pair<PitchPosition, PitchPosition>, Double> successRate = Map.ofEntries(
            entry(new Pair<>(Gk, Dw), 1.0),
            entry(new Pair<>(Gk, D), 1.0),
            entry(new Pair<>(Gk, Mw), 0.77),
            entry(new Pair<>(Gk, M), 0.67),
            entry(new Pair<>(Gk, A), 0.0),
            entry(new Pair<>(GK, Dw), 1.0),
            entry(new Pair<>(GK, D), 1.0),
            entry(new Pair<>(GK, M), 0.33),
            entry(new Pair<>(Dw, Gk), 0.86),
            entry(new Pair<>(Dw, D), 0.75),
            entry(new Pair<>(Dw, Mw), 0.67),
            entry(new Pair<>(Dw, M), 0.75),
            entry(new Pair<>(Dw, A), 0.00),
            entry(new Pair<>(D, Gk), 1.00),
            entry(new Pair<>(D, Dw), 1.00),
            entry(new Pair<>(D, Mw), 1.00),
            entry(new Pair<>(D, M), 0.71),
            entry(new Pair<>(D, A), 0.00),
            entry(new Pair<>(D, C), 1.0),
            entry(new Pair<>(Mw, Dw), 1.0),
            entry(new Pair<>(Mw, D), 1.0),
            entry(new Pair<>(Mw, M), 0.95),
            entry(new Pair<>(Mw, A), 0.21),
            entry(new Pair<>(Mw, C), 1.0),
            entry(new Pair<>(M, Dw), 1.0),
            entry(new Pair<>(M, D), 1.0),
            entry(new Pair<>(M, Mw), 0.87),
            entry(new Pair<>(M, A), 0.29),
            entry(new Pair<>(C, Gk), 1.0),
            entry(new Pair<>(C, M), 1.0),
            entry(new Pair<>(C, A), 0.86)
    );

    public static Double getSuccessRate(PitchPosition sourcePosition, PitchPosition targetPosition) {
        return successRate.get(new Pair<>(sourcePosition, targetPosition));
    }

    // order: Gk, GK, D, Dw, M, Mw, A, C
    static Map<Pair<PitchPosition, PitchPosition>, List<Double>> failDistribution = Map.ofEntries(
            entry(new Pair<>(Gk, Mw), List.of(0.0, 0.0, 0.0, 0.33, 0.33, 0.34, 0.0, 0.0)),
            entry(new Pair<>(Gk, M), List.of(0.0, 0.0, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0)),
            entry(new Pair<>(Gk, A), List.of(0.67, 0.0, 0.0, 0.33, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(GK, M), List.of(0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Dw, Gk), List.of(0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 1.0)),
            entry(new Pair<>(Dw, D), List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0)),
            entry(new Pair<>(Dw, Mw), List.of(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0)),
            entry(new Pair<>(Dw, M), List.of(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Dw, A), List.of(0.5, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0)),
            entry(new Pair<>(D, M), List.of(0.0, 0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0)),
            entry(new Pair<>(D, A), List.of(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Mw, M), List.of(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Mw, A), List.of(0.36, 0.1, 0.18, 0.36, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(M, Mw), List.of(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0)),
            entry(new Pair<>(M, A), List.of(0.17, 0.17, 0.21, 0.12, 0.29, 0.04, 0.0, 0.0)),
            entry(new Pair<>(C, A), List.of(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    );

    public static PitchPosition getFailedOutcomePosition(Pair<PitchPosition, PitchPosition> originalPositions) {
        List<Double> positionDistribution = failDistribution.get(originalPositions);
        double outcomeWeightIndex = new Random().nextDouble();
        double sum = 0.0;
        int index = 0;
        for (Double positionProbability: positionDistribution) {
            sum += positionProbability;
            if (outcomeWeightIndex < sum) {
                break;
            }
            ++index;
        }
        return PitchPosition.values()[index];
    }
}
