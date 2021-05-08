package org.easyengine.domain;

import org.easyengine.environment.PlayerPosition;
import org.easyengine.environment.Tactics;
import org.easyengine.environment.TacticsDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Objects.nonNull;
import static org.easyengine.environment.PlayerPosition.PositionX.Gk;

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
        this.formation.put(player.getPlayerPosition(), player);
    }

    public Player getPlayerByPosition(PlayerPosition position) {
        return formation.get(position);
    }

    public List<Player> getPlayerByPositionX(PlayerPosition.PositionX x) {

        List<Player> players = new ArrayList<>();

        this.formation.keySet().forEach(position -> {
            if (position.getX().equals(x)) {
                players.add(formation.get(position));
            }
        });

        return players;
    }

    public void validateFormation() {
        assert(nonNull(formation.get(new PlayerPosition(Gk))));
        TacticsDefinition.positionMap.get(tactics).forEach(position -> {
            assert(nonNull(formation.get(position)));
        });
    }
}
