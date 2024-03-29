package de.rapha149.displayutils.display.tablist;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

/**
 * A provider for tablist content.
 */
@FunctionalInterface
public interface TablistContentProvider {

    /**
     * This method is called when the tablist is updated. <br>
     * The order of the returned groups is the order in which they are displayed. Players not included in any group are displayed last. <br>
     * If a player is included in multiple groups, the first group in which they are included is used.
     *
     * @param players The current online players.
     * @return The {@link TablistGroup}s.
     */
    List<TablistGroup> getTablistContent(Collection<? extends Player> players);
}
