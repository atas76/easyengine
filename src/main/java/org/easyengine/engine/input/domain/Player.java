package org.easyengine.engine.input.domain;

import org.easyengine.engine.input.PlayerPosition;

public class Player {

    private Integer shirtNumber;
    private String name;
    protected PlayerPosition playerPosition;

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
}
