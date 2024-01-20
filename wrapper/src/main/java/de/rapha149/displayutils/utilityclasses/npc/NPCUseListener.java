package de.rapha149.displayutils.utilityclasses.npc;

import org.bukkit.entity.Player;

@FunctionalInterface
public interface NPCUseListener {

    void onUseNPC(Player player);
}
