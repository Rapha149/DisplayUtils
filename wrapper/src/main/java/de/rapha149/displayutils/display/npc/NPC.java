package de.rapha149.displayutils.display.npc;

import org.bukkit.Location;

import java.util.List;
import java.util.UUID;

/**
 * A class holding information about an NPC.
 */
public class NPC {

    private final String identifier;
    private final String name;
    private final NPCSkin skin;
    private final Location loc;
    private final List<UUID> includedPlayers;
    private final List<UUID> excludedPlayers;
    private final boolean sneaking;
    private final boolean collidable;
    private final boolean nameTagVisible;

    private final boolean matchSneakingWithPlayer;
    private final boolean lookAtPlayer;

    private final int maxDistanceSquared;
    private final double maxViewAngleCos;

    private final NPCUseListener useListener;
    private final int listenerCooldown;

    /**
     * Use the {@link NPCBuilder} to create a new NPC.
     * @param identifier The identifier of the NPC. Must be unique.
     * @param name The name of the NPC. Should be unique.
     * @param skin The skin of the NPC. See {@link NPCSkin} for more information.
     * @param loc The location of the NPC.
     * @param includedPlayers The players that can see the NPC. All other players won't be able to see it. If this and excludedPlayers is null, all players will be able to see the NPC.
     * @param excludedPlayers The players that can't see the NPC. If this and includedPlayers is null, all players will be able to see the NPC.
     * @param sneaking Whether the NPC is sneaking.
     * @param collidable Whether the NPC is collidable.
     * @param nameTagVisible Whether the NPC's name tag is visible.
     * @param matchSneakingWithPlayer Whether the NPC's sneaking state should be matched with each player's sneaking state.
     * @param lookAtPlayer Whether the NPC should look at each player.
     * @param maxDistanceSquared The maximum distance squared in blocks at which the NPC is shown to the player.
     * @param maxViewAngleCos The maximum cosine of the view angle at which the NPC is shown to the player.
     * @param useListener The listener that is called when the NPC is used.
     * @param listenerCooldown The cooldown of the listener in ticks.
     */
    NPC(String identifier, String name, NPCSkin skin, Location loc, List<UUID> includedPlayers, List<UUID> excludedPlayers, boolean sneaking, boolean collidable, boolean nameTagVisible, boolean matchSneakingWithPlayer, boolean lookAtPlayer, int maxDistanceSquared, double maxViewAngleCos, NPCUseListener useListener, int listenerCooldown) {
        this.identifier = identifier;
        this.name = name;
        this.skin = skin;
        this.loc = loc;
        this.includedPlayers = includedPlayers;
        this.excludedPlayers = excludedPlayers;
        this.sneaking = sneaking;
        this.collidable = collidable;
        this.nameTagVisible = nameTagVisible;
        this.matchSneakingWithPlayer = matchSneakingWithPlayer;
        this.lookAtPlayer = lookAtPlayer;
        this.maxDistanceSquared = maxDistanceSquared;
        this.maxViewAngleCos = maxViewAngleCos;
        this.useListener = useListener;
        this.listenerCooldown = Math.max(listenerCooldown, 1);
    }

    /**
     * @return The unique identifier of the NPC.
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @return The name of the NPC.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The skin of the NPC.
     * @see NPCSkin
     */
    public NPCSkin getSkin() {
        return skin;
    }

    /**
     * @return The location of the NPC.
     */
    public Location getLoc() {
        return loc;
    }

    /**
     * Checks the includedPlayers and excludedPlayers list and returns whether the NPC is shown for the player.
     * @param player The player.
     * @return True if the NPC is shown for the player, false otherwise.
     */
    public boolean isShownForPlayer(UUID player) {
        if (includedPlayers != null && !includedPlayers.contains(player))
            return false;
        //noinspection RedundantIfStatement
        if (excludedPlayers != null && excludedPlayers.contains(player))
            return false;
        return true;
    }

    /**
     * @return True if the NPC is sneaking, false otherwise.
     */
    public boolean isSneaking() {
        return sneaking;
    }

    /**
     * @return True if the NPC is collidable, false otherwise.
     */
    public boolean isCollidable() {
        return collidable;
    }

    /**
     * @return True if the NPC's name tag is visible, false otherwise.
     */
    public boolean isNameTagVisible() {
        return nameTagVisible;
    }

    /**
     * @return True if the NPC's sneaking state should be matched with each player's sneaking state, false otherwise.
     */
    public boolean isMatchSneakingWithPlayer() {
        return matchSneakingWithPlayer;
    }

    /**
     * @return True if the NPC should look at each player, false otherwise.
     */
    public boolean isLookAtPlayer() {
        return lookAtPlayer;
    }

    /**
     * @return The maximum distance squared in blocks at which the NPC is shown to the player.
     */
    public int getMaxDistanceSquared() {
        return maxDistanceSquared;
    }

    /**
     * @return The maximum cosine of the view angle at which the NPC is shown to the player.
     */
    public double getMaxViewAngleCos() {
        return maxViewAngleCos;
    }

    /**
     * @return The listener that is called when the NPC is used.
     * @see NPCUseListener
     */
    public NPCUseListener getUseListener() {
        return useListener;
    }

    /**
     * @return The cooldown of the listener in ticks.
     */
    public int getListenerCooldown() {
        return listenerCooldown;
    }
}
