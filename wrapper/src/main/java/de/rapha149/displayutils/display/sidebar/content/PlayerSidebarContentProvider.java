package de.rapha149.displayutils.display.sidebar.content;

import org.bukkit.entity.Player;

import java.util.List;

/**
 * A modifier for the lines of the sidebar.
 * This modifier is used if the sidebar is player specific.
 */
@FunctionalInterface
public interface PlayerSidebarContentProvider {

    /**
     * This method is called when the content of the sidebar is updated.
     * The returned lines must the be same size as the input lines. You may not return null.
     *
     * @param player The player to whom the returned lines are shown.
     * @param lines The current lines of the sidebar.
     * @return The modified lines of the sidebar.
     */
    List<String> modify(Player player, List<String> lines);
}
