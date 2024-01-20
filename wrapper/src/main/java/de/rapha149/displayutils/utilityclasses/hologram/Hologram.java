package de.rapha149.displayutils.utilityclasses.hologram;

import de.rapha149.displayutils.utilityclasses.hologram.provider.HologramContentProvider;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

public class Hologram {

    private final String identifier;
    private final List<String> lines;
    private final Location loc;
    private final HologramContentProvider provider;
    private final boolean playerSpecific;
    private final Integer updateInterval;
    private final List<UUID> players;
    private final Supplier<List<UUID>> playersSupplier;

    Hologram(String identifier, List<String> lines, Location loc, HologramContentProvider provider, boolean playerSpecific, Integer updateInterval, List<UUID> players, Supplier<List<UUID>> playersSupplier) {
        this.identifier = identifier;
        this.lines = lines;
        this.loc = loc;
        this.playerSpecific = playerSpecific;
        this.provider = provider;
        this.updateInterval = updateInterval;
        this.players = players;
        this.playersSupplier = playersSupplier;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getLines() {
        return lines;
    }

    public Location getLoc() {
        return loc;
    }

    public HologramContentProvider getProvider() {
        return provider;
    }

    public boolean isPlayerSpecific() {
        return playerSpecific;
    }

    public Integer getUpdateInterval() {
        return updateInterval;
    }

    public List<UUID> getPlayers() {
        return playersSupplier != null ? playersSupplier.get() : players;
    }
}
