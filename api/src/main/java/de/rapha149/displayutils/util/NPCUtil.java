package de.rapha149.displayutils.util;

import de.rapha149.displayutils.display.npc.NPC;
import de.rapha149.displayutils.display.scoreboard.TeamOptionsBuilder;
import de.rapha149.displayutils.display.scoreboard.TeamOptionStatus;
import de.rapha149.displayutils.version.ScoreboardAction;
import de.rapha149.displayutils.version.UseEntityAction;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.*;
import java.util.stream.Collectors;

import static de.rapha149.displayutils.util.DisplayUtils.*;

/**
 * Utility class for creating NPCs.
 */
public class NPCUtil {

    private static List<NPCTeam> teams;

    private static Map<String, NPCData> npcs = new HashMap<>();
    private static Map<String, NPCData> lookAtPlayerNPCs = new HashMap<>();
    private static Map<String, NPCData> matchSneakingNPCs = new HashMap<>();

    /**
     * Internal method.
     */
    static void init() {
        Object scoreboard = wrapper.newScoreboard();
        teams = Arrays.asList(
                new NPCTeam(true, true, wrapper.newTeam(scoreboard, "npc_normal")),
                new NPCTeam(false, true, wrapper.newTeam(scoreboard, "npc_nocoll")),
                new NPCTeam(true, false, wrapper.newTeam(scoreboard, "npc_notag")),
                new NPCTeam(false, false, wrapper.newTeam(scoreboard, "npc_nocoll_notag"))
        );
        teams.forEach(team -> {
            TeamOptionsBuilder builder = new TeamOptionsBuilder();
            if (!team.collidable)
                builder.setCollisionRule(TeamOptionStatus.NEVER);
            if (!team.nameTagVisible)
                builder.setNameTagVisibility(TeamOptionStatus.NEVER);
            wrapper.setTeamOptions(team.team, builder.build());
        });

        Bukkit.getPluginManager().registerEvents(new NPCListener(), plugin);
    }

    /**
     * Adds an NPC to the server.
     * The NPC will automatically be shown to all currently online players and players that join in the future (as long as the npc is configured to be shown to them).
     * The NPC is not persistent. After a reload/restart it will have to be added again.
     *
     * @param npc The NPC to add. Create with {@link de.rapha149.displayutils.display.npc.NPCBuilder}.
     */
    public static void addNPC(NPC npc) {
        checkUsable();

        String identifier = npc.getIdentifier();
        if (npcs.containsKey(identifier))
            throw new IllegalArgumentException("NPC with identifier " + identifier + " already exists");

        Player npcPlayer = wrapper.createPlayer(npc);
        wrapper.setSneaking(npcPlayer, npc.isSneaking());

        NPCData data = new NPCData(npc, npcPlayer);
        npcs.put(identifier, data);

        if (npc.isLookAtPlayer())
            lookAtPlayerNPCs.put(identifier, data);
        if (npc.isMatchSneakingWithPlayer())
            matchSneakingNPCs.put(identifier, data);

        Bukkit.getOnlinePlayers().forEach(data::updateVisibility);
    }

    /**
     * Removes an NPC from the server.
     * @param identifier The identifier of the NPC to remove.
     * @return True if an NPC with that identifier existed and was removed, false otherwise.
     */
    public static boolean removeNPC(String identifier) {
        checkUsable();

        NPCData data = npcs.remove(identifier);
        if (data == null)
            return false;

        lookAtPlayerNPCs.remove(identifier);
        matchSneakingNPCs.remove(identifier);

        List<Object> packets = Collections.singletonList(wrapper.getDestroyEntitiesPacket(Collections.singletonList(data.npcPlayer)));
        for (UUID uuid : data.currentPlayers) {
            Player player = Bukkit.getPlayer(uuid);
            if (player != null)
                wrapper.sendPackets(player, packets);
        }

        return true;
    }

    /**
     * Checks whether an NPC with the given identifier exists.
     * @param identifier The identifier to check.
     * @return True if an NPC with that identifier exists, false otherwise.
     */
    public static boolean isNPC(String identifier) {
        return npcs.containsKey(identifier);
    }

    /**
     * @return A set containing the identifiers of all npcs.
     */
    public static Set<String> getNPCIdentifiers() {
        return Collections.unmodifiableSet(npcs.keySet());
    }

    /**
     * Internal listener for npc events.
     */
    private static class NPCListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            wrapper.sendPackets(player, teams.stream().map(team -> wrapper.getTeamActionPacket(team.team, ScoreboardAction.CREATE)).collect(Collectors.toList()));

            UUID uuid = player.getUniqueId();
            wrapper.addIncomingPacketHandler(player, "displayutils_npcutil", packet -> {
                if (!packet.getClass().getSimpleName().equals("PacketPlayInUseEntity"))
                    return true;

                Integer id = wrapper.getIdFromUseEntityPacket(packet);
                if (id != null && wrapper.getActionFromUseEntityPacket(packet) != UseEntityAction.ATTACK) {
                    npcs.values().stream().filter(data -> data.npcPlayer.getEntityId() == id).findFirst().ifPresent(data -> {
                        if (data.cooldown.contains(uuid))
                            return;

                        data.cooldown.add(uuid);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> data.cooldown.remove(uuid), data.npc.getListenerCooldown());

                        data.npc.getUseListener().onUseNPC(player);
                    });
                }

                return true;
            });
        }

        @EventHandler
        public void onFinishJoin(PlayerJoinFinishEvent event) {
            Player player = event.getPlayer();
            npcs.values().forEach(data -> data.updateVisibility(player));
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            UUID uuid = event.getPlayer().getUniqueId();
            npcs.values().forEach(data -> data.currentPlayers.remove(uuid));
        }

        @EventHandler
        public void onMove(PlayerMoveEvent event) {
            onMove(event.getPlayer());
        }

        @EventHandler
        public void onTeleport(PlayerTeleportEvent event) {
            Bukkit.getScheduler().runTask(plugin, () -> onMove(event.getPlayer()));
        }

        @EventHandler
        public void onRespawn(PlayerRespawnEvent event) {
            Bukkit.getScheduler().runTask(plugin, () -> onMove(event.getPlayer()));
        }

        private void onMove(Player player) {
            List<NPCData> changed = npcs.values().stream().filter(data -> data.updateVisibility(player)).collect(Collectors.toList());
            for (NPCData npc : lookAtPlayerNPCs.values())
                if (!changed.contains(npc))
                    npc.lookAt(player);
        }

        @EventHandler
        public void onToggleSneak(PlayerToggleSneakEvent event) {
            Player player = event.getPlayer();
            boolean sneaking = event.isSneaking();
            for (NPCData npc : matchSneakingNPCs.values())
                npc.matchSneaking(player, sneaking);
        }
    }

    /**
     * Internal class that stores data about an NPC.
     */
    private static class NPCData {

        final NPC npc;
        final Player npcPlayer;
        final List<UUID> currentPlayers = new ArrayList<>();
        final List<UUID> cooldown = new ArrayList<>();

        NPCData(NPC npc, Player npcPlayer) {
            this.npc = npc;
            this.npcPlayer = npcPlayer;
        }

        boolean isInRange(Player player) {
            if (!player.getWorld().getUID().equals(npc.getLoc().getWorld().getUID()))
                return false;

            return player.getLocation().distanceSquared(npc.getLoc()) <= 10000;
        }

        boolean isInView(Player player) {
            if (!player.getWorld().getUID().equals(npc.getLoc().getWorld().getUID()))
                return false;
            if (player.getLocation().distanceSquared(npc.getLoc()) < 0.01)
                return true;

            Location eye = player.getEyeLocation();
            return npc.getLoc().toVector().subtract(eye.toVector()).normalize().dot(eye.getDirection()) > 0.5;
        }

        /**
         * Spawns or despawns the NPC for the given player.
         *
         * @param player The player to update the visibility for.
         * @return Whether changes were made.
         */
        boolean updateVisibility(Player player) {
            UUID uuid = player.getUniqueId();
            if (!joinFinishedPlayers.contains(uuid))
                return false;
            if (!npc.isShownForPlayer(uuid))
                return false;

            if (currentPlayers.contains(uuid)) {
                if (!isInRange(player)) {
                    currentPlayers.remove(uuid);
                    wrapper.sendPackets(player, Collections.singletonList(wrapper.getDestroyEntitiesPacket(Collections.singletonList(npcPlayer))));
                    return true;
                }
            } else if (isInRange(player) && isInView(player)) {
                currentPlayers.add(uuid);
                wrapper.sendPackets(player, Arrays.asList(
                        wrapper.getPlayerInfoPacket(npcPlayer, true),
                        wrapper.getNamedEntitySpawnPacket(npcPlayer),
                        wrapper.getEntityMetadataPacket(npcPlayer),
                        wrapper.getEntityHeadRotationPacket(npcPlayer, npc.getLoc().getYaw()),
                        wrapper.getTeamPlayerActionPacket(teams.stream()
                                        .filter(team -> team.collidable == npc.isCollidable() && team.nameTagVisible == npc.isNameTagVisible())
                                        .findFirst().orElseThrow(() -> new IllegalStateException("No team found")).team,
                                Collections.singletonList(npc.getName()), true)
                ));

                if (npc.isLookAtPlayer())
                    lookAt(player);
                if (npc.isMatchSneakingWithPlayer())
                    matchSneaking(player, player.isSneaking());

                Bukkit.getScheduler().runTaskLater(plugin, () ->
                        wrapper.sendPackets(player, Collections.singletonList(wrapper.getPlayerInfoPacket(npcPlayer, false))), 20);
                return true;
            }

            return false;
        }

        void lookAt(Player player) {
            if (!currentPlayers.contains(player.getUniqueId()))
                return;

            Location loc = npcPlayer.getLocation().setDirection(player.getEyeLocation().subtract(npcPlayer.getEyeLocation()).toVector());
            wrapper.sendPackets(player, wrapper.getUpdateRotationPackets(npcPlayer, loc.getYaw(), loc.getPitch()));
        }

        void matchSneaking(Player player, boolean sneaking) {
            if (!currentPlayers.contains(player.getUniqueId()))
                return;

            wrapper.setSneaking(npcPlayer, sneaking);
            wrapper.sendPackets(player, Collections.singletonList(wrapper.getEntityMetadataPacket(npcPlayer)));
            wrapper.setSneaking(npcPlayer, npc.isSneaking());
        }
    }

    /**
     * Internal class that stores data about a npc team for the "collidable" and "nameTagVisible" flags.
     */
    private static class NPCTeam {

        final boolean collidable;
        final boolean nameTagVisible;
        final Object team;

        NPCTeam(boolean collidable, boolean nameTagVisible, Object team) {
            this.collidable = collidable;
            this.nameTagVisible = nameTagVisible;
            this.team = team;
        }
    }
}
