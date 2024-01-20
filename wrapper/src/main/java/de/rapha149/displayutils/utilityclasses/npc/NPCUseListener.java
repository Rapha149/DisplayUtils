package de.rapha149.displayutils.utilityclasses.npc;

import org.bukkit.entity.Player;

/**
 * A functional interface used for handling a player interacting with an NPC.
 */
@FunctionalInterface
public interface NPCUseListener {

    /**
     * Called when a player interacts with the NPC.
     * @param player The player who interacted with the NPC.
     */
    void onUseNPC(Player player);
}
