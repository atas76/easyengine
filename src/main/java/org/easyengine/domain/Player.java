package org.easyengine.domain;

import org.easyengine.engine.Action;
import org.easyengine.engine.ProbabilityModel;
import org.easyengine.engine.space.Pitch;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.environment.PlayerPosition;

import java.util.Random;

import static org.easyengine.engine.ActionType.PASS;

public class Player {

    private Integer shirtNumber;
    private String name;
    private PlayerPosition playerPosition;
    // private PitchPosition pitchPosition; // TODO currently determined by the default mapping to player tactical position

    public Player(int shirtNumber, String name, PlayerPosition playerPosition) {
        this.shirtNumber = shirtNumber;
        this.name = name;
        this.playerPosition = playerPosition;
    }

    public Integer getShirtNumber() {
        return shirtNumber;
    }

    public String getName() {
        return name;
    }

    public PlayerPosition getPlayerPosition() {
        return playerPosition;
    }

    public PitchPosition getPitchPosition() {
        return Pitch.mapDefaultPitchPosition(playerPosition);
    }

    public Action decideAction() {
        return new Action(PASS, ProbabilityModel.getTargetPosition(this.getPitchPosition(), new Random().nextDouble()));
    }
}
