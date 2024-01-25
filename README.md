# DisplayUtils [![Maven Central](https://img.shields.io/maven-central/v/de.rapha149.displayutils/displayutils?label=Maven%20Central)](https://central.sonatype.com/artifact/de.rapha149.displayutils/displayutils) [![Javadoc](https://javadoc.io/badge2/de.rapha149.displayutils/displayutils/Javadoc.svg)](https://javadoc.io/doc/de.rapha149.displayutils/displayutils) 
An api to display various things in Minecraft that normally would need packets. With the api you an spawn NPCs, create holograms and create a lag-free tablist and sidebar!  
The api currently only supports the server version `1.12.2`.

## Integration

Maven dependency:
```xml
<dependency>
    <groupId>de.rapha149.displayutils</groupId>
    <artifactId>displayutils</artifactId>
    <version>1.0</version>
</dependency>
```

## Features

- Detailed and clear documentation as well as javadoc
- Easy to integrate into your plugin

### NPC features
- Easy to create and player specific NPCs without having to bother with packets
- Listener for when a player interacts with an NPC
- Automatically respawning the NPC for players that left the server, changed into another world or went too far away from the NPC
- Set name, skin and location as well as the sneaking, collidable and name tag visible flags
- NPCs looking at each player and matching sneaking with each player individually
- Fixing the bug that the NPC's skin wouldn't show by waiting for the player to fully join and look in the direction of the NPC when spawning it

### Hologram features
- Easy to create and player specific holograms via invisible armor stands
- Dynamic text replacements so, for example, you don't have to recreate the hologram in order to change placeholders
- Different text replacements for each player
- Automatic hologram updates every n ticks

### Sidebar features
- Easy to create and lag-free sidebar (lag-free due to using packets instead of Bukkit scoreboards)
- Dynamic text replacements so, for example, you don't have to recreate the sidebar in order to change placeholders
- Different text replacements for each player
- Automatic sidebar updates every n ticks

### Tablist features
- Easy to create and lag-free tablist (lag-free due to using packets instead of Bukkit scoreboards)
- Create groups of different players (e.g. assigned by rank) and set the order in which they are shown
- Choose whether to sort by name or sort manually within groups
- Adjust team options like friendly fire, see friendly invisibles, collision rule and name tag visibility
- Automatically update tablist when a player joins

## Usage

In order to be able to use the api, you have to call `DisplayUtils#init` in your `Plugin#onEnable` method:
```java
public class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // some code

        DisplayUtils.init(this);

        // some more code
    }
}
```
After you have done that, you can use the individual utils as described in the [wiki](https://github.com/Rapha149/DisplayUtils/wiki).

## Credits
This project's structure was inspired by WesJD's [AnvilGUI](https://github.com/WesJD/AnvilGUI).
