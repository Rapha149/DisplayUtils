package de.rapha149.displayutils.utilityclasses.sidebar.provider;

import de.rapha149.displayutils.utilityclasses.sidebar.SidebarPlaceholders;

@FunctionalInterface
public interface PlayerSidebarContentProvider extends SidebarContentProvider {

    @Override
    default SidebarPlaceholders getPlaceholders() {
        throw new UnsupportedOperationException();
    }
}
