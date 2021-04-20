package org.easyengine;

import org.easyengine.domain.Team;
import org.easyengine.environment.Environment;

public class Main {
    public static void main(String args[]) {

        Environment.load();

        Team homeTeam = Environment.getTeam("A");
        Team awayTeam = Environment.getTeam("B");
    }
}
