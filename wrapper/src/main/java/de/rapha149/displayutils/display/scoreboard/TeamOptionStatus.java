package de.rapha149.displayutils.display.scoreboard;

/**
 * Represents the collision rule / name tag visibility / death message visibility option of a team.
 */
public enum TeamOptionStatus {

    /**
     * The option is always enabled. <br>
     * This means that <br>
     * - players always collide (overwritten when the other player's team has a different option) <br>
     * - the name tags of the players are always shown <br>
     * - the death messages of the players are always shown
     */
    ALWAYS,

    /**
     * The option is never enabled. <br>
     * This means that <br>
     * - players never collide <br>
     * - the name tags of the players are never shown <br>
     * - the death messages of the players are never shown
     */
    NEVER,

    /**
     * The option is disabled for other teams. <br>
     * This means that <br>
     * - players only collide with their own team <br>
     * - the name tags of the players are only shown their own team <br>
     * - the death messages of the players are only shown for their own team
     */
    OFF_FOR_OTHER_TEAMS,

    /**
     * The option is disabled for the own team. <br>
     * This means that <br>
     * - players only collide with other teams <br>
     * - the name tags of the players are only shown for other teams <br>
     * - the death messages of the players are only shown for other teams
     */
    OFF_FOR_OWN_TEAM
}
