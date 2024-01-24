package de.rapha149.displayutils.display.hologram;

import de.rapha149.displayutils.display.hologram.content.GeneralHologramContentModifier;
import de.rapha149.displayutils.display.hologram.content.PlayerHologramContentModifier;
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
    private final GeneralHologramContentModifier generalModifier;
    private final PlayerHologramContentModifier playerModifier;
    private final Integer updateInterval;
    private final List<UUID> players;
    private final Supplier<List<UUID>> playersSupplier;

    /**
     * Use the {@link HologramBuilder} to create a new Hologram.
     * @param identifier The identifier of the hologram. Must be unique.
     * @param lines The lines of the hologram.
     * @param loc The location of the hologram (the location of the first line).
     * @param generalModifier The general modifier that is used to update the lines of the hologram.
     * @param playerModifier The player specific modifier that is used to update the lines of the hologram.
     * @param updateInterval The interval in ticks in which the content of the hologram is updated. If this is null, the content is not automatically updated.
     * @param players The players that can see the hologram. All other players won't be able to see it. If this is null, all players will be able to see the hologram.
     * @param playersSupplier The supplier that is used to get the players that can see the hologram. All other players won't be able to see it. If this is null, the parameter players is used.
     */
    Hologram(String identifier, List<String> lines, Location loc, GeneralHologramContentModifier generalModifier, PlayerHologramContentModifier playerModifier, Integer updateInterval, List<UUID> players, Supplier<List<UUID>> playersSupplier) {
        this.identifier = identifier;
        this.lines = lines;
        this.loc = loc;
        this.generalModifier = generalModifier;
        this.playerModifier = playerModifier;
        this.updateInterval = updateInterval;
        this.players = players;
        this.playersSupplier = playersSupplier;
    }

    /**
     * @return The unique identifier of the hologram.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return The lines of the hologram.
     */
    public List<String> getLines() {
        return lines;
    }

    /**
     * @return The location of the hologram (the location of the first line).
     */
    public Location getLoc() {
        return loc;
    }

    /**
     * @return The general modifier that is used to update the lines of the hologram.
     */
    public GeneralHologramContentModifier getGeneralModifier() {
        return generalModifier;
    }

    /**
     * @return The player specific modifier that is used to update the lines of the hologram.
     */
    public PlayerHologramContentModifier getPlayerModifier() {
        return playerModifier;
    }

    /**
     * @return Whether the hologram has a player specific modifier.
     */
    public boolean hasPlayerModifier() {
        return playerModifier != null;
    }

    /**
     * @return The interval in ticks in which the content of the hologram is updated. If this is null, the content should not be automatically updated.
     */
    public Integer getUpdateInterval() {
        return updateInterval;
    }

    /**
     * Returns the players that can see the hologram. All other players won't be able to see it. If this is null, all players will be able to see the hologram.
     * This method calls the supplier if it exists, otherwise it returns the players variable.
     *
     * @return The players that can see the hologram.
     */
    public List<UUID> getPlayers() {
        return playersSupplier != null ? playersSupplier.get() : players;
    }
}
