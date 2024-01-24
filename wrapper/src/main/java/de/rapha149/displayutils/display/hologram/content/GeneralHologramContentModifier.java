package de.rapha149.displayutils.display.hologram.content;


import java.util.List;

/**
 * A modifier for the lines of a hologram.
 * This modifier is used for holograms that are not player specific.
 */
@FunctionalInterface
public interface GeneralHologramContentModifier {

    /**
     * This method is called when the content of the hologram is updated.
     * The returned lines must the be same size as the input lines. You may not return null.
     * In order to not show a specific line, set that line to null. The armor stand of that line won't be shown either.
     *
     * @param lines The current lines of the hologram.
     * @return The modified lines of the hologram.
     */
    List<String> modify(List<String> lines);
}
