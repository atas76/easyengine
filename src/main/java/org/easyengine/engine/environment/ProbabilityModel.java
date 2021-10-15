package org.easyengine.engine.environment;

import javafx.util.Pair;
import org.easyengine.engine.ActionType;
import org.easyengine.engine.ShotOutcome;
import org.easyengine.engine.space.PitchPosition;

import static java.util.Map.entry;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.easyengine.engine.ActionType.*;
import static org.easyengine.engine.ShotOutcome.*;
import static org.easyengine.engine.space.PitchPosition.*;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static org.easyengine.engine.space.PitchPosition.GK;
import static org.easyengine.engine.space.PitchPosition.Gkr;

public class ProbabilityModel {

    static Map<Action, Double> actionDistribution = Map.ofEntries(
            // Goalkeeper
            entry(new Action(PASS, Gkr, Dw), 0.27),
            entry(new Action(PASS, Gkr, D), 0.27),
            entry(new Action(PASS, Gkr, Mw), 0.32),
            entry(new Action(PASS, Gkr, M), 0.14),
            // Goalkick
            entry(new Action(PASS, GK, Dw), 0.17),
            entry(new Action(PASS, GK, D), 0.66),
            entry(new Action(PASS, GK, M), 0.17),
            // D W
                // Pass
            entry(new Action(PASS, Dw, Gkr), 0.22),
            entry(new Action(PASS, Dw, D), 0.09),
            entry(new Action(PASS, Dw, Mw), 0.28),
            entry(new Action(PASS, Dw, M), 0.32),
            // entry(new Action(PASS, Dw, A), 0.04),
                // Move
            entry(new Action(MOVE, Dw, Mw), 0.09),
            // D C
                // Pass
            entry(new Action(PASS, D, Gkr), 0.21),
            entry(new Action(PASS, D, Dw), 0.25),
            entry(new Action(PASS, D, Mw), 0.25),
            entry(new Action(PASS, D, M), 0.21),
                // Move
            entry(new Action(MOVE, D, Dw), 0.04),
            entry(new Action(MOVE, D, M), 0.04),
            // M W
                // Pass
            entry(new Action(PASS, Mw, Dw), 0.02),
            entry(new Action(PASS, Mw, D), 0.06),
            entry(new Action(PASS, Mw, M), 0.41),
            entry(new Action(PASS, Mw, A), 0.17),
            entry(new Action(PASS, Mw, Ap), 0.03),
                // Move
            entry(new Action(MOVE, Mw, M), 0.09),
            entry(new Action(MOVE, Mw, Aw), 0.09),
            entry(new Action(MOVE, Mw, A), 0.13),
            // M C
                // Pass
            entry(new Action(PASS, M, Dw), 0.09),
            entry(new Action(PASS, M, D), 0.07),
            entry(new Action(PASS, M, Mw), 0.27),
            entry(new Action(PASS, M, M), 0.02),
            entry(new Action(PASS, M, A), 0.3),
            entry(new Action(PASS, M, Ap), 0.04),
                // Move
            entry(new Action(MOVE, M, Mw), 0.02),
            entry(new Action(MOVE, M, A), 0.17),
            entry(new Action(MOVE, M, Ad), 0.01),
            entry(new Action(MOVE, M, Apw), 0.01),
            // A W
                // Pass
            entry(new Action(PASS, Aw, A), 0.17),
            entry(new Action(PASS, Aw, Awd), 0.05),
                // Move
            entry(new Action(MOVE, Aw, Mw), 0.06),
            entry(new Action(MOVE, Aw, A), 0.23),
            entry(new Action(MOVE, Aw, Ad), 0.11),
            entry(new Action(MOVE, Aw, Awd), 0.05),
            entry(new Action(MOVE, Aw, Apw), 0.05),
                // Cross
            entry(new Action(CROSS, Aw, Aw), 0.05),
            entry(new Action(CROSS, Aw, Ap), 0.05),
            entry(new Action(CROSS, Aw, Ag), 0.12),
                // Shot
            entry(new Action(SHOT, Aw, A), 0.06),
            // A
            entry(new Action(SHOT, A, A), 0.96), // using 'A' pitch position as conventional placeholder for target
            entry(new Action(MOVE, A, Aw), 0.04),
            // Ad
            entry(new Action(MOVE, Ad, Awd), 0.25),
            entry(new Action(SHOT, Ad, A), 0.75),
            // Adw
            entry(new Action(MOVE, Awd, Ap), 1.0), // TODO added by convention - broken data sequence
            // Apw
            entry(new Action(PASS, Apw, Ad), 1.0),
            // Ap
            entry(new Action(SHOT, Ap, A), 1.0),
            // Corner kick
            // entry(new Action(CROSS, C, M), 0.14),
            // entry(new Action(CROSS, C, Gk), 0.07),
            entry(new Action(CROSS, C, A), 1.0)
            // entry(new Action(CROSS, C, Aw), 0.07),
            // entry(new Action(PASS, C, Aw), 0.22)
    );

    static Map<Action, Double> actionSuccessRate = Map.ofEntries(
            // Gkr
            entry(new Action(PASS, Gkr, Dw), 1.0),
            entry(new Action(PASS, Gkr, D), 1.0),
            entry(new Action(PASS, Gkr, Mw), 1.0),
            entry(new Action(PASS, Gkr, M), 1.0),
            // entry(new Action(PASS, Gkr, A), 0.0),
            // GK
            entry(new Action(PASS, GK, Dw), 1.0),
            entry(new Action(PASS, GK, D), 1.0),
            entry(new Action(PASS, GK, M), 1.0),
            // Dw
                // Pass
            entry(new Action(PASS, Dw, Gkr), 0.86),
            entry(new Action(PASS, Dw, D), 1.0),
            entry(new Action(PASS, Dw, Mw), 0.78),
            entry(new Action(PASS, Dw, M), 0.56),
            // entry(new Action(PASS, Dw, A), 0.0),
                // Move
            entry(new Action(MOVE, Dw, Mw), 1.0),
            // D
                // Pass
            entry(new Action(PASS, D, Gkr), 1.0),
            entry(new Action(PASS, D, Dw), 1.0),
            entry(new Action(PASS, D, Mw), 1.0),
            entry(new Action(PASS, D, M), 0.8),
            // entry(new Action(PASS, D, A), 0.00),
                // Move
            entry(new Action(MOVE, D, Dw), 1.0),
            entry(new Action(MOVE, D, M), 1.0),
            // Mw
                // Pass
            entry(new Action(PASS, Mw, Dw), 1.0),
            entry(new Action(PASS, Mw, D), 1.0),
            entry(new Action(PASS, Mw, M), 1.0),
            entry(new Action(PASS, Mw, A), 0.25),
            entry(new Action(PASS, Mw, Ap), 1.0),
                // Move
            entry(new Action(MOVE, Mw, M), 1.0),
            entry(new Action(MOVE, Mw, Aw), 1.0),
            entry(new Action(MOVE, Mw, A), 1.0),
            // M
                // Pass
            entry(new Action(PASS, M, Dw), 1.0),
            entry(new Action(PASS, M, D), 1.0),
            entry(new Action(PASS, M, Mw), 0.87),
            entry(new Action(PASS, M, M), 1.0),
            entry(new Action(PASS, M, A), 0.41),
            entry(new Action(PASS, M, Ap), 1.0),
                // Move
            entry(new Action(MOVE, M, Mw), 1.0),
            entry(new Action(MOVE, M, A), 0.9),
            entry(new Action(MOVE, M, Ad), 1.0),
            entry(new Action(MOVE, M, Apw), 1.0),
            // Aw
                // Pass
            entry(new Action(PASS, Aw, A), 1.0),
            entry(new Action(PASS, Aw, Awd), 1.0),
                // Move
            entry(new Action(MOVE, Aw, Mw), 1.0),
            entry(new Action(MOVE, Aw, A), 0.75),
            entry(new Action(MOVE, Aw, Ad), 1.0),
            entry(new Action(MOVE, Aw, Awd), 1.0),
            entry(new Action(MOVE, Aw, Apw), 1.0),
                // Cross
            // entry(new Action(CROSS, Aw, A), 0.25),
            entry(new Action(CROSS, Aw, Aw), 0.0),
            entry(new Action(CROSS, Aw, Ap), 1.0),
            entry(new Action(CROSS, Aw, Ag), 0.0),
            // A
            entry(new Action(MOVE, A, Aw), 1.0),
            // Corner kick
                // Pass
            // entry(new Action(PASS, C, Aw), 1.0),
                // Cross
            // entry(new Action(CROSS, C, M), 1.0),
            // entry(new Action(CROSS, C, Gkr), 1.0),
            entry(new Action(CROSS, C, A), 0.5),
            // entry(new Action(CROSS, C, Aw), 1.0)
            // Ad
            entry(new Action(MOVE, Ad, Awd), 1.0),
            // Awd
            entry(new Action(MOVE, Awd, Ap), 1.0), // TODO added by convention
            // Apw
            entry(new Action(PASS, Apw, Ad), 1.0)
    );

    // order: Gk, GK, D, Dw, M, Mw, A, C
    static Map<Pair<PitchPosition, PitchPosition>, List<Double>> passFailDistribution = Map.ofEntries(
            // entry(new Pair<>(Gkr, Mw), List.of(0.0, 0.0, 0.0, 0.33, 0.33, 0.34, 0.0, 0.0)),
            // entry(new Pair<>(Gkr, M), List.of(0.0, 0.0, 0.5, 0.0, 0.0, 0.5, 0.0, 0.0)),
            // entry(new Pair<>(Gkr, A), List.of(0.67, 0.0, 0.0, 0.33, 0.0, 0.0, 0.0, 0.0)),
            // entry(new Pair<>(GK, M), List.of(0.0, 0.0, 0.0, 0.5, 0.5, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Dw, Gkr), List.of(0.0, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 1.0)),
            // entry(new Pair<>(Dw, D), List.of(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0)),
            entry(new Pair<>(Dw, Mw), List.of(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0)),
            entry(new Pair<>(Dw, M), List.of(0.0, 0.0, 0.0, 0.0, 0.75, 0.0, 0.25, 0.0)),
            // entry(new Pair<>(Dw, A), List.of(0.5, 0.0, 0.0, 0.0, 0.5, 0.0, 0.0, 0.0)),
            entry(new Pair<>(D, M), List.of(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0)),
            // entry(new Pair<>(D, A), List.of(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0)),
            // entry(new Pair<>(Mw, M), List.of(0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Mw, A), List.of(0.67, 0.0, 0.33, 0.0, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(M, Mw), List.of(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0)),
            entry(new Pair<>(M, A), List.of(0.3, 0.3, 0.4, 0.0, 0.0, 0.0, 0.0, 0.0))
            // entry(new Pair<>(Aw, A), List.of(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
            // entry(new Pair<>(C, A), List.of(0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0))
    );

    // order: Gk, GK, D, Dw, M, Mw, A, C
    static Map<Pair<PitchPosition, PitchPosition>, List<Double>> moveFailDistribution = Map.ofEntries(
            entry(new Pair<>(M, A), List.of(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Aw, A), List.of(0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 0.0))
    );

    static Map<Pair<PitchPosition, PitchPosition>, List<Double>> crossFailDistribution = Map.ofEntries(
            entry(new Pair<>(Aw, Aw), List.of(0.0, 1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Aw, A), List.of(0.75, 0.0, 0.0, 0.25, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(Aw, Ag), List.of(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)),
            entry(new Pair<>(C, A), List.of(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))
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

    public static Double getActionSuccessRate(Action action) {
        return actionSuccessRate.get(action);
    }

    public static Map<Action, Double> getActionOptionsSuccessRates(PitchPosition initialPosition) {
        return actionSuccessRate.entrySet().stream().filter(successRateEntry -> successRateEntry.getKey().getSource() == initialPosition)
                .map(successRateEntry -> entry(successRateEntry.getKey(), successRateEntry.getValue()))
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Double getExpectedChance(PitchPosition pitchPosition) {

        Map<PitchPosition, Double> expectedShots = getExpectedShotsDistribution();
        Double expectedChance = expectedShots.get(pitchPosition);

        return nonNull(expectedChance) ? expectedChance : 0.0;
    }

    private static Map<PitchPosition, Double> getExpectedShotsDistribution() {
        Map<PitchPosition, Double> shotDistribution =
                actionDistribution.entrySet().stream()
                    .filter(actionEntry -> actionEntry.getKey().getType() == SHOT)
                    .map(actionEntry -> entry(actionEntry.getKey().getSource(), actionEntry.getValue()))
                    .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        for (PitchPosition pitchPosition: PitchPosition.values()) {
            shotDistribution.putIfAbsent(pitchPosition, 0.0);
        }

        return shotDistribution;
    }

    public static PitchPosition getFailedOutcomePosition(Pair<PitchPosition, PitchPosition> originalPositions, ActionType actionType) {
        Map<Pair<PitchPosition, PitchPosition>, List<Double>> failDistribution = passFailDistribution;
        if (actionType == MOVE) {
            failDistribution = moveFailDistribution;
        } else if (actionType == CROSS) {
            failDistribution = crossFailDistribution;
        }
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

    static Map<PitchPosition, Map<ShotOutcome, Double>> shotOutcomesDistribution = Map.ofEntries(
                entry(A, Map.ofEntries(
                    entry(GOAL, 0.0),
                    entry(ShotOutcome.GK, 0.54),
                    entry(BLK_C, 0.14),
                    entry(SAVE_C, 0.08),
                    entry(BLK_R_A, 0.08),
                    entry(BLK_Gkr, 0.08),
                    entry(SAVE_R_AD, 0.08),
                    entry(BLK_R_M, 0.0),
                    entry(BLK_R_MW, 0.0),
                    entry(SAVE_R_A, 0.0),
                    entry(SAVE, 0.0))
                ),
                entry(Aw, Map.ofEntries(
                    entry(GOAL, 0.0),
                    entry(ShotOutcome.GK, 0.0),
                    entry(BLK_C, 0.0),
                    entry(SAVE_C, 1.0),
                    entry(BLK_R_A, 0.0),
                    entry(BLK_Gkr, 0.0),
                    entry(SAVE_R_AD, 0.0),
                    entry(BLK_R_M, 0.0),
                    entry(BLK_R_MW, 0.0),
                    entry(SAVE_R_A, 0.0),
                    entry(SAVE, 0.0))
                ),
                entry(Ad, Map.ofEntries(
                    entry(GOAL, 0.0),
                    entry(ShotOutcome.GK, 0.0),
                    entry(BLK_C, 0.33),
                    entry(SAVE_C, 0.34),
                    entry(BLK_R_A, 0.0),
                    entry(BLK_Gkr, 0.0),
                    entry(SAVE_R_AD, 0.0),
                    entry(BLK_R_M, 0.0),
                    entry(BLK_R_MW, 0.0),
                    entry(SAVE_R_A, 0.0),
                    entry(SAVE, 0.33))
                ),
            entry(Ap, Map.ofEntries(
                    entry(GOAL, 0.29),
                    entry(ShotOutcome.GK, 0.29),
                    entry(BLK_C, 0.0),
                    entry(SAVE_C, 0.0),
                    entry(BLK_R_A, 0.14),
                    entry(BLK_Gkr, 0.0),
                    entry(SAVE_R_AD, 0.0),
                    entry(BLK_R_M, 0.0),
                    entry(BLK_R_MW, 0.14),
                    entry(SAVE_R_A, 0.0),
                    entry(SAVE, 0.14))
            )
    );

    public static ShotOutcome getShotOutcome(Double outcomeWeightIndex) {
        return getShotOutcome(A, outcomeWeightIndex);
    }

    public static ShotOutcome getShotOutcome(PitchPosition pitchPosition, Double outcomeWeightIndex) {

        double sum = 0.0;

        Map<ShotOutcome, Double> shotPitchPositionOutcomesDistribution = shotOutcomesDistribution.get(pitchPosition);

        for (ShotOutcome shotOutcome: ShotOutcome.values()) {
            sum += shotPitchPositionOutcomesDistribution.get(shotOutcome);
            if (outcomeWeightIndex < sum) {
                return shotOutcome;
            }
        }
        return null;
    }
}
