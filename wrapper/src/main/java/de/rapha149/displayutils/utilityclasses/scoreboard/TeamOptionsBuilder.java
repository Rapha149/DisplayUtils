package de.rapha149.displayutils.utilityclasses.scoreboard;

import org.bukkit.ChatColor;

public class TeamOptionsBuilder implements Cloneable {

    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.RESET;
    private boolean friendlyFire = true;
    private boolean seeFriendlyInvisibles = true;
    private TeamCollisionRule collisionRule = TeamCollisionRule.ALWAYS;
    private TeamVisibilityOption nameTagVisibility = TeamVisibilityOption.ALWAYS;
    private TeamVisibilityOption deathMessageVisibility = TeamVisibilityOption.ALWAYS;

    public TeamOptionsBuilder setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public TeamOptionsBuilder setSuffix(String suffix) {
        this.suffix = suffix;
        return this;
    }

    public TeamOptionsBuilder setColor(ChatColor color) {
        this.color = color;
        return this;
    }

    public TeamOptionsBuilder setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        return this;
    }

    public TeamOptionsBuilder setSeeFriendlyInvisibles(boolean seeFriendlyInvisibles) {
        this.seeFriendlyInvisibles = seeFriendlyInvisibles;
        return this;
    }

    public TeamOptionsBuilder setCollisionRule(TeamCollisionRule collisionRule) {
        this.collisionRule = collisionRule;
        return this;
    }

    public TeamOptionsBuilder setNameTagVisibility(TeamVisibilityOption nameTagVisibility) {
        this.nameTagVisibility = nameTagVisibility;
        return this;
    }

    public TeamOptionsBuilder setDeathMessageVisibility(TeamVisibilityOption deathMessageVisibility) {
        this.deathMessageVisibility = deathMessageVisibility;
        return this;
    }

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