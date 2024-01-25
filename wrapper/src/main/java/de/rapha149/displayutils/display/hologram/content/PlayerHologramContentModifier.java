package de.rapha149.displayutils.display.hologram.content;


import org.bukkit.entity.Player;

import java.util.List;

/**
 * A modifier for the lines of a hologram.
 * This modifier is used for holograms that are player specific.
 */
@FunctionalInterface
public interface PlayerHologramContentModifier {

    /**
     * This method is called when the content of the hologram is updated.
     * The returned lines must the be same size as the input lines. You may not return null.
     * In order to not show a specific line, set that line to null. The armor stand of that line won't be shown either.
     *
     * @param player The player to whom the returned lines are shown.
     * @param lines The current lines of the hologram. Please note that this list is immutable.
     * @return The modified lines of the hologram.
     */
    List<String> modify(Player player, List<String> lines);
}
