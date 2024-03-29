package de.rapha149.displayutils.version;

/**
 * An enum for the scoreboard actions. Used for the display objective packet.
 */
public enum ScoreboardPosition {

    LIST(0),
    SIDEBAR(1),
    BELOW_NAME(2);

    private final int position;

    ScoreboardPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
