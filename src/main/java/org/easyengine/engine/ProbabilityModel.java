package org.easyengine.engine;

import javafx.util.Pair;
import org.easyengine.engine.space.Position;

import static java.util.Map.entry;
import static org.easyengine.engine.space.Position.*;

import java.util.List;
import java.util.Map;

import static org.easyengine.engine.space.Position.Gk;

public class ProbabilityModel {

    static Map<Pair<Position, Position>, Double> actionDistribution = Map.ofEntries(
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
            entry(new Pair<>(M, A), 0.59)
        );

    static Map<Pair<Position, Position>, List<Double>> failDistribution = Map.ofEntries(
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
            entry(new Pair<>(M, A), List.of(0.17, 0.17, 0.21, 0.12, 0.29, 0.04, 0.0, 0.0))
    );
}