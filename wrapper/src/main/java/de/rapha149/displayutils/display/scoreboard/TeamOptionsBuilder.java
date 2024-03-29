package de.rapha149.displayutils.display.scoreboard;

import org.bukkit.ChatColor;

/**
 * A builder for {@link TeamOptions}.
 */
public class TeamOptionsBuilder {

    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.RESET;
    private boolean friendlyFire = true;
    private boolean seeFriendlyInvisibles = true;
    private TeamOptionStatus collisionRule = TeamOptionStatus.ALWAYS;
    private TeamOptionStatus nameTagVisibility = TeamOptionStatus.ALWAYS;
    private TeamOptionStatus deathMessageVisibility = TeamOptionStatus.ALWAYS;

    /**
     * Sets the prefix of the team.
     * A prefix longer than 16 (up to 1.12) or 64 (since 1.13) characters may result in the player being kicked.
     *
     * @param prefix The prefix of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the suffix of the team.
     * A suffix longer than 16 (up to 1.12) or 64 (since 1.13) characters may result in the player being kicked.
     *
     * @param suffix The suffix of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * Sets the color of the team. <br>
     * Please note that in older versions of Minecraft, this color is not applied to the player name in the tablist.
     * In these versions you have to adjust the final color of the prefix in order to the color of the team in the tablist.
     * However, in newer versions the colors of the prefix have no effect on the player name in the tablist. You have to set the color of the team with this method instead.
     *
     *
     * @param color The color of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setColor(ChatColor color) {
        this.color = color;
        return this;
    }

    /**
     * Sets whether the team allows friendly fire.
     *
     * @param friendlyFire Whether the team allows friendly fire.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        return this;
    }

    /**
     * Sets whether the team can see friendly invisibles.
     *
     * @param seeFriendlyInvisibles Whether the team can see friendly invisibles.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        return this;
    }

    /**
     * Sets the collision rule of the team.
     *
     * @param collisionRule The collision rule of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setCollisionRule(TeamOptionStatus collisionRule) {
        this.collisionRule = collisionRule;
        return this;
    }

    /**
     * Sets the name tag visibility option of the team.
     *
     * @param nameTagVisibility The name tag visibility option of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setNameTagVisibility(TeamOptionStatus nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
        return this;
    }

    /**
     * Sets the death message visibility option of the team.
     *
     * @param deathMessageVisibility The death message visibility option of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setDeathMessageVisibility(TeamOptionStatus deathMessageVisibility) {
        this.deathMessageVisibility = deathMessageVisibility;
        return this;
    }

    /**
     * Builds the TeamOptions.
     * @return The {@link TeamOptions} instance.
     */
    public TeamOptions build() {
        return new TeamOptions(prefix, suffix, color, friendlyFire, seeFriendlyInvisibles, collisionRule, nameTagVisibility, deathMessageVisibility);
    }
}