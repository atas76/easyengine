package org.easyengine.domain;

import org.easyengine.environment.PlayerPosition;

public class Player {

    private Integer shirtNumber;
    private String name;
    private PlayerPosition position;

    public Player(int shirtNumber, String name, PlayerPosition position) {
        this.shirtNumber = shirtNumber;
        this.name = name;
        this.position = position;
    }

    public Integer getShirtNumber() {
        return shirtNumber;
    }

    public String getName() {
        return name;
    }

    public PlayerPosition getPosition() {
        return position;
    }
}
