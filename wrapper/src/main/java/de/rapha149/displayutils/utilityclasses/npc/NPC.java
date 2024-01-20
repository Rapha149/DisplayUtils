package de.rapha149.displayutils.utilityclasses.npc;

import org.bukkit.Location;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class NPC {

    private final String identifier;
    private final String name;
    private final NPCSkin skin;
    private final Location loc;
    private final List<UUID> players;
    private final boolean sneaking;
    private final boolean collidable;
    private final boolean nameTagVisible;

    private final boolean matchSneakingWithPlayer;
    private final boolean lookAtPlayer;

    private final NPCUseListener useListener;
    private final int listenerCooldown;

    NPC(String identifier, String name, NPCSkin skin, Location loc, List<UUID> players, boolean sneaking, boolean collidable, boolean nameTagVisible, boolean matchSneakingWithPlayer, boolean lookAtPlayer, NPCUseListener useListener, int listenerCooldown) {
        this.identifier = identifier;
        this.name = name;
        this.skin = skin;
        this.loc = loc;
        this.players = players;
        this.sneaking = sneaking;
        this.collidable = collidable;
        this.nameTagVisible = nameTagVisible;
        this.matchSneakingWithPlayer = matchSneakingWithPlayer;
        this.lookAtPlayer = lookAtPlayer;
        this.useListener = useListener;
        this.listenerCooldown = Math.max(listenerCooldown, 1);
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
    }

    public NPCSkin getSkin() {
        return skin;
    }

    public Location getLoc() {
        return loc;
    }

    public List<UUID> getPlayers() {
        return players;
    }

    public boolean isSneaking() {
        return sneaking;
    }

    public boolean isCollidable() {
        return collidable;
    }

    public boolean isNameTagVisible() {
        return nameTagVisible;
    }

    public boolean isMatchSneakingWithPlayer() {
        return matchSneakingWithPlayer;
    }

    public boolean isLookAtPlayer() {
        return lookAtPlayer;
    }

    public NPCUseListener getUseListener() {
        return useListener;
    }

    public int getListenerCooldown() {
        return listenerCooldown;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        NPC npc = (NPC) o;
        return sneaking == npc.sneaking &&
                collidable == npc.collidable &&
                nameTagVisible == npc.nameTagVisible &&
                matchSneakingWithPlayer == npc.matchSneakingWithPlayer &&
                lookAtPlayer == npc.lookAtPlayer &&
                listenerCooldown == npc.listenerCooldown &&
                Objects.equals(identifier, npc.identifier) &&
                Objects.equals(name, npc.name) &&
                Objects.equals(skin, npc.skin) &&
                Objects.equals(loc, npc.loc) &&
                Objects.equals(players, npc.players) &&
                Objects.equals(useListener, npc.useListener);
    }

    @Override
    public int hashCode() {
        return Objects.hash(identifier, name, skin, loc, players, sneaking, collidable, nameTagVisible, matchSneakingWithPlayer, lookAtPlayer, useListener, listenerCooldown);
    }
}
