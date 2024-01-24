package de.rapha149.displayutils.display.sidebar;

import de.rapha149.displayutils.display.sidebar.provider.SidebarContentProvider;

import java.util.List;

/**
 * A class holding information about the sidebar.
 */
public class Sidebar {

    private final String name;
    private final List<String> lines;
    private final SidebarContentProvider provider;
    private final boolean playerSpecific;
    private final Integer updateInterval;

    /**
     * Use the {@link SidebarBuilder} to create a new Sidebar.
     * @param name The name of the sidebar.
     * @param lines The lines of the sidebar. May contain placeholders (%placeholder%).
     * @param provider The provider that is used to get the content of the sidebar.
     * @param playerSpecific Whether the content of the sidebar is different for each player.
     * @param updateInterval The interval in ticks in which the content of the sidebar is updated. If this is null, the content is not automatically updated.
     */
    Sidebar(String name, List<String> lines, SidebarContentProvider provider, boolean playerSpecific, Integer updateInterval) {
        this.name = name;
        this.lines = lines.size() > 15 ? lines.subList(0, 15) : lines;
        this.playerSpecific = playerSpecific;
        this.provider = provider;
        this.updateInterval = updateInterval;
    }

    /**
     * Returns the name of the sidebar.
     * @return The name of the sidebar.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The lines of the sidebar. May contain placeholders (%placeholder%).
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Returns the provider that is used to get the placeholder values of the sidebar.
     * @return The provider that is used to get the placeholder values of the sidebar.
     */
    public SidebarContentProvider getProvider() {
        return provider;
    }

    /**
     * Returns whether the content of the sidebar is different for each player.
     * @return True if the content of the sidebar is different for each player, false otherwise.
     */
    public boolean isPlayerSpecific() {
        return playerSpecific;
    }

    /**
     * @return The interval in ticks in which the content of the sidebar is updated. If this is null, the content should not be automatically updated.
     */
    public Integer getUpdateInterval() {
        return updateInterval;
    }
}