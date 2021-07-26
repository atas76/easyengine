package org.easyengine.util;

public class Logger {

    private static boolean debug;

    public static void setDebug() {
        debug = true;
    }

    public static void debug(String log) {
        if (debug) {
            System.out.println(log);
        }
    }

    public static void debugEnd() {
        if (debug) {
            System.out.println();
        }
    }
}
