package de.rapha149.displayutils.utilityclasses.sidebar;

import de.rapha149.displayutils.utilityclasses.sidebar.provider.GeneralSidebarContentProvider;
import de.rapha149.displayutils.utilityclasses.sidebar.provider.PlayerSidebarContentProvider;
import de.rapha149.displayutils.utilityclasses.sidebar.provider.SidebarContentProvider;

import java.util.List;
import java.util.Objects;

public class SidebarBuilder {

    private String name;
    private List<String> lines;
    private SidebarContentProvider provider;
    private boolean playerSpecific;
    private Integer updateInterval;

    public SidebarBuilder(String name, List<String> lines) {
        this.name = name;
        this.lines = lines;
    }

    public SidebarBuilder useGeneralProvider(GeneralSidebarContentProvider provider) {
        this.provider = provider;
        this.playerSpecific = false;
        return this;
    }

    public SidebarBuilder useSpecificPlayerProvider(PlayerSidebarContentProvider provider) {
        this.provider = provider;
        this.playerSpecific = true;
        return this;
    }

    public SidebarBuilder withAutomaticUpdates(int updateInterval) {
        this.updateInterval = updateInterval;
        return this;
    }

    public Sidebar build() {
        Objects.requireNonNull(provider, "Provider must be set.");
        return new Sidebar(name, lines, provider, playerSpecific, updateInterval);
    }
}