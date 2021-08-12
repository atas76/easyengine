package org.easyengine.engine.agent;

import org.easyengine.engine.Action;
import org.easyengine.engine.environment.ProbabilityModel;
import org.easyengine.engine.input.PlayerPosition;
import org.easyengine.engine.space.Pitch;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.util.Config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.engine.ActionType.SHOT;
import static org.easyengine.engine.space.PitchPosition.A;

public class Player extends org.easyengine.engine.input.domain.Player {

    private PitchPosition pitchPosition; // in free play, determined by the default mapping to player tactical position

    public Player(int shirtNumber, String name, PlayerPosition playerPosition) {
        super(shirtNumber, name, playerPosition);
    }

    public Player(org.easyengine.engine.input.domain.Player player) {
        super(player.getShirtNumber(), player.getName(), player.getPlayerPosition());
    }

    public void setPitchPosition(PitchPosition pitchPosition) {
        this.pitchPosition = pitchPosition;
    }

    public PitchPosition getPitchPosition() {
        return nonNull(this.pitchPosition) ? this.pitchPosition : Pitch.mapDefaultPitchPosition(playerPosition);
    }

    public Action decideAction() {
        if (!Config.isAI()) {
            return decideActionDataDriven();
        } else {
            return decideActionAgentDriven();
        }
    }

    private Action decideActionAgentDriven() {

        if (A == this.getPitchPosition()) {
            return new Action(SHOT);
        }

        Map<PitchPosition, Double> successRates = ProbabilityModel.getSuccessRates(getPitchPosition());
        Map<PitchPosition, Double> passTargetUtilityFunction = new HashMap<>();

        successRates.forEach((position, successRate) ->
                passTargetUtilityFunction.put(position, successRate * ProbabilityModel.getExpectedChance(position)));

        PitchPosition targetPosition =
                passTargetUtilityFunction.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toList()).get(0).getKey();

        return new Action(PASS, targetPosition);
    }

    private Action decideActionDataDriven() {

        if (A == this.getPitchPosition()) {
            return new Action(SHOT);
        }

        return new Action(PASS, ProbabilityModel.getTargetPosition(this.getPitchPosition(), new Random().nextDouble()));
    }
}
