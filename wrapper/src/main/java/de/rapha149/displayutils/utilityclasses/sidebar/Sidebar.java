package de.rapha149.displayutils.utilityclasses.sidebar;

import de.rapha149.displayutils.utilityclasses.sidebar.provider.SidebarContentProvider;

import java.util.List;

public class Sidebar {

    private final String name;
    private final List<String> lines;
    private final SidebarContentProvider provider;
    private final boolean playerSpecific;
    private final Integer updateInterval;

    Sidebar(String name, List<String> lines, SidebarContentProvider provider, boolean playerSpecific, Integer updateInterval) {
        this.name = name;
        this.lines = lines.size() > 15 ? lines.subList(0, 15) : lines;
        this.playerSpecific = playerSpecific;
        this.provider = provider;
        this.updateInterval = updateInterval;
    }

    public String getName() {
        return name;
    }

    public List<String> getLines() {
        return lines;
    }

    public SidebarContentProvider getProvider() {
        return provider;
    }

    public boolean isPlayerSpecific() {
        return playerSpecific;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }
}
