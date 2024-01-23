package de.rapha149.displayutils.display.sidebar.provider;

import de.rapha149.displayutils.display.sidebar.SidebarPlaceholders;

/**
 * A provider for the placeholder values of the sidebar. Used to update the content of the sidebar.
 * This provider is used if the sidebar is player specific.
 */
@FunctionalInterface
public interface PlayerSidebarContentProvider extends SidebarContentProvider {

    /**
     * @throws UnsupportedOperationException This method is not supported by this provider.
     */
    @Override
    default SidebarPlaceholders getPlaceholders() {
        throw new UnsupportedOperationException();
    }
}
