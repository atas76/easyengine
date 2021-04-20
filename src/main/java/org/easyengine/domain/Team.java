package org.easyengine.domain;

import org.easyengine.environment.PlayerPosition;
import org.easyengine.environment.Tactics;
import org.easyengine.environment.TacticsDefinition;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.easyengine.environment.PlayerPosition.PositionX.GK;

public class Team {

    private String name;
    private Map<Integer, Player> players = new HashMap<>();
    private Map<PlayerPosition, Player> formation = new HashMap<>();
    private Tactics tactics;

    public Team(String name, Tactics tactics) {
        this.name = name;
        this.tactics = tactics;
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

    public void validateFormation() {
        assert(nonNull(formation.get(new PlayerPosition(GK))));
        TacticsDefinition.positionMap.get(tactics).forEach(position -> {
            assert(nonNull(formation.get(position)));
        });
    }
}
