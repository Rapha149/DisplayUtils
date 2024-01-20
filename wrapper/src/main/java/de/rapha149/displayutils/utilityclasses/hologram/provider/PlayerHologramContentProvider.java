package de.rapha149.displayutils.utilityclasses.hologram.provider;


import de.rapha149.displayutils.utilityclasses.hologram.HologramPlaceholders;

@FunctionalInterface
public interface PlayerHologramContentProvider extends HologramContentProvider {

    @Override
    default HologramPlaceholders getPlaceholders() {
        throw new UnsupportedOperationException();
    }
}
