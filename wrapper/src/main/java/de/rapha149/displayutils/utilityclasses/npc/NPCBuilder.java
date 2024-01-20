package de.rapha149.displayutils.utilityclasses.npc;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class NPCBuilder {

    private String identifier;
    private String name;
    private NPCSkin skin;
    private Location loc;
    private List<UUID> players = null;
    private boolean sneaking = false;
    private boolean collidable = true;
    private boolean nameTagVisible = true;

    private boolean matchSneakingWithPlayer = false;
    private boolean lookAtPlayer = false;

    private NPCUseListener useListener;
    private int listenerCooldown = 20;

    public NPCBuilder(String identifier, String name, NPCSkin skin, Location loc, NPCUseListener useListener) {
        this.identifier = identifier;
        this.name = name;
        this.skin = skin;
        this.loc = loc;
        this.useListener = useListener;
    }

    public NPCBuilder setPlayer(Player player) {
        this.players = Collections.singletonList(player.getUniqueId());
        return this;
    }

    public NPCBuilder setPlayers(List<Player> players) {
        this.players = players.stream().map(Player::getUniqueId).collect(Collectors.toList());
        return this;
    }

    public NPCBuilder setSneaking(boolean sneaking) {
        this.sneaking = sneaking;
        return this;
    }

    public NPCBuilder setCollidable(boolean collidable) {
        this.collidable = collidable;
        return this;
    }

    public NPCBuilder setNameTagVisible(boolean nameTagVisible) {
        this.nameTagVisible = nameTagVisible;
        return this;
    }

    public NPCBuilder setMatchSneakingWithPlayer(boolean matchSneakingWithPlayer) {
        this.matchSneakingWithPlayer = matchSneakingWithPlayer;
        return this;
    }

    public NPCBuilder setLookAtPlayer(boolean lookAtPlayer) {
        this.lookAtPlayer = lookAtPlayer;
        return this;
    }

    /**
     * @param listenerCooldown in ticks
     * @return this
     */
    public NPCBuilder setListenerCooldown(int listenerCooldown) {
        this.listenerCooldown = listenerCooldown;
        return this;
    }

    public NPC build() {
        return new NPC(identifier, name, skin, loc, players, sneaking, collidable, nameTagVisible, matchSneakingWithPlayer, lookAtPlayer, useListener, listenerCooldown);
    }
}