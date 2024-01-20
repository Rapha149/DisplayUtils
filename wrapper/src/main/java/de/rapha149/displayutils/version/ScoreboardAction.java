package de.rapha149.displayutils.version;

/**
 * An enum for the scoreboard actions used for the scoreboard packets.
 */
public enum ScoreboardAction {
    CREATE(0),
    REMOVE(1),
    UPDATE(2);

    private final int mode;

    ScoreboardAction(int mode) {
        this.mode = mode;
    }

    public int getMode() {
        return mode;
    }
}