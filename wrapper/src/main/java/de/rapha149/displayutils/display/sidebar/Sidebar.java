package de.rapha149.displayutils.display.sidebar;

import de.rapha149.displayutils.display.sidebar.content.GeneralSidebarContentModifier;
import de.rapha149.displayutils.display.sidebar.content.PlayerSidebarContentProvider;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A class holding information about the sidebar.
 */
public class Sidebar {

    private final String title;
    private final List<String> lines;
    private final GeneralSidebarContentModifier generalModifier;
    private final PlayerSidebarContentProvider playerModifier;
    private final Integer updateInterval;
    private final List<UUID> players;
    private final Supplier<List<UUID>> playersSupplier;

    /**
     * Use the {@link SidebarBuilder} to create a new Sidebar.
     * @param title The title of the sidebar.
     * @param lines The lines of the sidebar. May contain placeholders (%placeholder%).
     * @param generalModifier The general modifier that is used to update the lines of the sidebar.
     * @param playerModifier The player specific modifier that is used to update the lines of the sidebar.
     * @param updateInterval The interval in ticks in which the content of the sidebar is updated. If this is null, the content is not automatically updated.
     * @param players The players that can see the sidebar. All other players won't be able to see it. If this is null, all players will be able to see the sidebar.
     * @param playersSupplier The supplier that is used to get the players that can see the sidebar. All other players won't be able to see it. If this is null, the parameter players is used.
     */
    Sidebar(String title, List<String> lines, GeneralSidebarContentModifier generalModifier, PlayerSidebarContentProvider playerModifier, Integer updateInterval, List<UUID> players, Supplier<List<UUID>> playersSupplier) {
        this.title = title;
        this.lines = lines.size() > 15 ? lines.subList(0, 15) : lines;
        this.generalModifier = generalModifier;
        this.playerModifier = playerModifier;
        this.updateInterval = updateInterval;
        this.players = players;
        this.playersSupplier = playersSupplier;
    }

    /**
     * @return The title of the sidebar.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return The lines of the sidebar. May contain placeholders (%placeholder%).
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * @return The general modifier that is used to update the lines of the sidebar.
     */
    public GeneralSidebarContentModifier getGeneralModifier() {
        return generalModifier;
    }

    /**
     * @return The player specific modifier that is used to update the lines of the sidebar.
     */
    public PlayerSidebarContentProvider getPlayerModifier() {
        return playerModifier;
    }

    /**
     * @return Whether the sidebar has a player specific modifier.
     */
    public boolean hasPlayerModifier() {
        return playerModifier != null;
    }

    /**
     * @return The interval in ticks in which the content of the sidebar is updated. If this is null, the content should not be automatically updated.
     */
    public Integer getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Returns the players that can see the sidebar. All other players won't be able to see it. If this is null, all players will be able to see the sidebar.
     * This method calls the supplier if it exists, otherwise it returns the players variable.
     *
     * @return The players that can see the sidebar.
     */
    public List<UUID> getPlayers() {
        return playersSupplier != null ? playersSupplier.get() : players;
    }
}