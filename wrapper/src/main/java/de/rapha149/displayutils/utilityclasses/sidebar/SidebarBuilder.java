package de.rapha149.displayutils.utilityclasses.sidebar;

import de.rapha149.displayutils.utilityclasses.hologram.HologramBuilder;
import de.rapha149.displayutils.utilityclasses.sidebar.provider.GeneralSidebarContentProvider;
import de.rapha149.displayutils.utilityclasses.sidebar.provider.PlayerSidebarContentProvider;
import de.rapha149.displayutils.utilityclasses.sidebar.provider.SidebarContentProvider;

import java.util.List;
import java.util.Objects;

public class SidebarBuilder {

    private String name;
    private List<String> lines;
    private SidebarContentProvider provider = (GeneralSidebarContentProvider) SidebarPlaceholders::new;
    private boolean playerSpecific = false;
    private Integer updateInterval = null;

    /**
     * Constructs a new SidebarBuilder.
     * @param name The name of the sidebar. Will be shown above the lines.
     * @param lines The lines of the sidebar. May contain placeholder (%placeholder%). If there are more than 15 lines, the last lines will be ignored.
     * @throws java.lang.NullPointerException If the name or the lines are null.
     */
    public SidebarBuilder(String name, List<String> lines) {
        Objects.requireNonNull(name, "The name cannot be null");
        Objects.requireNonNull(lines, "The lines cannot be null");

        this.name = name;
        this.lines = lines;
    }

    /**
     * Sets the provider that is used to get the placeholder values of the sidebar.
     * Use this method if the content of the sidebar is the same for all players.
     * @param provider The provider.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the provider is null.
     * @see #setSpecificPlayerProvider(PlayerSidebarContentProvider)
     */
    public SidebarBuilder setGeneralProvider(GeneralSidebarContentProvider provider) {
        Objects.requireNonNull(provider, "The provider cannot be null");

        this.provider = provider;
        this.playerSpecific = false;
        return this;
    }

    /**
     * Sets the provider that is used to get the placeholder values of the sidebar.
     * Use this method if the content of the sidebar is different for each player.
     * @param provider The provider.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the provider is null.
     * @see #setGeneralProvider(GeneralSidebarContentProvider)
     */
    public SidebarBuilder setSpecificPlayerProvider(PlayerSidebarContentProvider provider) {
        Objects.requireNonNull(provider, "The provider cannot be null");

        this.provider = provider;
        this.playerSpecific = true;
        return this;
    }

    /**
     * Sets the interval in which the content of the sidebar is updated. If this method is not called, the content will not be automatically updated.
     * @param updateInterval The interval in ticks.
     * @return The {@link SidebarBuilder} instance.
     * @throws java.lang.IllegalArgumentException If the update interval is negative.
     */
    public SidebarBuilder setAutomaticUpdates(int updateInterval) {
        if (updateInterval < 0)
            throw new IllegalArgumentException("The update interval cannot be negative");

        this.updateInterval = updateInterval;
        return this;
    }

    public Sidebar build() {
        return new Sidebar(name, lines, provider, playerSpecific, updateInterval);
    }
}