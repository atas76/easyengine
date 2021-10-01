package org.easyengine.engine.space;

import org.easyengine.engine.input.PlayerPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.Map.entry;
import static org.easyengine.engine.space.PitchPosition.*;
import static org.easyengine.engine.input.PlayerPosition.PositionX.Gk;
import static org.easyengine.engine.input.PlayerPosition.PositionX.D;
import static org.easyengine.engine.input.PlayerPosition.PositionX.M;
import static org.easyengine.engine.input.PlayerPosition.PositionX.F;
import static org.easyengine.engine.input.PlayerPosition.PositionY.*;

public class Pitch {

    private static Map<PlayerPosition, PitchPosition> defaultPitchPositionMap = Map.ofEntries(
            entry(new PlayerPosition(Gk), PitchPosition.Gkr),
            entry(new PlayerPosition(D, R), Dw),
            entry(new PlayerPosition(D, L), Dw),
            entry(new PlayerPosition(D, C_R), PitchPosition.D),
            entry(new PlayerPosition(D, C_L), PitchPosition.D),
            entry(new PlayerPosition(M, R), Mw),
            entry(new PlayerPosition(M, L), Mw),
            entry(new PlayerPosition(M, C_R), PitchPosition.M),
            entry(new PlayerPosition(M, C_L), PitchPosition.M),
            entry(new PlayerPosition(F, C_R), A),
            entry(new PlayerPosition(F, C_L), A)
    );

    private static Map<PitchPosition, PitchPosition> reversePitchPositionMap = Map.ofEntries(
            entry(PitchPosition.Gkr, PitchPosition.A),
            // entry(PitchPosition.Dw, PitchPosition.Aw),
            entry(PitchPosition.D, PitchPosition.A),
            entry(PitchPosition.Mw, PitchPosition.Mw),
            entry(PitchPosition.M, PitchPosition.M),
            entry(PitchPosition.A, PitchPosition.D)
    );

    private static Map<PitchPosition, List<PlayerPosition>> defaultTacticalPositionMap = Map.ofEntries(
            entry(PitchPosition.Gkr, new ArrayList<>(List.of(new PlayerPosition(Gk)))),
            entry(Dw, new ArrayList<>(List.of(new PlayerPosition(D, R), new PlayerPosition(D, L)))),
            entry(PitchPosition.D, new ArrayList<>(List.of(new PlayerPosition(D, C_R), new PlayerPosition(D, C_L)))),
            entry(Mw, new ArrayList<>(List.of(new PlayerPosition(M, R), new PlayerPosition(M, L)))),
            entry(PitchPosition.M, new ArrayList<>(List.of(new PlayerPosition(M, C_R), new PlayerPosition(M, C_L)))),
            entry(Aw, new ArrayList<>(List.of(
                    // new PlayerPosition(M, R), new PlayerPosition(M, L),
                    new PlayerPosition(F, C_R), new PlayerPosition(F, C_L)
                    ))),
            entry(A, new ArrayList<>(List.of(new PlayerPosition(F, C_R), new PlayerPosition(F, C_L))))
    );

    public static PitchPosition mapDefaultPitchPosition(PlayerPosition playerPosition) {
        return defaultPitchPositionMap.get(playerPosition);
    }

    public static PitchPosition mapDefendingPitchPosition(PitchPosition possessionPosition) {
        return reversePitchPositionMap.get(possessionPosition);
    }

    public static PlayerPosition mapDefaultTacticalPosition(PitchPosition pitchPosition) {
        if (PitchPosition.Gkr == pitchPosition || PitchPosition.GK == pitchPosition) {
            return new PlayerPosition(Gk);
        }
        return defaultTacticalPositionMap.get(pitchPosition).get(new Random().nextInt(2));
    }

    public static PlayerPosition mapDefaultTacticalPositionExcluding(PitchPosition pitchPosition, PlayerPosition playerPosition) {
        return defaultTacticalPositionMap.get(pitchPosition).stream().filter(position -> !position.equals(playerPosition)).findAny().get();
    }
}
