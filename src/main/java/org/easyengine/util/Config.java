package org.easyengine.util;

public class Config {

    private static boolean playerArtificialIntelligence = false;

    public static void setAI() {
        playerArtificialIntelligence = true;
    }

    public static boolean isAI() {
        return playerArtificialIntelligence;
    }
}
