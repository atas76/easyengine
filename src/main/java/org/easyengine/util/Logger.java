package org.easyengine.util;

import org.easyengine.engine.MatchEvent;

import java.util.List;

public class Logger {

    private static boolean debug;
    private static boolean events;

    public static void setDebug() {
        debug = true;
    }

    public static void setEvents() {
        events = true;
    }

    public static void debug(String log) {
        if (debug) {
            System.out.println(log);
        }
    }

    public static void report(List<MatchEvent> events) {
        if (Logger.events) {
            events.forEach(System.out::println);
        }
    }

    public static void debugReport(List<MatchEvent> events) {
        events.forEach(System.out::println);
    }

    public static void debugEnd() {
        if (debug) {
            System.out.println();
        }
    }
}
