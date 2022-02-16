package com.codboxer.finallayouttest.util;

/**
 * @author: Tran Ngoc Man
 * @usage: This class contain path string of Firebase Database
 */

public  class FirebaseStringProvider {
    private static final String CONTROL_PATH = "control";
    private static final String TIMER_PATH = "timer";
    private static final String RELAY1_PATH = "relay-1";
    private static final String RELAY2_PATH = "relay-2";
    private static final String RELAY3_PATH = "relay-3";
    private static final String RELAY4_PATH = "relay-4";
    private static final String NAME_RELAY_PATH = "name";
    private static final String STATUS_RELAY_PATH = "status";

    public static String getControlPath() {
        return CONTROL_PATH;
    }

    public static String getTimerPath() {
        return TIMER_PATH;
    }

    public static String getRelay1Path() {
        return RELAY1_PATH;
    }

    public static String getRelay2Path() {
        return RELAY2_PATH;
    }

    public static String getRelay3Path() {
        return RELAY3_PATH;
    }

    public static String getRelay4Path() {
        return RELAY4_PATH;
    }

    public static String getNameOfRelayPath() {
        return NAME_RELAY_PATH;
    }

    public static String getStatusOfRelayPath() {
        return STATUS_RELAY_PATH;
    }
}
