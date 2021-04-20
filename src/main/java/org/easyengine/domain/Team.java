package org.easyengine.domain;

import org.easyengine.environment.PlayerPosition;

import java.util.HashMap;
import java.util.Map;

public class Team {

    private String name;
    private Map<Integer, Player> players = new HashMap<>();
    private Map<PlayerPosition, Player> formation = new HashMap<>();

    public Team(String name) {
        this.name = name;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public void addPlayer(Player player) {
        this.players.put(player.getShirtNumber(), player);
        this.formation.put(player.getPosition(), player);
    }

    public Player getPlayersByPosition(PlayerPosition position) {
        return formation.get(position);
    }
}
