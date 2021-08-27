package org.easyengine.util;

import java.util.List;

public class Logger {

    private static boolean debug;
    private static boolean events;
    private static boolean info;

    public static void setDebug() {
        debug = true;
    }

    public static void setEvents() {
        events = true;
    }

    public static void setInfo() {
        info = true;
    }

    public static void debug(String log) {
        if (debug) {
            System.out.println(log);
        }
    }

    public static void report(List<String> events) {
        if (Logger.events) {
            events.forEach(System.out::println);
        }
    }

    public static void info(String teamName, String matchInfo) {
        if (info) {
            System.out.println(teamName);
            System.out.println();
            System.out.println(matchInfo);
        }
    }

    public static void infoShotsOnTarget(String homeTeamName, int homeShotsOnTarget, String awayTeamName, int awayShotsOnTarget) {
        if (info) {
            System.out.println("Shots on target");
            System.out.println(
                    homeTeamName + " - " + awayTeamName + " " +
                            homeShotsOnTarget + " - " + awayShotsOnTarget
            );
        }
        // TODO total shots
    }

    public static void debugEnd() {
        if (debug) {
            System.out.println();
        }
    }

    public static void infoEnd() {
        if (info) {
            System.out.println();
        }
    }
 }
