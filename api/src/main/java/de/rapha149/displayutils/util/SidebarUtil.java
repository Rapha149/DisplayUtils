package de.rapha149.displayutils.util;

import de.rapha149.displayutils.display.scoreboard.TeamOptionsBuilder;
import de.rapha149.displayutils.display.sidebar.Sidebar;
import de.rapha149.displayutils.version.ScoreboardAction;
import de.rapha149.displayutils.version.ScoreboardPosition;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static de.rapha149.displayutils.util.DisplayUtils.*;

public class SidebarUtil {

    private static final Pattern STARTSWITH_COLOR_CHAR = Pattern.compile("^[0-9a-fk-or]", Pattern.CASE_INSENSITIVE);
    private static final Pattern STARTSWITH_NEW_COLOR = Pattern.compile("^§[0-9a-fr]", Pattern.CASE_INSENSITIVE); // does not check for bold, italic, etc. because they don't reset the formatting
    private static final List<String> SCORE_NAMES = Arrays.asList("§0", "§1", "§2", "§3", "§4", "§5", "§6", "§7", "§8", "§9", "§a", "§b", "§c", "§d", "§e");
    private static List<Object> teams;

    private static Sidebar sidebar;
    private static BukkitTask task;
    private static List<UUID> initializedPlayers = new ArrayList<>();

    private static List<String> previousGeneralLines;
    private static Map<UUID, List<String>> previousPlayerLines;

    /**
     * Internal method.
     */
    static void init() {
        Bukkit.getPluginManager().registerEvents(new SidebarListener(), plugin);
    }

    /**
     * Sets the sidebar for all players. Overwrites the previous sidebar if there was one.
     * @param sidebar The sidebar to set. Create with {@link de.rapha149.displayutils.display.sidebar.SidebarBuilder}.
     */
    public static void setSidebar(Sidebar sidebar) {
        checkUsable();

        removeSidebar();
        SidebarUtil.sidebar = sidebar;
        if (sidebar.hasPlayerModifier())
            previousPlayerLines = new HashMap<>();
        updateSidebar();

        Integer updateInterval = sidebar.getUpdateInterval();
        if (updateInterval != null)
            task = Bukkit.getScheduler().runTaskTimer(plugin, SidebarUtil::updateSidebar, updateInterval, updateInterval);
    }

    /**
     * Removes the sidebar for all players.
     */
    public static void removeSidebar() {
        checkUsable();

        sidebar = null;
        if (task != null) {
            task.cancel();
            task = null;
        }
        initializedPlayers.clear();
        previousGeneralLines = null;
        previousPlayerLines = null;

        // remove all scores and teams
        List<Object> packets = getRemovePackets();
        Bukkit.getOnlinePlayers().forEach(player -> wrapper.sendPackets(player, packets));
    }

    /**
     * Updates the sidebar for all players.
     */
    public static void updateSidebar() {
        checkUsable();
        if (sidebar == null || Bukkit.getOnlinePlayers().isEmpty())
            return;

        List<UUID> allowedPlayers = sidebar.getPlayers();
        boolean filterPlayers = allowedPlayers != null;
        if (filterPlayers && !initializedPlayers.isEmpty()) {
            List<Object> packets = getRemovePackets();
            initializedPlayers.removeIf(uuid -> {
                if (!allowedPlayers.contains(uuid)) {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null)
                        wrapper.sendPackets(player, packets);
                    return true;
                }

                return false;
            });
        }

        List<Player> players = Bukkit.getOnlinePlayers().stream().filter(player -> !filterPlayers || allowedPlayers.contains(player.getUniqueId())).collect(Collectors.toList());
        if (players.isEmpty())
            return;

        boolean playerSpecific = sidebar.hasPlayerModifier();
        Map<Player, List<Object>> packets = new HashMap<>();
        Object scoreboard = wrapper.newScoreboard();

        List<String> lines = sidebar.getGeneralModifier() != null ? Collections.unmodifiableList(sidebar.getGeneralModifier().modify(sidebar.getLines())) : sidebar.getLines();
        List<Player> uninitialized = players.stream().filter(player -> !initializedPlayers.contains(player.getUniqueId())).collect(Collectors.toList());
        if (!uninitialized.isEmpty()) {
            String title = sidebar.getTitle();
            int maxTitleLength = wrapper.getMaxObjetiveDisplayNameLength();
            if (title.length() > maxTitleLength) {
                String newTitle = title.substring(0, maxTitleLength);
                if (newTitle.endsWith("§") && STARTSWITH_COLOR_CHAR.matcher(title.substring(maxTitleLength)).find())
                    newTitle = newTitle.substring(0, newTitle.length() - 1);
                title = newTitle;
            }

            Object objective = wrapper.newObjective(scoreboard, "sidebar", title);

            List<Object> initPackets = new ArrayList<>(Arrays.asList(
                    wrapper.getObjectiveActionPacket(objective, ScoreboardAction.CREATE),
                    wrapper.getDisplayObjectivePacket(objective, ScoreboardPosition.SIDEBAR)
            ));
            for (int i = 0; i < lines.size(); i++) {
                String name = SCORE_NAMES.get(i);
                initPackets.add(wrapper.getChangeScorePacket(scoreboard, objective, name, lines.size() - i));

                Object team = getTeam(scoreboard, i, playerSpecific ? "" : lines.get(i));
                initPackets.add(wrapper.getTeamActionPacket(team, ScoreboardAction.CREATE));
                initPackets.add(wrapper.getTeamPlayerActionPacket(team, Collections.singletonList(name), true));
            }

            for (Player player : uninitialized) {
                packets.put(player, new ArrayList<>(initPackets));
                initializedPlayers.add(player.getUniqueId());
            }
        }

        if (playerSpecific) {
            for (Player player : players) {
                UUID uuid = player.getUniqueId();

                List<String> playerLines = sidebar.getPlayerModifier().modify(player, lines);
                List<String> previousLines = previousPlayerLines.get(uuid);

                for (int i = 0; i < playerLines.size(); i++) {
                    String line = playerLines.get(i);
                    if (previousLines == null || !previousLines.get(i).equals(line))
                        packets.computeIfAbsent(player, p -> new ArrayList<>()).add(wrapper.getTeamActionPacket(getTeam(scoreboard, i, line), ScoreboardAction.UPDATE));
                }

                previousPlayerLines.put(uuid, playerLines);
            }
        } else {
            List<Object> generalPackets = new ArrayList<>();
            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i);
                if (previousGeneralLines == null || !previousGeneralLines.get(i).equals(line))
                    generalPackets.add(wrapper.getTeamActionPacket(getTeam(scoreboard, i, line), ScoreboardAction.UPDATE));
            }

            for (Player player : players) {
                if (!packets.containsKey(player)) // previously uninitialized players are already in this map and don't need this packets
                    packets.put(player, generalPackets);
            }

            previousGeneralLines = lines;
        }

        packets.forEach(wrapper::sendPackets);
    }

    /**
     * Internal method that creates a team for the given line.
     * @param scoreboard The scoreboard to create the team for.
     * @param index The index of the line.
     * @param line The line.
     * @return The created team.
     */
    private static Object getTeam(Object scoreboard, int index, String line) {
        if (teams == null) {
            teams = new ArrayList<>();
            for (int i = 0; i < 15; i++)
                teams.add(wrapper.newTeam(scoreboard, "sidebar_" + i));
        }

        int length = line.length(), maxLength = wrapper.getMaxTeamPrefixSuffixLength();
        String prefix = line.substring(0, Math.min(length, maxLength));
        String suffix;
        if (length > maxLength) {
            suffix = line.substring(maxLength);
            if (prefix.endsWith("§") && STARTSWITH_COLOR_CHAR.matcher(suffix).find()) {
                prefix = prefix.substring(0, prefix.length() - 1);
                suffix = "§" + suffix;
            }
            if (!STARTSWITH_NEW_COLOR.matcher(suffix).find()) {
                String lastColors = ChatColor.getLastColors(prefix);
                suffix = (lastColors.isEmpty() ? "§r" : "") + suffix;
            }
            if (suffix.length() > maxLength) {
                String newSuffix = suffix.substring(0, maxLength);
                if (newSuffix.endsWith("§") && STARTSWITH_COLOR_CHAR.matcher(suffix.substring(maxLength)).find())
                    newSuffix = newSuffix.substring(0, newSuffix.length() - 1);
                suffix = newSuffix;
            }
        } else
            suffix = "";

        Object team = teams.get(index);
        wrapper.setTeamOptions(team, new TeamOptionsBuilder()
                .setPrefix(prefix)
                .setSuffix(suffix)
                .build());
        return team;
    }

    /**
     * Internal method. Returns the packets to remove the sidebar.
     * @return A list of packets to remove the sidebar.
     */
    private static List<Object> getRemovePackets() {
        List<Object> packets = new ArrayList<>();
        Object scoreboard = wrapper.newScoreboard();
        packets.add(wrapper.getObjectiveActionPacket(wrapper.newObjective(scoreboard, "sidebar", null), ScoreboardAction.REMOVE));
        if (teams != null) {
            for (Object team : teams)
                packets.add(wrapper.getTeamActionPacket(team, ScoreboardAction.REMOVE));
        }
        return packets;
    }

    /**
     * Internal listener that handles player joins and quits.
     */
    private static class SidebarListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            updateSidebar();
        }

        @EventHandler
        public void onQuit(PlayerQuitEvent event) {
            if (sidebar == null)
                return;

            UUID uuid = event.getPlayer().getUniqueId();
            initializedPlayers.remove(uuid);
            if (sidebar.hasPlayerModifier())
                previousPlayerLines.remove(uuid);
        }
    }
}
