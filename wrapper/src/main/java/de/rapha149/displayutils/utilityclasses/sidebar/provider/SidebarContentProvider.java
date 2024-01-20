package de.rapha149.displayutils.utilityclasses.sidebar.provider;

import de.rapha149.displayutils.utilityclasses.sidebar.SidebarPlaceholders;
import org.bukkit.entity.Player;

public interface SidebarContentProvider {

    SidebarPlaceholders getPlaceholders();

    SidebarPlaceholders getPlaceholders(Player player);
}
