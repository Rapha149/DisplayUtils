package de.rapha149.displayutils.utilityclasses.hologram.provider;

import de.rapha149.displayutils.utilityclasses.hologram.HologramPlaceholders;
import org.bukkit.entity.Player;

/**
 * A provider for the placeholder values of a hologram. Used to update the content of a hologram.
 * Use {@link GeneralHologramContentProvider} or {@link PlayerHologramContentProvider}.
 */
public interface HologramContentProvider {

    /**
     * This method is called when the content of the hologram is updated.
     * @return The current general placeholders of the hologram.
     */
    HologramPlaceholders getPlaceholders();

    /**
     * This method is called when the content of the hologram is updated.
     * @param player The player to whom the returned placeholders are shown in the hologram.
     * @return The current player specific placeholders of the hologram.
     */
    HologramPlaceholders getPlaceholders(Player player);
}
