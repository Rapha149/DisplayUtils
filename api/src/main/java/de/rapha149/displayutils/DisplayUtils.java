package de.rapha149.displayutils;

import de.rapha149.displayutils.version.VersionWrapper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The base class of this api.
 * Please call {@link #init(Plugin)} in your {@link Plugin#onEnable()} method. After that you can use the individual utils.
 */
public class DisplayUtils {

    public static final VersionWrapper wrapper;
    public static Plugin plugin;
    public static List<UUID> joinFinishedPlayers = new ArrayList<>();

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3].substring(1);
        try {
            wrapper = (VersionWrapper) Class.forName(VersionWrapper.class.getPackage().getName() + ".Wrapper" + version).getDeclaredConstructor().newInstance();
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException |
                 InvocationTargetException exception) {
            throw new IllegalStateException("Failed to load support for server version " + version, exception);
        } catch (ClassNotFoundException exception) {
            throw new IllegalStateException("DisplayUtils does not support the server version \"" + version + "\"", exception);
        }
    }

    /**
     * Initializes the api. After calling this method in your {@link Plugin#onEnable()} method you can use the individual utils.
     * @param plugin The Bukkit plugin that is using this api.
     */
    public static void init(Plugin plugin) {
        if (DisplayUtils.plugin != null)
            throw new IllegalStateException("DisplayUtils is already initialized");
        DisplayUtils.plugin = plugin;

        joinFinishedPlayers = Bukkit.getOnlinePlayers().stream().map(Player::getUniqueId).collect(Collectors.toList());
        Bukkit.getPluginManager().registerEvents(new JoinFinishListener(), plugin);

        NPCUtil.init();
        HologramUtil.init();
        SidebarUtil.init();
        TablistUtil.init();
    }

    /**
     * Internal method. Checks if the api is initialized and throws an exception if not.
     */
    static void checkUsable() {
        if (plugin == null)
            throw new IllegalStateException("DisplayUtils is not initialized");
        if (wrapper == null)
            throw new IllegalStateException("DisplayUtils does not support or failed to load support for this server version.");
    }

    /**
     * Internal listener that listens for the player to finish joining the server.
     * Calls the event {@link PlayerJoinFinishEvent} when the player has finished joining.
     */
    private static class JoinFinishListener implements Listener {

        JoinFinishListener() {
        }

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            UUID uuid = player.getUniqueId();
            wrapper.addIncomingPacketHandler(player, "displayutils_joinfinish", packet -> {
                if (!packet.getClass().getSimpleName().equals("PacketPlayInFlying"))
                    return true;

                if (!joinFinishedPlayers.contains(uuid)) {
                    joinFinishedPlayers.add(uuid);
                    Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new PlayerJoinFinishEvent(player)));
                }
                return true;
            });
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            joinFinishedPlayers.remove(event.getPlayer().getUniqueId());
        }
    }

    /**
     * Internal event that is called when a player has finished joining the server.
     */
    public static class PlayerJoinFinishEvent extends PlayerEvent {

        private static final HandlerList handlers = new HandlerList();

        public PlayerJoinFinishEvent(Player who) {
            super(who);
        }

        @Override
        public HandlerList getHandlers() {
            return handlers;
        }

        public static HandlerList getHandlerList() {
            return handlers;
        }
    }
}
