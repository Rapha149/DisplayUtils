package de.rapha149.displayutils.utilityclasses.sidebar.provider;

import de.rapha149.displayutils.utilityclasses.sidebar.SidebarPlaceholders;
import org.bukkit.entity.Player;

/**
 * A provider for the placeholder values of the sidebar. Used to update the content of the sidebar.
 * Use {@link GeneralSidebarContentProvider} or {@link PlayerSidebarContentProvider}.
 */
public interface SidebarContentProvider {

    /**
     * This method is called when the content of the sidebar is updated.
     * @return The current general placeholders of the sidebar.
     */
    SidebarPlaceholders getPlaceholders();

    /**
     * This method is called when the content of the sidebar is updated.
     * @param player The player to whom the returned placeholders are shown in the sidebar.
     * @return The current player specific placeholders of the sidebar.
     */
    SidebarPlaceholders getPlaceholders(Player player);
}
