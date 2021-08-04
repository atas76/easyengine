package org.easyengine.util;

import org.easyengine.engine.MatchEvent;

import java.util.List;

public class Logger {

    private static boolean debug;
    private static boolean report;

    public static void setDebug() {
        debug = true;
    }

    public static void setReport() {
        report = true;
    }

    public static void debug(String log) {
        if (debug) {
            System.out.println(log);
        }
    }

    public static void report(List<MatchEvent> events) {
        if (report) {
            events.forEach(System.out::println);
        }
    }

    public static void debugEnd() {
        if (debug) {
            System.out.println();
        }
    }
}
