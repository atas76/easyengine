package org.easyengine.domain;

import org.easyengine.environment.PlayerPosition;
import org.easyengine.environment.Tactics;
import org.easyengine.environment.TacticsDefinition;

import java.util.*;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.easyengine.environment.PlayerPosition.PositionX.Gk;

public class Team {

    public static class Instructions {

        private List<Integer> cornerKickTakers = new ArrayList<>();

        public List<Integer> getCornerKickTakers() {
            return this.cornerKickTakers;
        }

        public void addCornerKickTaker(Integer shirtNumber) {
            this.cornerKickTakers.add(shirtNumber);
        }
    }

    private String name;
    private Map<Integer, Player> players = new HashMap<>();
    private Map<PlayerPosition, Player> formation = new HashMap<>();
    private Tactics tactics;
    private Instructions teamInstructions = new Instructions();
    private int goalsScored = 0;

    public Team(String name, Tactics tactics) {
        this.name = name;
        this.tactics = tactics;
    }

    public int getGoalsScored() {
        return this.goalsScored;
    }

    public String getName() {
        return name;
    }

    public int getPlayersCount() {
        return players.size();
    }

    public void addPlayer(Player player) {
        this.players.put(player.getShirtNumber(), player);
        this.formation.put(player.getPlayerPosition(), player);
    }

    public void addCornerKickTaker(Integer shirtNumber) {
        assert(players.containsKey(shirtNumber));
        teamInstructions.addCornerKickTaker(shirtNumber);
    }

    public void score() {
        ++this.goalsScored;
    }

    public List<Player> getCornerKickTakers() {
        return teamInstructions.getCornerKickTakers().stream().map(
                shirtNumber -> this.players.get(shirtNumber)).collect(toList());
    }

    public Player getRandomTaker(List<Player> takers) {
        return takers.get(new Random().nextInt(takers.size()));
    }

    public Player getPlayerByPosition(PlayerPosition position) {
        return formation.get(position);
    }

    public Player getGoalkeeper() {
        return getPlayerByPosition(new PlayerPosition(Gk));
    }

    public List<Player> getPlayersByPositionX(PlayerPosition.PositionX x) {

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
