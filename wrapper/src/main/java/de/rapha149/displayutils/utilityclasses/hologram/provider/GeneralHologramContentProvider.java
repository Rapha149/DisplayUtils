package de.rapha149.displayutils.utilityclasses.hologram.provider;

import de.rapha149.displayutils.utilityclasses.hologram.HologramPlaceholders;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface GeneralHologramContentProvider extends HologramContentProvider {

    @Override
    default HologramPlaceholders getPlaceholders(Player player) {
        throw new UnsupportedOperationException();
    }
}
