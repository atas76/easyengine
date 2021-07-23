package org.easyengine.engine.space;

import org.easyengine.environment.PlayerPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Map.entry;
import static org.easyengine.engine.space.Position.*;
import static org.easyengine.environment.PlayerPosition.PositionX.Gk;
import static org.easyengine.environment.PlayerPosition.PositionX.D;
import static org.easyengine.environment.PlayerPosition.PositionX.M;
import static org.easyengine.environment.PlayerPosition.PositionX.F;
import static org.easyengine.environment.PlayerPosition.PositionY.*;

public class Pitch {

    private static Map<PlayerPosition, Position> defaultPitchPositionMap = Map.ofEntries(
            entry(new PlayerPosition(Gk), Position.Gk),
            entry(new PlayerPosition(D, R), Dw),
            entry(new PlayerPosition(D, L), Dw),
            entry(new PlayerPosition(D, C_R), org.easyengine.engine.space.Position.D),
            entry(new PlayerPosition(D, C_L), org.easyengine.engine.space.Position.D),
            entry(new PlayerPosition(M, R), Mw),
            entry(new PlayerPosition(M, L), Mw),
            entry(new PlayerPosition(M, C_R), org.easyengine.engine.space.Position.M),
            entry(new PlayerPosition(M, C_L), org.easyengine.engine.space.Position.M),
            entry(new PlayerPosition(F, C_R), A),
            entry(new PlayerPosition(F, C_L), A)
    );

    private static Map<Position, List<PlayerPosition>> defaultTacticalPositionMap = Map.ofEntries(
            entry(Position.Gk, new ArrayList<>(List.of(new PlayerPosition(Gk)))),
            entry(Dw, new ArrayList<>(List.of(new PlayerPosition(D, R), new PlayerPosition(D, L)))),
            entry(org.easyengine.engine.space.Position.D, new ArrayList<>(List.of(new PlayerPosition(D, C_R), new PlayerPosition(D, C_L)))),
            entry(Mw, new ArrayList<>(List.of(new PlayerPosition(M, R), new PlayerPosition(M, L)))),
            entry(org.easyengine.engine.space.Position.M, new ArrayList<>(List.of(new PlayerPosition(M, C_R), new PlayerPosition(M, C_L)))),
            entry(A, new ArrayList<>(List.of(new PlayerPosition(F, C_R), new PlayerPosition(F, C_L))))
    );

    public static Position mapDefaultPitchPosition(PlayerPosition playerPosition) {
        return defaultPitchPositionMap.get(playerPosition);
    }

    public static PlayerPosition mapDefaultTacticalPosition(Position pitchPosition) {
        return defaultTacticalPositionMap.get(pitchPosition).get(new Random().nextInt(2));
    }
}
