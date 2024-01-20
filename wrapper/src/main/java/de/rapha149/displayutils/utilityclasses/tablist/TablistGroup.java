package de.rapha149.displayutils.utilityclasses.tablist;

import de.rapha149.displayutils.utilityclasses.scoreboard.TeamOptions;

import java.util.List;

public class TablistGroup {

    private final String identifier;
    private final TeamOptions teamOptions;
    private final List<String> players;
    private final boolean customPlayerOrder;

    public TablistGroup(String identifier, TeamOptions teamOptions, List<String> players, boolean customPlayerOrder) {
        this.identifier = identifier;
        this.teamOptions = teamOptions;
        this.players = players;
        this.customPlayerOrder = customPlayerOrder;
    }

    public String getIdentifier() {
        return identifier;
    }

    public TeamOptions getTeamOptions() {
        return teamOptions;
    }

    public List<String> getPlayers() {
        return players;
    }

    public boolean isCustomPlayerOrder() {
        return customPlayerOrder;
    }
}
