package de.rapha149.displayutils.util;

import de.rapha149.displayutils.display.hologram.Hologram;
import de.rapha149.displayutils.display.hologram.HologramPlaceholders;
import de.rapha149.displayutils.util.HologramUtil.HologramData.GeneralHologramData;
import de.rapha149.displayutils.util.HologramUtil.HologramData.PlayerHologramData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static de.rapha149.displayutils.util.DisplayUtils.*;


public class HologramUtil {

    private static Map<String, HologramData> holograms = new HashMap<>();

    /**
     * Internal method.
     */
    static void init() {
        Bukkit.getPluginManager().registerEvents(new HologramListener(), plugin);
    }

    /**
     * Adds a hologram to the server.
     * The hologram will automatically be shown to all currently online players and players that join in the future (as long as the npc is configured to be shown to them).
     * The hologram is not persistent. After a reload/restart it will have to be added again.
     *
     * @param hologram The hologram to add.
     */
    public static void addHologram(Hologram hologram) {
        checkUsable();

        String identifier = hologram.getIdentifier();
        if (holograms.containsKey(identifier))
            throw new IllegalArgumentException("Hologram with identifier " + identifier + " already exists");

        List<ArmorStand> armorStands = new ArrayList<>();
        Location currentLoc = hologram.getLoc().clone().subtract(0, 0.5, 0);
        for (int i = 0; i < hologram.getLines().size(); i++) {
            ArmorStand armorStand = wrapper.createArmorStand(currentLoc);
            armorStand.setCustomNameVisible(true);
            armorStand.setVisible(false);
            armorStand.setMarker(true);
            armorStand.setBasePlate(false);
            armorStands.add(armorStand);

            currentLoc.subtract(0, 0.25, 0);
        }

        Integer updateInterval = hologram.getUpdateInterval();
        BukkitTask task = updateInterval == null ? null : Bukkit.getScheduler().runTaskTimer(plugin,
                () -> updateHologram(identifier), updateInterval, updateInterval);

        holograms.put(identifier, hologram.isPlayerSpecific() ? new PlayerHologramData(hologram, armorStands, task) :
                new GeneralHologramData(hologram, armorStands, task));
        updateHologram(identifier);
    }

    /**
     * Removes a hologram from the server.
     * @param identifier The identifier of the hologram to remove.
     */
    public static void removeHologram(String identifier) {
        checkUsable();

        HologramData data = holograms.remove(identifier);
        if (data == null)
            return;

        if (data.task != null)
            data.task.cancel();

        List<Object> packets = Collections.singletonList(getDestroyPacket(data));
        Bukkit.getOnlinePlayers().forEach(player -> wrapper.sendPackets(player, packets));
    }

    /**
     * Updates all holograms.
     * This will call the general / player content modifier and the player supplier if it exists of each hologram and send the updated lines to all players that can see the hologram.
     */
    public static void updateHolograms() {
        checkUsable();
        holograms.values().forEach(HologramUtil::updateHologram);
    }

    /**
     * Updates a specific hologram.
     * This will call the general / player content modifier and the player supplier if it exists of each hologram and send the updated lines to all players that can see the hologram.
     *
     * @param identifier The identifier of the hologram to update.
     */
    public static void updateHologram(String identifier) {
        checkUsable();
        if (holograms.containsKey(identifier))
            updateHologram(holograms.get(identifier));
    }

    /**
     * Updates a hologram.
     * @param data The data of the hologram to update.
     */
    private static void updateHologram(HologramData data) {
        Hologram hologram = data.hologram;
        List<UUID> allowedPlayers = hologram.getPlayers();
        boolean filterPlayers = allowedPlayers != null;

        if (filterPlayers && !data.initializedPlayers.isEmpty()) {
            List<Object> packets = Collections.singletonList(getDestroyPacket(data));
            data.initializedPlayers.removeIf(uuid -> {
                if (!allowedPlayers.contains(uuid)) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null)
                        wrapper.sendPackets(player, packets);
                    return true;
                }

                return false;
            });
        }

        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(player -> isInCorrectWorld(player, hologram) &&
                                                                                   (!filterPlayers || allowedPlayers.contains(player.getUniqueId()))).collect(Collectors.toList());
        if (players.isEmpty())
            return;

        Map<Player, List<Object>> packets = new HashMap<>();
        boolean playerSpecific = hologram.hasPlayerModifier();
        List<String> lines = hologram.getGeneralModifier() != null ? hologram.getGeneralModifier().modify(hologram.getLines()) : hologram.getLines();

        List<Player> uninitialized = players.stream().filter(player -> !data.initializedPlayers.contains(player.getUniqueId())).collect(Collectors.toList());
        if (!uninitialized.isEmpty()) {
            List<Object> initPackets = new ArrayList<>();
            for (int i = 0; i < data.armorStands.size(); i++) {
                ArmorStand armorStand = data.armorStands.get(i);
                initPackets.add(wrapper.getSpawnLivingEntityPacket(armorStand));
                if (!playerSpecific) {
                    armorStand.setCustomName(generalLines.get(i));
                    initPackets.add(wrapper.getEntityMetadataPacket(armorStand));
                }
            }

            for (Player player : uninitialized) {
                packets.put(player, initPackets);
                data.initializedPlayers.add(player.getUniqueId());
            }
        }

        if (playerSpecific) {
            Map<UUID, List<String>> previousPlayerLines = ((PlayerHologramData) data).previousLines;
            for (Player player : players) {
                UUID uuid = player.getUniqueId();

                List<String> playerLines = hologram.getPlayerModifier().modify(player, lines);
                List<String> previousLines = previousPlayerLines.get(uuid);

                for (int i = 0; i < playerLines.size(); i++) {
                    String line = playerLines.get(i);
                    if (previousLines == null || !previousLines.get(i).equals(line)) {
                        ArmorStand armorStand = data.armorStands.get(i);
                        armorStand.setCustomName(line);
                        packets.computeIfAbsent(player, p -> new ArrayList<>()).add(wrapper.getEntityMetadataPacket(armorStand));
                    }
                }
            }
        } else {
            GeneralHologramData generalData = (GeneralHologramData) data;
            List<String> previousLines = generalData.previousLines;

            List<Object> generalPackets = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (previousLines == null || !previousLines.get(i).equals(line)) {
                    ArmorStand armorStand = data.armorStands.get(i);
                    armorStand.setCustomName(line);
                    generalPackets.add(wrapper.getEntityMetadataPacket(armorStand));
                }
            }

            for (Player player : players) {
                if (!packets.containsKey(player)) // previously uninitialized players are already in this map and don't need this packets
                    packets.put(player, generalPackets);
            }

            generalData.previousLines = lines;
        }

        packets.forEach(wrapper::sendPackets);
    }

    /**
     * Respawns a hologram for a player.
     * @param player The player to respawn the hologram for.
     * @param data The data of the hologram to respawn.
     */
    private static void respawnHologram(Player player, HologramData data) {
        Hologram hologram = data.hologram;
        if (!isInCorrectWorld(player, hologram))
            return;

        UUID uuid = player.getUniqueId();
        List<UUID> players = hologram.getPlayers();
        if (players != null && players.contains(uuid))
            return;

        boolean playerSpecific = hologram.hasPlayerModifier();
        if (playerSpecific && !data.initializedPlayers.contains(uuid)) {
            updateHologram(data);
            return;
        }

        List<String> lines = playerSpecific ? ((PlayerHologramData) data).previousLines.get(uuid) : ((GeneralHologramData) data).previousLines;
        if (lines == null) {
            data.initializedPlayers.remove(uuid);
            updateHologram(data);
            return;
        }

        List<Object> packets = new ArrayList<>();
        for (int i = 0; i < data.armorStands.size(); i++) {
            ArmorStand armorStand = data.armorStands.get(i);
            armorStand.setCustomName(lines.get(i));
            packets.add(wrapper.getSpawnLivingEntityPacket(armorStand));
        }

        wrapper.sendPackets(player, packets);
        if (!data.initializedPlayers.contains(uuid))
            data.initializedPlayers.add(uuid);
    }

    /**
     * Checks if a player is in the same world as a hologram.
     * @param player The player to check.
     * @param hologram The hologram to check.
     * @return Whether the player is in the same world as the hologram.
     */
    private static boolean isInCorrectWorld(Player player, Hologram hologram) {
        return player.getWorld().getUID().equals(hologram.getLoc().getWorld().getUID());
    }

    /**
     * Creates a destroy entities packet for a hologram.
     * @param data The data of the hologram to create the packet for.
     * @return The created packet.
     */
    private static Object getDestroyPacket(HologramData data) {
        return wrapper.getDestroyEntitiesPacket(data.armorStands.stream().map(LivingEntity.class::cast).collect(Collectors.toList()));
    }

    /**
     * Internal listener that handles player joins, quits and world changes.
     */
    private static class HologramListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            updateHolograms();
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            UUID uuid = event.getPlayer().getUniqueId();
            for (HologramData data : holograms.values()) {
                data.initializedPlayers.remove(uuid);
                if (data.hologram.hasPlayerModifier())
                    ((PlayerHologramData) data).previousLines.remove(uuid);
            }
        }

        @EventHandler
        public void onChangedWorld(PlayerChangedWorldEvent event) {
            Player player = event.getPlayer();
            for (HologramData data : holograms.values()) {
                if (!isInCorrectWorld(player, data.hologram)) {
                    data.initializedPlayers.remove(player.getUniqueId());
                    continue;
                }

                respawnHologram(player, data);
            }
        }
    }

    /**
     * Internal class that holds data about a hologram.
     */
    static class HologramData {

        final Hologram hologram;
        final List<ArmorStand> armorStands;
        final BukkitTask task;
        final List<UUID> initializedPlayers = new ArrayList<>();

        HologramData(Hologram hologram, List<ArmorStand> armorStands, BukkitTask task) {
            this.hologram = hologram;
            this.armorStands = armorStands;
            this.task = task;
        }

        static class GeneralHologramData extends HologramData {

            List<String> previousLines;

            GeneralHologramData(Hologram hologram, List<ArmorStand> armorStands, BukkitTask task) {
                super(hologram, armorStands, task);
            }
        }

        static class PlayerHologramData extends HologramData {

            final Map<UUID, List<String>> previousLines = new HashMap<>();

            PlayerHologramData(Hologram hologram, List<ArmorStand> armorStands, BukkitTask task) {
                super(hologram, armorStands, task);
            }
        }
    }
}
