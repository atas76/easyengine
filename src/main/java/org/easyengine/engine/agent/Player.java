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

        Map<org.easyengine.engine.environment.Action, Double> successRates = ProbabilityModel.getActionOptionsSuccessRates(getPitchPosition());
        Map<org.easyengine.engine.environment.Action, Double> actionTargetUtilityFunction = new HashMap<>();

        successRates.forEach((action, successRate) ->
                actionTargetUtilityFunction.put(action, successRate * ProbabilityModel.getExpectedChance(action.getTarget())));

        Action targetAction =
                actionTargetUtilityFunction.entrySet().stream()
                        .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                        .collect(Collectors.toList()).get(0).getKey();

        return new Action(targetAction.getType(), targetAction.getTarget());
    }

    private Action decideActionDataDriven() {

        Action nextAction = ProbabilityModel.getAction(this.getPitchPosition(), new Random().nextDouble());

        assert nextAction != null;
        return new Action(nextAction.getType(), nextAction.getTarget());
    }
}
