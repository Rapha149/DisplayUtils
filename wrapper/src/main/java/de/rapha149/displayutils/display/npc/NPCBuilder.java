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

    private int maxDistance = 100;
    private int maxViewAngle = 60;

    private NPCUseListener useListener;
    private int listenerCooldown = 20;

    /**
     * Constructs a new NPCBuilder.
     *
     * @param identifier  The identifier of the NPC. Must be unique.
     * @param name        The name of the NPC. Should be unique. A name longer than 16 characters will most likely result in the player being kicked.
     * @param skin        The skin of the NPC. See {@link NPCSkin} for more information.
     * @param loc         The location of the NPC.
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
     * Sets whether the NPC is sneaking or not. <br>
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
     * Sets whether the NPC is collidable or not. <br>
     * The default is true.
     * <p></p>
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
     * Sets whether the NPC's name tag is visible or not. <br>
     * The default is true.
     * <p>
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
     * Sets whether the NPC should match the sneaking state of each player. <br>
     * The default is false.
     * <p>
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
     * Sets whether the NPC should look at each player. <br>
     * The default is false.
     * <p>
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
     * Sets the maximum distance within which the NPC is visible to a player. <br>
     * The distance is measured in blocks. <br>
     * The default value is 100 blocks. <br>
     * The NPC will be shown to the player if the player is within this distance from the NPC and the NPC will be hidden if the player goes further away.
     * <p>
     * The NPC shouldn't be spawned when the player isn't in a certain range from the NPC because if the player is too
     * far away from the NPC, the NPC's skin may not load and the player will see a Steve/Alex skin. <br>
     * You can use this method to change the default max distance if the player should be able to see the NPC from a greater distance
     * or if you experience issues with players not seeing the NPC's skin even within the default distance.
     *
     * @param maxDistance The maximum distance in blocks within which the NPC is visible to a player.
     * @return The {@link NPCBuilder} instance.
     * @throws IllegalArgumentException If the max distance is smaller than 0.
     * @see #setMaxViewAngle(int)
     */
    public NPCBuilder setMaxDistance(int maxDistance) {
        if (maxDistance < 0)
            throw new IllegalArgumentException("The max distance cannot be smaller than 0");

        this.maxDistance = maxDistance;
        return this;
    }

    /**
     * Sets the maximum view angle within which the NPC is visible to a player. <br>
     * The angle is measured in degrees. <br>
     * The default value is 60 degrees. At a value of 180 degrees it is assumed that the NPC is always in view of the player. <br>
     * The NPC will only be spawned for the player if the player looks at the NPC within this angle but the NPC will not be despawned if the player looks away.
     * <p>
     * The NPC shouldn't be spawned when the player isn't looking in it's direction because if the player isn't looking
     * at the NPC, the NPC's skin may not load and the player will see a Steve/Alex skin. <br>
     * You can use this method to change the default max view angle if the player should be able to see the NPC from a wider angle
     * or if you experience issues with players not seeing the NPC's skin even within the default angle.
     * <p>
     * The view angle is used to determine whether the NPC is within the player's field of view.
     * This is done by calculating the angle between the player's view direction and the direction to the NPC.
     * If this angle is less than or equal to the maximum view angle, the NPC is considered to be within the player's field of view.
     *
     * @param maxViewAngle The maximum view angle in degrees within which the NPC is visible to a player.
     * @return The {@link NPCBuilder} instance.
     * @throws IllegalArgumentException If the max view angle is smaller than 0 or greater than 180 degrees.
     * @see #setMaxDistance(int)
     */
    public NPCBuilder setMaxViewAngle(int maxViewAngle) {
        if (maxViewAngle < 0 || maxViewAngle > 180)
            throw new IllegalArgumentException("The max view angle must be between 0 and 180 degrees");

        this.maxViewAngle = maxViewAngle;
        return this;
    }

    /**
     * Sets the cooldown of the listener that is called when a player rightclicks the NPC. <br>
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
     *
     * @return The {@link NPC} instance.
     */
    public NPC build() {
        int maxDistanceSquared = maxDistance * maxDistance;
        double maxViewAngleCos = Math.cos(Math.toRadians(maxViewAngle));
        return new NPC(identifier, name, skin, loc, includedPlayers, excludedPlayers, sneaking, collidable, nameTagVisible, matchSneakingWithPlayer, lookAtPlayer, maxDistanceSquared, maxViewAngleCos, useListener, listenerCooldown);
    }
}