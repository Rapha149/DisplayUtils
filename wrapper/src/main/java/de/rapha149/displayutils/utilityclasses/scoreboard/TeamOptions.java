package de.rapha149.displayutils.utilityclasses.scoreboard;

import org.bukkit.ChatColor;

/**
 * The options for a team.
 */
public class TeamOptions {

    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.RESET;
    private boolean friendlyFire = true;
    private boolean seeFriendlyInvisibles = true;
    private TeamCollisionRule collisionRule = TeamCollisionRule.ALWAYS;
    private TeamVisibilityOption nameTagVisibility = TeamVisibilityOption.ALWAYS;
    private TeamVisibilityOption deathMessageVisibility = TeamVisibilityOption.ALWAYS;

    /**
     * Use {@link TeamOptionsBuilder} to create a new instance.
     * @param prefix The prefix of the team.
     * @param suffix The suffix of the team.
     * @param color The color of the team.
     * @param friendlyFire Whether the team allows friendly fire.
     * @param seeFriendlyInvisibles Whether the team can see friendly invisibles.
     * @param collisionRule The collision rule of the team.
     * @param nameTagVisibility The name tag visibility option of the team.
     * @param deathMessageVisibility The death message visibility option of the team.
     */
    TeamOptions(String prefix, String suffix, ChatColor color, boolean friendlyFire, boolean seeFriendlyInvisibles, TeamCollisionRule collisionRule, TeamVisibilityOption nameTagVisibility, TeamVisibilityOption deathMessageVisibility) {
        this.prefix = prefix;
        this.suffix = suffix;
        this.color = color;
        this.friendlyFire = friendlyFire;
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        this.collisionRule = collisionRule;
        this.nameTagVisibility = nameTagVisibility;
        this.deathMessageVisibility = deathMessageVisibility;
    }

    /**
     * Gets the prefix of the team.
     *
     * @return The prefix of the team.
     */
    public String getPrefix() {
        return prefix;
    }

    /**
     * Gets the suffix of the team.
     *
     * @return The suffix of the team.
     */
    public String getSuffix() {
        return suffix;
    }

    /**
     * Gets the color of the team.
     *
     * @return The color of the team.
     */
    public ChatColor getColor() {
        return color;
    }

    /**
     * Returns whether the team allows friendly fire.
     *
     * @return True if the team allows friendly fire, false otherwise.
     */
    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    /**
     * Returns whether the team can see friendly invisibles.
     *
     * @return True if the team can see friendly invisibles, false otherwise.
     */
    public boolean isSeeFriendlyInvisibles() {
        return seeFriendlyInvisibles;
    }

    /**
     * Gets the collision rule of the team.
     *
     * @return The collision rule of the team.
     */
    public TeamCollisionRule getCollisionRule() {
        return collisionRule;
    }

    /**
     * Gets the name tag visibility option of the team.
     *
     * @return The name tag visibility option of the team.
     */
    public TeamVisibilityOption getNameTagVisibility() {
        return nameTagVisibility;
    }

    /**
     * Gets the death message visibility option of the team.
     *
     * @return The death message visibility option of the team.
     */
    public TeamVisibilityOption getDeathMessageVisibility() {
        return deathMessageVisibility;
    }
}