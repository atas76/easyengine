package org.easyengine.domain;

import org.easyengine.engine.Action;
import org.easyengine.engine.ProbabilityModel;
import org.easyengine.engine.space.Pitch;
import org.easyengine.engine.space.PitchPosition;
import org.easyengine.environment.PlayerPosition;

import java.util.Random;

import static java.util.Objects.nonNull;
import static org.easyengine.engine.ActionType.PASS;
import static org.easyengine.engine.ActionType.SHOT;
import static org.easyengine.engine.space.PitchPosition.A;

public class Player {

    private Integer shirtNumber;
    private String name;
    private PlayerPosition playerPosition;
    private PitchPosition pitchPosition; // in free play, determined by the default mapping to player tactical position

    public Player(int shirtNumber, String name, PlayerPosition playerPosition) {
        this.shirtNumber = shirtNumber;
        this.name = name;
        this.playerPosition = playerPosition;
    }

    public void setPitchPosition(PitchPosition pitchPosition) {
        this.pitchPosition = pitchPosition;
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
        return nonNull(this.pitchPosition) ? this.pitchPosition : Pitch.mapDefaultPitchPosition(playerPosition);
    }

    public Action decideAction() {

        if (A == this.getPitchPosition()) {
            return new Action(SHOT);
        }

        return new Action(PASS, ProbabilityModel.getTargetPosition(this.getPitchPosition(), new Random().nextDouble()));
    }
}
