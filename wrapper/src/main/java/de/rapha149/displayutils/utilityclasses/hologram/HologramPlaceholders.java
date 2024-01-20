package de.rapha149.displayutils.utilityclasses.hologram;

import java.util.HashMap;
import java.util.Map;

/**
 * A map of placeholders that can be applied to a string.
 */
public class HologramPlaceholders extends HashMap<String, String> {

    /**
     * Constructs new HologramPlaceholders.
     * @see HashMap#HashMap()
     */
    public HologramPlaceholders() {
        super();
    }

    /**
     * Constructs new HologramPlaceholders with the same mappings as the specified map.
     * @param map The map whose mappings are to be placed in this map.
     * @see HashMap#HashMap(Map)
     */
    public HologramPlaceholders(Map<String, String> map) {
        super(map);
    }

    /**
     * Applies the placeholders to the input string.
     * @param input The input string.
     * @return The input string with the placeholders applied.
     */
    public String apply(String input) {
        for (Entry<String, String> entry : entrySet())
            input = input.replace("%" + entry.getKey() + "%", entry.getValue());
        return input;
    }
}
