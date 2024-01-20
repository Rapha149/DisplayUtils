package de.rapha149.displayutils.utilityclasses.sidebar;

import java.util.HashMap;
import java.util.Map;

public class SidebarPlaceholders extends HashMap<String, String> {

    public SidebarPlaceholders() {
        super();
    }

    public SidebarPlaceholders(Map<String, String> map) {
        super(map);
    }

    public String apply(String input) {
        for (Entry<String, String> entry : entrySet())
            input = input.replace("%" + entry.getKey() + "%", entry.getValue());
        return input;
    }
}
