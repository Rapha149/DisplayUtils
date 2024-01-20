package de.rapha149.displayutils.utilityclasses.hologram;

import java.util.HashMap;
import java.util.Map;

public class HologramPlaceholders extends HashMap<String, String> {

    public HologramPlaceholders() {
        super();
    }

    public HologramPlaceholders(Map<String, String> map) {
        super(map);
    }

    public String apply(String input) {
        for (Entry<String, String> entry : entrySet())
            input = input.replace("%" + entry.getKey() + "%", entry.getValue());
        return input;
    }
}
