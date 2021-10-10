package org.easyengine.util;

public class Config {

    private static boolean playerAI = false;

    public static void setAI() {
        playerAI = true;
    }

    public static boolean isAI() {
        return playerAI;
    }
}
