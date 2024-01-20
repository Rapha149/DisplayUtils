package de.rapha149.displayutils.utilityclasses.hologram.provider;


import de.rapha149.displayutils.utilityclasses.hologram.HologramPlaceholders;

/**
 * A provider for the placeholder values of a hologram. Used to update the content of a hologram.
 * This provider is used for holograms that are player specific.
 */
@FunctionalInterface
public interface PlayerHologramContentProvider extends HologramContentProvider {

    /**
     * @throws UnsupportedOperationException This method is not supported by this provider.
     */
    @Override
    default HologramPlaceholders getPlaceholders() {
        throw new UnsupportedOperationException();
    }
}
