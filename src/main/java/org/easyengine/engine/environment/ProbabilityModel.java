package org.easyengine.engine.environment;

import javafx.util.Pair;
import org.easyengine.engine.ShotOutcome;
import org.easyengine.engine.space.PitchPosition;

import static java.util.Map.entry;
import static java.util.stream.Collectors.toMap;
import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.engine.ShotOutcome.*;
import static org.easyengine.engine.space.PitchPosition.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static org.easyengine.engine.space.PitchPosition.GK;
import static org.easyengine.engine.space.PitchPosition.Gk;

public class ProbabilityModel {

    static Map<Action, Double> actionDistribution = Map.ofEntries(
            entry(new Action(PASS, Gk, Dw), 0.22),
            entry(new Action(PASS, Gk, D), 0.16),
            entry(new Action(PASS, Gk, Mw), 0.37),
            entry(new Action(PASS, Gk, M), 0.16),
            entry(new Action(PASS, Gk, A), 0.09),
            entry(new Action(PASS, GK, Dw), 0.125),
            entry(new Action(PASS, GK, D), 0.5),
            entry(new Action(PASS, GK, M), 0.375),
            entry(new Action(PASS, Dw, Gk), 0.21),
            entry(new Action(PASS, Dw, D), 0.12),
            entry(new Action(PASS, Dw, Mw), 0.26),
            entry(new Action(PASS, Dw, M), 0.35),
            entry(new Action(PASS, Dw, A), 0.06),
            entry(new Action(PASS, D, Gk), 0.2),
            entry(new Action(PASS, D, Dw), 0.24),
            entry(new Action(PASS, D, Mw), 0.24),
            entry(new Action(PASS, D, M), 0.28),
            entry(new Action(PASS, D, A), 0.04),
            entry(new Action(PASS, Mw, Dw), 0.025),
            entry(new Action(PASS, Mw, D), 0.075),
            entry(new Action(PASS, Mw, M), 0.55),
            entry(new Action(PASS, Mw, A), 0.35),
            entry(new Action(PASS, M, Dw), 0.09),
            entry(new Action(PASS, M, D), 0.07),
            entry(new Action(PASS, M, Mw), 0.25),
            entry(new Action(PASS, M, A), 0.59),
            entry(new Action(PASS, C, Gk), 0.1),
            entry(new Action(PASS, C, M), 0.2),
            entry(new Action(PASS, C, A), 0.7)
    );

    public static Map<Action, Double> getActionDistribution(PitchPosition source) {
        Map<Action, Double> distributions = actionDistribution.entrySet().stream()
                .filter(entry -> entry.getKey().getSource() == source)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
        return distributions.entrySet().stream()
                .map(distEntry -> entry(distEntry.getKey(), distEntry.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Action getAction(PitchPosition source, double decisionWeightIndex) {
        Map<Action, Double> actionsDistribution = getActionDistribution(source);
        TreeMap<Action, Double> sortedTargetsDistribution = new TreeMap<>(actionsDistribution);
        AtomicReference<Double> sum = new AtomicReference<>(0.0);
        for (Map.Entry<Action, Double> targetEntry: sortedTargetsDistribution.entrySet()) {
            sum.updateAndGet(v -> v + targetEntry.getValue());
            if (decisionWeightIndex < sum.get()) {
                return targetEntry.getKey();
            }
        }
        return null; // This will be handled at the action execution layer
    }

    static Map<PitchPosition, Double> cornerKickRate = Map.ofEntries(
            entry(D, 0.04),
            entry(Mw, 0.02)
    );

    public static Double getCornerKickRate(PitchPosition pitchPosition) {
        return cornerKickRate.get(pitchPosition);
    }

    static Map<Action, Double> actionSuccessRate = Map.ofEntries(
            entry(new Action(PASS, Gk, Dw), 1.0),
            entry(new Action(PASS, Gk, D), 1.0),
            entry(new Action(PASS, Gk, Mw), 0.77),
            entry(new Action(PASS, Gk, M), 0.67),
            entry(new Action(PASS, Gk, A), 0.0),
            entry(new Action(PASS, GK, Dw), 1.0),
            entry(new Action(PASS, GK, D), 1.0),
            entry(new Action(PASS, GK, M), 0.33),
            entry(new Action(PASS, Dw, Gk), 0.86),
            entry(new Action(PASS, Dw, D), 0.75),
            entry(new Action(PASS, Dw, Mw), 0.67),
            entry(new Action(PASS, Dw, M), 0.75),
            entry(new Action(PASS, Dw, A), 0.00),
            entry(new Action(PASS, D, Gk), 1.00),
            entry(new Action(PASS, D, Dw), 1.00),
            entry(new Action(PASS, D, Mw), 1.00),
            entry(new Action(PASS, D, M), 0.71),
            entry(new Action(PASS, D, A), 0.00),
            entry(new Action(PASS, Mw, Dw), 1.0),
            entry(new Action(PASS, Mw, D), 1.0),
            entry(new Action(PASS, Mw, M), 0.95),
            entry(new Action(PASS, Mw, A), 0.21),
            entry(new Action(PASS, M, Dw), 1.0),
            entry(new Action(PASS, M, D), 1.0),
            entry(new Action(PASS, M, Mw), 0.87),
            entry(new Action(PASS, M, A), 0.29),
            entry(new Action(PASS, C, Gk), 1.0),
            entry(new Action(PASS, C, M), 1.0),
            entry(new Action(PASS, C, A), 0.86)
    );

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
            entry(new Pair<>(Mw, Dw), 1.0),
            entry(new Pair<>(Mw, D), 1.0),
            entry(new Pair<>(Mw, M), 0.95),
            entry(new Pair<>(Mw, A), 0.21),
            entry(new Pair<>(M, Dw), 1.0),
            entry(new Pair<>(M, D), 1.0),
            entry(new Pair<>(M, Mw), 0.87),
            entry(new Pair<>(M, A), 0.29),
            entry(new Pair<>(C, Gk), 1.0),
            entry(new Pair<>(C, M), 1.0),
            entry(new Pair<>(C, A), 0.86)
    );

    public static Double getActionSuccessRate(Action action) {
        return actionSuccessRate.get(action);
    }

    public static Map<Action, Double> getActionSuccessRates(PitchPosition initialPosition) {
        return actionSuccessRate.entrySet().stream().filter(successRateEntry -> successRateEntry.getKey().getSource() == initialPosition)
                .map(successRateEntry -> entry(successRateEntry.getKey(), successRateEntry.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Double getExpectedChance(PitchPosition pitchPosition) {

        if (A == pitchPosition) {
            return 1.0;
        }

        final var sourceActionsStream = actionSuccessRate.keySet().stream()
                .filter(action -> action.getSource() == pitchPosition);
        final var sourceActions = sourceActionsStream.collect(Collectors.toList());

        List<Action> targetActions =
                sourceActions.stream()
                        .filter(targetAction -> targetAction.getTarget() == A).collect(Collectors.toList());

        if (!targetActions.isEmpty()) {
            return actionSuccessRate.get(targetActions.get(0));
        }

        return 0.0;
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

    static Map<ShotOutcome, Double> shotOutcomesDistribution = Map.ofEntries(
            entry(GOAL, 0.09),
            entry(ShotOutcome.GK, 0.26),
            entry(BLK_C, 0.13),
            entry(SAVE_C, 0.13),
            entry(BLK_R_A, 0.1),
            entry(BLK_Gkr, 0.04),
            entry(BLK_R_M, 0.04),
            entry(SAVE_R_A, 0.04),
            entry(SAVE, 0.17)
    );

    public static ShotOutcome getShotOutcome(Double outcomeWeightIndex) {

        double sum = 0.0;

        for (ShotOutcome shotOutcome: ShotOutcome.values()) {
            sum += shotOutcomesDistribution.get(shotOutcome);
            if (outcomeWeightIndex < sum) {
                return shotOutcome;
            }
        }
        return null;
    }
}
