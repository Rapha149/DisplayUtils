package de.rapha149.displayutils.display.sidebar;

import de.rapha149.displayutils.display.sidebar.content.GeneralSidebarContentModifier;
import de.rapha149.displayutils.display.sidebar.content.PlayerSidebarContentProvider;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class SidebarBuilder {

    private String title;
    private List<String> lines;
    private GeneralSidebarContentModifier generalModifier = null;
    private PlayerSidebarContentProvider playerModifier = null;
    private Integer updateInterval = null;
    private List<UUID> players = null;
    private Supplier<List<UUID>> playerSupplier = null;

    /**
     * Constructs a new SidebarBuilder. <p>
     * If the title is longer than 32 characters, it will be cut off. <p>
     * If there are more than 15 lines, the last lines will be ignored. <p>
     * If a line is longer than 32 (up to 1.12) or 128 (since 1.13) characters, it will be cut off. If you are using color codes, it could also be cut off sooner.
     *
     * @param title The title of the sidebar. Will be shown above the lines.
     * @param lines The lines of the sidebar.
     * @throws java.lang.NullPointerException If the name or the lines are null.
     * @throws java.lang.IllegalArgumentException If the lines are empty.
     */
    public SidebarBuilder(String title, List<String> lines) {
        Objects.requireNonNull(title, "The name cannot be null");
        Objects.requireNonNull(lines, "The lines cannot be null");
        if (lines.isEmpty())
            throw new IllegalArgumentException("The lines cannot be empty");

        this.title = title;
        this.lines = lines.stream().map(line -> line != null ? line : "").collect(Collectors.toList());
    }

    /**
     * Sets the modifier that can be used to update the lines of the sidebar without re-adding it.
     * Use this method if the content of the sidebar is the same for all players.
     * @param generalModifier The modifier.
     * @return The {@link SidebarBuilder} instance.
     * @throws java.lang.NullPointerException If the modifier is null.
     * @see #setPlayerContentModifier(PlayerSidebarContentProvider)
     */
    public SidebarBuilder setGeneralContentModifier(GeneralSidebarContentModifier generalModifier) {
        Objects.requireNonNull(generalModifier, "The modifier cannot be null");

        this.generalModifier = generalModifier;
        return this;
    }

    /**
     * Sets the modifier that can be used to update the lines of the sidebar without re-adding it.
     * Use this method if the content of the sidebar is different for each player.
     * @param playerModifier The modifier.
     * @return The {@link SidebarBuilder} instance.
     * @throws java.lang.NullPointerException If the modifier is null.
     * @see #setGeneralContentModifier(GeneralSidebarContentModifier)
     */
    public SidebarBuilder setPlayerContentModifier(PlayerSidebarContentProvider playerModifier) {
        Objects.requireNonNull(playerModifier, "The modifier cannot be null");

        this.playerModifier = playerModifier;
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

    /**
     * Sets the players that can see the sidebar. All other players won't be able to see it.
     * @param players The players that can see the sidebar.
     * @return The {@link SidebarBuilder} instance.
     * @see #setPlayerUUIDs(List)
     * @see #setPlayerSupplier(Supplier)
     * @see #setPlayerUUIDSupplier(Supplier)
     */
    public SidebarBuilder setPlayers(List<Player> players) {
        this.players = players.stream().map(Player::getUniqueId).collect(Collectors.toList());
        return this;
    }

    /**
     * Sets the players that can see the sidebar. All other players won't be able to see it.
     * @param uuids The uuids of the players that can see the sidebar.
     * @return The {@link SidebarBuilder} instance.
     * @see #setPlayers(List)
     * @see #setPlayerSupplier(Supplier)
     * @see #setPlayerUUIDSupplier(Supplier)
     */
    public SidebarBuilder setPlayerUUIDs(List<UUID> uuids) {
        this.players = uuids;
        return this;
    }

    /**
     * Sets a supplier that is used to get the players that can see the sidebar. All other players won't be able to see it.
     * The supplier will be called every time the sidebar is updated.
     * This overrides the players that are set with {@link #setPlayers(List)}.
     *
     * @param playerSupplier The supplier that is used to get the players that can see the sidebar.
     * @return The {@link SidebarBuilder} instance.
     * @throws java.lang.NullPointerException If the player supplier is null.
     * @see #setPlayers(List)
     * @see #setPlayerUUIDs(List)
     * @see #setPlayerUUIDSupplier(Supplier)
     */
    public SidebarBuilder setPlayerSupplier(Supplier<List<Player>> playerSupplier) {
        Objects.requireNonNull(playerSupplier, "The player supplier cannot be null");

        this.playerSupplier = () -> playerSupplier.get().stream().map(Player::getUniqueId).collect(Collectors.toList());
        return this;
    }

    /**
     * Sets a supplier that is used to get the players that can see the sidebar. All other players won't be able to see it.
     * The supplier will be called every time the sidebar is updated.
     * This overrides the players that are set with {@link #setPlayers(List)}.
     *
     * @param uuidsSupplier The supplier that is used to get the uuids of the players that can see the sidebar.
     * @return The {@link SidebarBuilder} instance.
     * @throws java.lang.NullPointerException If the player supplier is null.
     * @see #setPlayers(List)
     * @see #setPlayerUUIDs(List)
     * @see #setPlayerSupplier(Supplier)
     */
    public SidebarBuilder setPlayerUUIDSupplier(Supplier<List<UUID>> uuidsSupplier) {
        Objects.requireNonNull(uuidsSupplier, "The player supplier cannot be null");

        this.playerSupplier = uuidsSupplier;
        return this;
    }

    /**
     * Builds the sidebar.
     * @return The {@link Sidebar} instance.
     */
    public Sidebar build() {
        return new Sidebar(title, lines, generalModifier, playerModifier, updateInterval, players, playerSupplier);
    }
}