package de.rapha149.displayutils.display.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A builder for {@link NPC}s.
 */
public class NPCBuilder {

    private String identifier;
    private String name;
    private NPCSkin skin;
    private Location loc;
    private List<UUID> includedPlayers = null;
    private List<UUID> excludedPlayers = null;
    private boolean sneaking = false;
    private boolean collidable = true;
    private boolean nameTagVisible = true;

    private boolean matchSneakingWithPlayer = false;
    private boolean lookAtPlayer = false;

    private NPCUseListener useListener;
    private int listenerCooldown = 20;

    /**
     * Constructs a new NPCBuilder.
     * @param identifier The identifier of the NPC. Must be unique.
     * @param name The name of the NPC. Should be unique. A name longer than 16 characters will most likely result in the player being kicked.
     * @param skin The skin of the NPC. See {@link NPCSkin} for more information.
     * @param loc The location of the NPC.
     * @param useListener The listener that is called when the NPC is used.
     * @throws NullPointerException If any of the parameters are null.
     */
    public NPCBuilder(String identifier, String name, NPCSkin skin, Location loc, NPCUseListener useListener) {
        Objects.requireNonNull(identifier, "The identifier cannot be null");
        Objects.requireNonNull(name, "The name cannot be null");
        Objects.requireNonNull(skin, "The skin cannot be null");
        Objects.requireNonNull(loc, "The location cannot be null");
        Objects.requireNonNull(useListener, "The use listener cannot be null");

        this.identifier = identifier;
        this.name = name;
        this.skin = skin;
        this.loc = loc;
        this.useListener = useListener;
    }

    /**
     * Sets the players that can see the NPC. All other players won't be able to see it. <br>
     * If this and the other mentioned methods are not called, all players will be able to see the NPC.
     *
     * @param includedPlayers The players that can see the NPC.
     * @return The {@link NPCBuilder} instance.
     * @throws NullPointerException If the list or any of the players are null.
     * @see #setIncludedPlayerUUIDs(List)
     * @see #setExcludedPlayers(List)
     * @see #setExcludedPlayerUUIDs(List)
     */
    public NPCBuilder setIncludedPlayers(List<Player> includedPlayers) {
        Objects.requireNonNull(includedPlayers, "The list cannot be null");
        includedPlayers.forEach(player -> Objects.requireNonNull(player, "The list cannot contain null players"));

        this.includedPlayers = includedPlayers.stream().map(Player::getUniqueId).collect(Collectors.toList());
        return this;
    }

    /**
     * Sets the players that can see the NPC. All other players won't be able to see it. <br>
     * If this and the other mentioned methods are not called, all players will be able to see the NPC.
     *
     * @param uuids The uuids of the players that can see the NPC.
     * @return The {@link NPCBuilder} instance.
     * @throws NullPointerException If the list or any of the uuids are null.
     * @see #setIncludedPlayers(List)
     * @see #setExcludedPlayers(List)
     * @see #setExcludedPlayerUUIDs(List)
     */
    public NPCBuilder setIncludedPlayerUUIDs(List<UUID> uuids) {
        Objects.requireNonNull(uuids, "The list cannot be null");
        uuids.forEach(uuid -> Objects.requireNonNull(uuid, "The list cannot contain null uuids"));

        this.includedPlayers = uuids;
        return this;
    }

    /**
     * Sets the players that cannot see the NPC. All other players will be able to see it. <br>
     * If this and the other mentioned methods are not called, all players will be able to see the NPC.
     *
     * @param excludedPlayers The players that cannot see the NPC.
     * @return The {@link NPCBuilder} instance.
     * @throws NullPointerException If the list or any of the players are null.
     * @see #setIncludedPlayers(List)
     * @see #setIncludedPlayerUUIDs(List)
     * @see #setExcludedPlayerUUIDs(List)
     */
    public NPCBuilder setExcludedPlayers(List<Player> excludedPlayers) {
        Objects.requireNonNull(excludedPlayers, "The list cannot be null");
        excludedPlayers.forEach(player -> Objects.requireNonNull(player, "The list cannot contain null players"));

        this.excludedPlayers = excludedPlayers.stream().map(Player::getUniqueId).collect(Collectors.toList());
        return this;
    }

    /**
     * Sets the players that cannot see the NPC. All other players will be able to see it. <br>
     * If this and the other mentioned methods are not called, all players will be able to see the NPC.
     *
     * @param uuids The uuids of the players that cannot see the NPC.
     * @return The {@link NPCBuilder} instance.
     * @throws NullPointerException If the list or any of the uuids are null.
     * @see #setIncludedPlayers(List)
     * @see #setIncludedPlayerUUIDs(List)
     * @see #setExcludedPlayers(List)
     */
    public NPCBuilder setExcludedPlayerUUIDs(List<UUID> uuids) {
        Objects.requireNonNull(uuids, "The list cannot be null");
        uuids.forEach(uuid -> Objects.requireNonNull(uuid, "The list cannot contain null uuids"));

        this.excludedPlayers = uuids;
        return this;
    }

    /**
     * Sets whether the NPC is sneaking or not.
     * The default is false.
     *
     * @param sneaking Whether the NPC is sneaking or not.
     * @return The {@link NPCBuilder} instance.
     */
    public NPCBuilder setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        return this;
    }

    /**
     * Sets whether the NPC is collidable or not.
     * The default is true. <br>
     * When using this method you should avoid sending NPCs with the same name to the same player. <br>
     * That is because this flag is applied by adding the NPC to a team, which can only be done by name. <br>
     * Therefore having two NPCs with the same name but different collidable flags will result in the same flag for both NPCs.
     *
     * @param collidable Whether the NPC is collidable or not.
     * @return The {@link NPCBuilder} instance.
     */
    public NPCBuilder setCollidable(boolean collidable) {
        this.collidable = collidable;
        return this;
    }

    /**
     * Sets whether the NPC's name tag is visible or not.
     * The default is true. <br>
     *
     * When using this method you should avoid sending NPCs with the same name to the same player. <br>
     * That is because this flag is applied by adding the NPC to a team, which can only be done by name. <br>
     * Therefore having two NPCs with the same name but different nameTagVisible flags will result in the same flag for both NPCs.
     *
     * @param nameTagVisible Whether the NPC's name tag is visible or not.
     * @return The {@link NPCBuilder} instance.
     */
    public NPCBuilder setNameTagVisible(boolean nameTagVisible) {
        this.nameTagVisible = nameTagVisible;
        return this;
    }

    /**
     * Sets whether the NPC should match the sneaking state of each player.
     * The default is false. <br>
     *
     * This will work even when there are multiple players that can see the NPC. The NPC will individually adjust its
     * sneaking state to match that of each player, making it unique to each respective player.
     *
     * @param matchSneakingWithPlayer Whether the NPC should match the sneaking state of the player.
     * @return The {@link NPCBuilder} instance.
     */
    public NPCBuilder setMatchSneakingWithPlayer(boolean matchSneakingWithPlayer) {
        this.matchSneakingWithPlayer = matchSneakingWithPlayer;
        return this;
    }

    /**
     * Sets whether the NPC should look at each player.
     * The default is false. <br>
     *
     * This will work even when there are multiple players that can see the NPC. The NPC will individually look at each player, making it unique to each respective player.
     *
     * @param lookAtPlayer Whether the NPC should look at the player.
     * @return The {@link NPCBuilder} instance.
     */
    public NPCBuilder setLookAtPlayer(boolean lookAtPlayer) {
        this.lookAtPlayer = lookAtPlayer;
        return this;
    }

    /**
     * Sets the cooldown of the listener that is called when a player rightclicks the NPC.
     * The default is 20 ticks.
     *
     * @param listenerCooldown The cooldown in ticks.
     * @return The {@link NPCBuilder} instance.
     * @throws IllegalArgumentException If the cooldown is smaller than 0.
     */
    public NPCBuilder setListenerCooldown(int listenerCooldown) {
        if (listenerCooldown < 0)
            throw new IllegalArgumentException("The listener cooldown cannot be smaller than 0");

        this.listenerCooldown = listenerCooldown;
        return this;
    }

    /**
     * Builds the NPC.
     * @return The {@link NPC} instance.
     */
    public NPC build() {
        return new NPC(identifier, name, skin, loc, includedPlayers, excludedPlayers, sneaking, collidable, nameTagVisible, matchSneakingWithPlayer, lookAtPlayer, useListener, listenerCooldown);
    }
}