package com.amg.double9domino.analytics;

public class Flurry {
    public static final String FORCE_LOSE = "LOSE_AND_QUIT";
    public static final String ID = "Z455RKYR3RDZ649WYSMT";
    public static final String NEW_GAME_CLASSIC = "NEW_GAME_CLASSIC";
    public static final String NEW_GAME_MUGGINS = "NEW_GAME_MUGGINS";
    public static final String NEW_HAND_CLASSIC = "NEW_HAND_CLASSIC";
    public static final String NEW_HAND_MUGGINS = "NEW_HAND_MUGGINS";
    public static final String SHARE_RESULT_CLASSIC = "SHARE_RESULT_CLASSIC";
    public static final String SHARE_RESULT_MUGGINS = "SHARE_RESULT_MUGGINS";

    public enum Event {
        NEW_HAND,
        NEW_GAME,
        SHARE_RESULT
    }
}
