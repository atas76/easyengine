package org.easyengine.environment;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.easyengine.environment.PlayerPosition.PositionX.*;
import static org.easyengine.environment.PlayerPosition.PositionY.*;

public class TacticsDefinition {

    public static Map<Tactics, List<PlayerPosition>> positionMap = new HashMap<>();

    static {
        positionMap.put(Tactics.TACTIC_4_4_2, Arrays.asList(
                new PlayerPosition(D, R),
                new PlayerPosition(D, L),
                new PlayerPosition(D, C_R),
                new PlayerPosition(D, C_L),
                new PlayerPosition(M, C_R),
                new PlayerPosition(M, C_L),
                new PlayerPosition(M, R),
                new PlayerPosition(M, L),
                new PlayerPosition(F, C_R),
                new PlayerPosition(F, C_L)
        ));
    }
}
