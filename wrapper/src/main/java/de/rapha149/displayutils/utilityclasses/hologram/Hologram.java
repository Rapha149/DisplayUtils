package de.rapha149.displayutils.utilityclasses.hologram;

import de.rapha149.displayutils.utilityclasses.hologram.provider.HologramContentProvider;
import org.bukkit.Location;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

/**
 * A class holding information about a hologram.
 */
public class Hologram {

    private final String identifier;
    private final List<String> lines;
    private final Location loc;
    private final HologramContentProvider provider;
    private final boolean playerSpecific;
    private final Integer updateInterval;
    private final List<UUID> players;
    private final Supplier<List<UUID>> playersSupplier;

    /**
     * Use the {@link HologramBuilder} to create a new Hologram.
     * @param identifier The identifier of the hologram. Must be unique.
     * @param lines The lines of the hologram. May contain placeholders (%placeholder%).
     * @param loc The location of the hologram (the location of the first line).
     * @param provider The provider that is used to get the content of the hologram.
     * @param playerSpecific Whether the content of the hologram is different for each player.
     * @param updateInterval The interval in ticks in which the content of the hologram is updated. If this is null, the content is not automatically updated.
     * @param players The players that can see the hologram. All other players won't be able to see it. If this is null, all players will be able to see the hologram.
     * @param playersSupplier The supplier that is used to get the players that can see the hologram. All other players won't be able to see it. If this is null, the parameter players is used.
     */
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

    /**
     * Returns the identifier of the hologram.
     * @return The unique identifier of the hologram.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Returns the lines of the hologram.
     * @return The lines of the hologram. May contain placeholders (%placeholder%).
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * Returns the location of the hologram.
     * @return The location of the hologram (the location of the first line).
     */
    public Location getLoc() {
        return loc;
    }

    /**
     * Returns the provider that is used to get the placeholder values of the hologram.
     * @return The provider that is used to get the placeholder values of the hologram.
     */
    public HologramContentProvider getProvider() {
        return provider;
    }

    /**
     * Returns whether the content of the hologram is different for each player.
     * @return True if the content of the hologram is different for each player, false otherwise.
     */
    public boolean isPlayerSpecific() {
        return playerSpecific;
    }

    /**
     * Returns the interval in ticks in which the content of the hologram is updated.
     * @return The interval in ticks in which the content of the hologram is updated. If this is null, the content should not be automatically updated.
     */
    public Integer getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Returns the players that can see the hologram. If playersSupplier is not null, this method calls the supplier.
     * @return The players that can see the hologram. All other players won't be able to see it. If this is null, all players will be able to see the hologram.
     */
    public List<UUID> getPlayers() {
        return playersSupplier != null ? playersSupplier.get() : players;
    }
}
