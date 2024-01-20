package de.rapha149.displayutils.utilityclasses.hologram.provider;

import de.rapha149.displayutils.utilityclasses.hologram.HologramPlaceholders;
import org.bukkit.entity.Player;

public interface HologramContentProvider {

    HologramPlaceholders getPlaceholders();

    HologramPlaceholders getPlaceholders(Player player);
}
