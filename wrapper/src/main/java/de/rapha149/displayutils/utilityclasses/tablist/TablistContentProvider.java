package de.rapha149.displayutils.utilityclasses.tablist;

import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.List;

@FunctionalInterface
public interface TablistContentProvider {

    List<TablistGroup> getTablistContent(Collection<? extends Player> players);
}
