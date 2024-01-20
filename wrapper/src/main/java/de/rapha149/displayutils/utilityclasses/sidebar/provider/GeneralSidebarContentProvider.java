package de.rapha149.displayutils.utilityclasses.sidebar.provider;

import de.rapha149.displayutils.utilityclasses.sidebar.SidebarPlaceholders;
import org.bukkit.entity.Player;

/**
 * A provider for the placeholder values of the sidebar. Used to update the content of the sidebar.
 * This provider if the sidebar is not player specific.
 */
@FunctionalInterface
public interface GeneralSidebarContentProvider extends SidebarContentProvider {

    /**
     * @throws UnsupportedOperationException This method is not supported by this provider.
     */
    @Override
    default SidebarPlaceholders getPlaceholders(Player player) {
        throw new UnsupportedOperationException();
    }
}
