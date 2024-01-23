package de.rapha149.displayutils.display.hologram.provider;

import de.rapha149.displayutils.display.hologram.HologramPlaceholders;
import org.bukkit.entity.Player;

/**
 * A provider for the placeholder values of a hologram. Used to update the content of a hologram.
 * This provider is used for holograms that are not player specific.
 */
@FunctionalInterface
public interface GeneralHologramContentProvider extends HologramContentProvider {

    /**
     * @throws UnsupportedOperationException This method is not supported by this provider.
     */
    @Override
    default HologramPlaceholders getPlaceholders(Player player) {
        throw new UnsupportedOperationException();
    }
}
