package de.rapha149.displayutils.utilityclasses.scoreboard;

import org.bukkit.ChatColor;

public class TeamOptions {

    private String prefix = "";
    private String suffix = "";
    private ChatColor color = ChatColor.RESET;
    private boolean friendlyFire = true;
    private boolean seeFriendlyInvisibles = true;
    private TeamCollisionRule collisionRule = TeamCollisionRule.ALWAYS;
    private TeamVisibilityOption nameTagVisibility = TeamVisibilityOption.ALWAYS;
    private TeamVisibilityOption deathMessageVisibility = TeamVisibilityOption.ALWAYS;

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

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    public boolean isSeeFriendlyInvisibles() {
        return seeFriendlyInvisibles;
    }

    public TeamCollisionRule getCollisionRule() {
        return collisionRule;
    }

    public TeamVisibilityOption getNameTagVisibility() {
        return nameTagVisibility;
    }

    public TeamVisibilityOption getDeathMessageVisibility() {
        return deathMessageVisibility;
    }
}
