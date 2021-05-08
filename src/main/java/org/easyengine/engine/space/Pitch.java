package org.easyengine.engine.space;

import org.easyengine.environment.PlayerPosition;

import java.util.Map;

import static java.util.Map.entry;
import static org.easyengine.engine.space.Position.*;
import static org.easyengine.environment.PlayerPosition.PositionX.Gk;
import static org.easyengine.environment.PlayerPosition.PositionX.D;
import static org.easyengine.environment.PlayerPosition.PositionX.M;
import static org.easyengine.environment.PlayerPosition.PositionX.F;
import static org.easyengine.environment.PlayerPosition.PositionY.R;
import static org.easyengine.environment.PlayerPosition.PositionY.L;
import static org.easyengine.environment.PlayerPosition.PositionY.C;

public class Pitch {

    private static Map<PlayerPosition, Position> defaultPositionMap = Map.ofEntries(
            entry(new PlayerPosition(Gk), Position.Gk),
            entry(new PlayerPosition(D, R), Dw),
            entry(new PlayerPosition(D, L), Dw),
            entry(new PlayerPosition(D, C), Position.D),
            entry(new PlayerPosition(M, R), Mw),
            entry(new PlayerPosition(M, L), Mw),
            entry(new PlayerPosition(M, C), Position.M),
            entry(new PlayerPosition(F, C), A)
    );

    public static Position mapDefaultPosition(PlayerPosition playerPosition) {
        return defaultPositionMap.get(playerPosition);
    }
}
