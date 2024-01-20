package de.rapha149.displayutils.utilityclasses.sidebar.provider;

import de.rapha149.displayutils.utilityclasses.sidebar.SidebarPlaceholders;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface GeneralSidebarContentProvider extends SidebarContentProvider {

    @Override
    default SidebarPlaceholders getPlaceholders(Player player) {
        throw new UnsupportedOperationException();
    }
}
