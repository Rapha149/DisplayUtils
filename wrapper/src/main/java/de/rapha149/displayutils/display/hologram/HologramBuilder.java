package de.rapha149.displayutils.display.hologram;

import de.rapha149.displayutils.display.hologram.provider.GeneralHologramContentProvider;
import de.rapha149.displayutils.display.hologram.provider.HologramContentProvider;
import de.rapha149.displayutils.display.hologram.provider.PlayerHologramContentProvider;
import org.bukkit.Location;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;

public class HologramBuilder {

    private String identifier;
    private List<String> lines;
    private Location loc;
    private HologramContentProvider provider = (GeneralHologramContentProvider) HologramPlaceholders::new;
    private boolean playerSpecific = false;
    private Integer updateInterval = null;
    private List<UUID> players = null;
    private Supplier<List<UUID>> playersSupplier = null;

    /**
     * Constructs a new HologramBuilder.
     * @param identifier The identifier of the hologram. Must be unique.
     * @param lines The lines of the hologram. May contain placeholders (%placeholder%).
     * @param loc The location of the hologram (the location of the first line).
     * @throws java.lang.NullPointerException If the identifier, lines or loc is null.
     */
    public HologramBuilder(String identifier, List<String> lines, Location loc) {
        Objects.requireNonNull(identifier, "The identifier cannot be null");
        Objects.requireNonNull(lines, "The lines cannot be null");
        Objects.requireNonNull(loc, "The location cannot be null");

        this.identifier = identifier;
        this.lines = lines;
        this.loc = loc;
    }

    /**
     * Sets the provider that is used to get the placeholder values of the hologram.
     * Use this method if the content of the hologram is the same for all players.
     * @param provider The provider.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the provider is null.
     * @see #setSpecificPlayerProvider(PlayerHologramContentProvider)
     */
    public HologramBuilder setGeneralProvider(GeneralHologramContentProvider provider) {
        Objects.requireNonNull(provider, "The provider cannot be null");

        this.provider = provider;
        this.playerSpecific = false;
        return this;
    }

    /**
     * Sets the provider that is used to get the placeholder values of the hologram.
     * Use this method if the content of the hologram is different for each player.
     * @param provider The provider.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the provider is null.
     * @see #setGeneralProvider(GeneralHologramContentProvider)
     */
    public HologramBuilder setSpecificPlayerProvider(PlayerHologramContentProvider provider) {
        Objects.requireNonNull(provider, "The provider cannot be null");

        this.provider = provider;
        this.playerSpecific = true;
        return this;
    }

    /**
     * Sets the interval in which the content of the hologram is updated. If this method is not called, the content will not be automatically updated.
     * @param updateInterval The interval in ticks.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.IllegalArgumentException If the update interval is negative.
     */
    public HologramBuilder setAutomaticUpdates(int updateInterval) {
        if (updateInterval < 0)
            throw new IllegalArgumentException("The update interval cannot be negative");

        this.updateInterval = updateInterval;
        return this;
    }

    /**
     * Sets the players that can see the hologram. All other players won't be able to see it.
     * @param players The players that can see the hologram.
     * @return The {@link HologramBuilder} instance.
     * @see #setPlayers(Supplier)
     */
    public HologramBuilder setPlayers(List<UUID> players) {
        this.players = players;
        return this;
    }

    /**
     * Sets a supplier that is used to get the players that can see the hologram. All other players won't be able to see it.
     * The supplier will be called every time the hologram is updated.
     * This overrides the players that are set with {@link #setPlayers(List)}.
     *
     * @param playersSupplier The supplier that is used to get the players that can see the hologram.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the players supplier is null.
     */
    public HologramBuilder setPlayers(Supplier<List<UUID>> playersSupplier) {
        Objects.requireNonNull(playersSupplier, "The players supplier cannot be null");

        this.playersSupplier = playersSupplier;
        return this;
    }

    /**
     * Builds the hologram.
     * @return The hologram.
     */
    public Hologram build() {
        return new Hologram(identifier, lines, loc, provider, playerSpecific, updateInterval, players, playersSupplier);
    }
}