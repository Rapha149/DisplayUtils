package de.rapha149.displayutils.utilityclasses.tablist;

import de.rapha149.displayutils.utilityclasses.scoreboard.TeamOptions;

import java.util.List;

/**
 * A class holding information about a tablist group.
 */
public class TablistGroup {

    private final String identifier;
    private final TeamOptions teamOptions;
    private final List<String> players;
    private final boolean customPlayerOrder;

    /**
     * Constructs a new TablistGroup.
     * @param identifier The identifier of the group. This is used to identify the group when updating the tablist.
     * @param teamOptions The {@link TeamOptions} for the team.
     * @param players The players in the group.
     * @param customPlayerOrder Whether the players should be ordered by the order in the list or by their name.
     */
    public TablistGroup(String identifier, TeamOptions teamOptions, List<String> players, boolean customPlayerOrder) {
        this.identifier = identifier;
        this.teamOptions = teamOptions;
        this.players = players;
        this.customPlayerOrder = customPlayerOrder;
    }

    /**
     * Gets the identifier of the group.
     * This is used to identify the group when updating the tablist.
     * @return The identifier of the group.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Gets the TeamOptions for the team.
     * @return The TeamOptions for the team.
     */
    public TeamOptions getTeamOptions() {
        return teamOptions;
    }

    /**
     * Gets the players in the group.
     * @return The players in the group.
     */
    public List<String> getPlayers() {
        return players;
    }

    /**
     * Returns whether the players should be ordered by the order in the list or by their name.
     * @return True if the players should be ordered by the order in the list, false if they should be ordered by their name.
     */
    public boolean isCustomPlayerOrder() {
        return customPlayerOrder;
    }
}
