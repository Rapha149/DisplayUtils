package de.rapha149.displayutils.display.scoreboard;

import org.bukkit.ChatColor;

/**
 * A builder for {@link TeamOptions}.
 */
public class TeamOptionsBuilder implements Cloneable {

    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.RESET;
    private boolean friendlyFire = true;
    private boolean seeFriendlyInvisibles = true;
    private TeamCollisionRule collisionRule = TeamCollisionRule.ALWAYS;
    private TeamVisibilityOption nameTagVisibility = TeamVisibilityOption.ALWAYS;
    private TeamVisibilityOption deathMessageVisibility = TeamVisibilityOption.ALWAYS;

    /**
     * Sets the prefix of the team.
     * @param prefix The prefix of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    /**
     * Sets the suffix of the team.
     *
     * @param suffix The suffix of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    /**
     * Sets the color of the team.
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
    public TeamOptionsBuilder setCollisionRule(TeamCollisionRule collisionRule) {
        this.collisionRule = collisionRule;
        return this;
    }

    /**
     * Sets the name tag visibility option of the team.
     *
     * @param nameTagVisibility The name tag visibility option of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setNameTagVisibility(TeamVisibilityOption nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
        return this;
    }

    /**
     * Sets the death message visibility option of the team.
     *
     * @param deathMessageVisibility The death message visibility option of the team.
     * @return The {@link TeamOptionsBuilder} instance.
     */
    public TeamOptionsBuilder setDeathMessageVisibility(TeamVisibilityOption deathMessageVisibility) {
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

    @Override
    public TeamOptionsBuilder clone() {
        try {
            TeamOptionsBuilder clone = (TeamOptionsBuilder) super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}