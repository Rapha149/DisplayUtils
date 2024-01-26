package de.rapha149.displayutils.display.hologram;

import de.rapha149.displayutils.display.hologram.content.GeneralHologramContentModifier;
import de.rapha149.displayutils.display.hologram.content.PlayerHologramContentModifier;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HologramBuilder {

    private String identifier;
    private List<String> lines;
    private Location loc;
    private HologramVerticalAlignment verticalAlignment = HologramVerticalAlignment.CENTER;
    private GeneralHologramContentModifier generalModifier;
    private PlayerHologramContentModifier playerModifier;
    private Integer updateInterval = null;
    private List<UUID> players = null;
    private Supplier<List<UUID>> playerSupplier = null;

    /**
     * Constructs a new HologramBuilder.
     * @param identifier The identifier of the hologram. Must be unique.
     * @param lines The lines of the hologram.
     * @param loc The location of the hologram (the location of the first line).
     * @throws java.lang.NullPointerException If the identifier, lines or loc is null.
     * @throws java.lang.IllegalArgumentException If the lines are empty.
     */
    public HologramBuilder(String identifier, List<String> lines, Location loc) {
        Objects.requireNonNull(identifier, "The identifier cannot be null");
        Objects.requireNonNull(lines, "The lines cannot be null");
        Objects.requireNonNull(loc, "The location cannot be null");
        if (lines.isEmpty())
            throw new IllegalArgumentException("The lines cannot be empty");

        this.identifier = identifier;
        this.lines = Collections.unmodifiableList(lines.stream().map(line -> line != null ? line : "").collect(Collectors.toList()));
        this.loc = loc;
    }

    /**
     * Sets the vertical alignment of the hologram. <br>
     * The default is CENTER.
     *
     * @param verticalAlignment The vertical alignment.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the verticalAlignment is null.
     * @see HologramVerticalAlignment
     */
    public HologramBuilder setVerticalAlignment(HologramVerticalAlignment verticalAlignment) {
        Objects.requireNonNull(verticalAlignment, "The vertical alignment cannot be null");

        this.verticalAlignment = verticalAlignment;
        return this;
    }

    /**
     * Sets the modifier that can be used to update the lines of the hologram without re-adding it. <br>
     * Use this method if the content of the hologram is the same for all players.
     * @param generalModifier The modifier.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the modifier is null.
     * @see #setPlayerContentModifier(PlayerHologramContentModifier)
     */
    public HologramBuilder setGeneralContentModifier(GeneralHologramContentModifier generalModifier) {
        Objects.requireNonNull(generalModifier, "The modifier cannot be null");

        this.generalModifier = generalModifier;
        return this;
    }

    /**
     * Sets the modifier that can be used to update the lines of the hologram without re-adding it.
     * Use this method if the content of the hologram is different for each player.
     * @param playerModifier The modifier.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the playerModifier is null.
     * @see #setGeneralContentModifier(GeneralHologramContentModifier)
     */
    public HologramBuilder setPlayerContentModifier(PlayerHologramContentModifier playerModifier) {
        Objects.requireNonNull(playerModifier, "The modifier cannot be null");

        this.playerModifier = playerModifier;
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
     * @see #setPlayerUUIDs(List)
     * @see #setPlayerSupplier(Supplier)
     * @see #setPlayerUUIDSupplier(Supplier) 
     */
    public HologramBuilder setPlayers(List<Player> players) {
        this.players = players.stream().map(Player::getUniqueId).collect(Collectors.toList());
        return this;
    }
    
    /**
     * Sets the players that can see the hologram. All other players won't be able to see it.
     * @param uuids The uuids of the players that can see the hologram.
     * @return The {@link HologramBuilder} instance.
     * @see #setPlayers(List)
     * @see #setPlayerSupplier(Supplier) 
     * @see #setPlayerUUIDSupplier(Supplier) 
     */
    public HologramBuilder setPlayerUUIDs(List<UUID> uuids) {
        this.players = uuids;
        return this;
    }

    /**
     * Sets a supplier that is used to get the players that can see the hologram. All other players won't be able to see it.
     * The supplier will be called every time the hologram is updated.
     * This overrides the players that are set with {@link #setPlayers(List)}.
     *
     * @param playerSupplier The supplier that is used to get the players that can see the hologram.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the players supplier is null.
     * @see #setPlayers(List)
     * @see #setPlayerUUIDs(List)
     * @see #setPlayerUUIDSupplier(Supplier)
     */
    public HologramBuilder setPlayerSupplier(Supplier<List<Player>> playerSupplier) {
        Objects.requireNonNull(playerSupplier, "The player supplier cannot be null");

        this.playerSupplier = () -> playerSupplier.get().stream().map(Player::getUniqueId).collect(Collectors.toList());
        return this;
    }

    /**
     * Sets a supplier that is used to get the players that can see the hologram. All other players won't be able to see it.
     * The supplier will be called every time the hologram is updated.
     * This overrides the players that are set with {@link #setPlayers(List)}.
     *
     * @param uuidsSupplier The supplier that is used to get the uuids of the players that can see the hologram.
     * @return The {@link HologramBuilder} instance.
     * @throws java.lang.NullPointerException If the players supplier is null.
     * @see #setPlayers(List)
     * @see #setPlayerUUIDs(List)
     * @see #setPlayerSupplier(Supplier)
     */
    public HologramBuilder setPlayerUUIDSupplier(Supplier<List<UUID>> uuidsSupplier) {
        Objects.requireNonNull(uuidsSupplier, "The player supplier cannot be null");

        this.playerSupplier = uuidsSupplier;
        return this;
    }

    /**
     * Builds the hologram.
     * @return The {@link Hologram} instance.
     */
    public Hologram build() {
        return new Hologram(identifier, lines, loc, verticalAlignment, generalModifier, playerModifier, updateInterval, players, playerSupplier);
    }
}