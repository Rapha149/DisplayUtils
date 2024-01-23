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
     * This method is called when the tablist is updated.
     * @param players The current online players.
     * @return The tablist content, i.e. the different {@link TablistGroup}s.
     */
    List<TablistGroup> getTablistContent(Collection<? extends Player> players);
}
