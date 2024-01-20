package de.rapha149.displayutils.version;

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