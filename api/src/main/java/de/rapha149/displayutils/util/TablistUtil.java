package de.rapha149.displayutils.util;

import de.rapha149.displayutils.display.scoreboard.TeamOptions;
import de.rapha149.displayutils.display.scoreboard.TeamOptionsBuilder;
import de.rapha149.displayutils.display.tablist.TablistContentProvider;
import de.rapha149.displayutils.display.tablist.TablistGroup;
import de.rapha149.displayutils.version.ScoreboardAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import static de.rapha149.displayutils.util.DisplayUtils.*;

/**
 * Utility class for modifying the tablist.
 */
public class TablistUtil {

    private static final TeamOptions DEFAULT_TEAM_OPTIONS = new TeamOptionsBuilder().build();

    private static TablistContentProvider provider;
    private static Map<String, Entry<TablistGroup, Object>> teams = new HashMap<>();

    /**
     * Internal method.
     */
    static void init() {
        Bukkit.getPluginManager().registerEvents(new TablistListener(), plugin);
    }

    /**
     * @return The current tablist content provider
     */
    public static TablistContentProvider getTablistProvider() {
        return provider;
    }

    /**
     * Sets the tablist content provider.
     * @param provider The new provider.
     */
    public static void setTablistProvider(TablistContentProvider provider) {
        checkUsable();

        TablistUtil.provider = provider;
        updateTablist();
    }

    /**
     * Updates the tablist for all players.
     * You do not have to call this method when a player joins, the tablist is shown to them automatically.
     */
    public static void updateTablist() {
        updateTablist(null);
    }

    /**
     * Internal method to update the tablist for all players. This method is automatically called when a player joins.
     * @param joined The player that joined, or null if the tablist is updated manually.
     */
    private static void updateTablist(Player joined) {
        checkUsable();
        if (provider == null || Bukkit.getOnlinePlayers().isEmpty())
            return;

        List<TablistGroup> content = provider.getTablistContent(Bukkit.getOnlinePlayers());
        {
            List<String> identifiers = content.stream().map(TablistGroup::getIdentifier).collect(Collectors.toList());
            if (identifiers.size() > new HashSet<>(identifiers).size())
                throw new IllegalArgumentException("The identifiers of the tablist groups must be unique");
        }

        List<TablistGroup> groups = new ArrayList<>();
        List<String> existingPlayers = new ArrayList<>();
        for (TablistGroup group : content) {
            if (group.isCustomPlayerOrder()) {
                List<String> players = group.getPlayers();
                for (int i = 0; i < players.size(); i++) {
                    groups.add(new TablistGroup(
                            group.getIdentifier() + "-pos" + i,
                            group.getTeamOptions(),
                            Collections.singletonList(players.get(i)),
                            false
                    ));
                }
            } else {
                groups.add(new TablistGroup(
                        group.getIdentifier() + "-pos0",
                        group.getTeamOptions(),
                        group.getPlayers(),
                        false
                ));
            }

            existingPlayers.addAll(group.getPlayers());
        }

        List<String> remainingPlayers = Bukkit.getOnlinePlayers().stream().map(Player::getName)
                .filter(player -> !existingPlayers.contains(player)).collect(Collectors.toList());
        if (!remainingPlayers.isEmpty()) {
            groups.add(new TablistGroup(
                    "remaining", // this can not be a duplicate identifiers because other identifiers are suffixed with "-pos"
                    DEFAULT_TEAM_OPTIONS,
                    remainingPlayers,
                    false
            ));
        }

        Map<TablistGroup, String> groupMap = new HashMap<>();
        List<Object> packets = new ArrayList<>();
        List<Object> packetsJoined = joined != null ? new ArrayList<>() : null;

        int length = String.valueOf(groups.size()).length();
        for (int i = 0; i < groups.size(); i++)
            groupMap.put(groups.get(i), String.format("%0" + length + "d", i));

        List<String> identifiers = groups.stream().map(TablistGroup::getIdentifier).collect(Collectors.toList());
        teams.entrySet().removeIf(entry -> {
            if (!identifiers.contains(entry.getKey())) {
                packets.add(wrapper.getTeamActionPacket(entry.getValue().getValue(), ScoreboardAction.REMOVE));
                return true;
            }
            return false;
        });

        Random random = new Random();
        Object scoreboard = wrapper.newScoreboard();
        groupMap.forEach((group, position) -> {
            String identifier = group.getIdentifier();
            Entry<TablistGroup, Object> entry = teams.get(identifier);

            boolean created = false;
            Object team;
            List<String> playersToAdd = new ArrayList<>(group.getPlayers());
            List<String> playersToRemove = Collections.emptyList();

            if (entry == null || !wrapper.getTeamName(entry.getValue()).startsWith(position)) {
                created = true;
                if (entry != null)
                    packets.add(wrapper.getTeamActionPacket(entry.getValue(), ScoreboardAction.REMOVE));

                List<String> existingNames = teams.values().stream().map(e -> wrapper.getTeamName(e.getValue())).collect(Collectors.toList());
                String name;
                do {
                    name = position + random.nextInt(1000000);
                } while (existingNames.contains(name));

                team = wrapper.newTeam(scoreboard, name);
                teams.put(identifier, new SimpleEntry<>(group, team));
            } else {
                team = entry.getValue();

                TablistGroup previousGroup = entry.getKey();
                playersToAdd.removeAll(previousGroup.getPlayers());
                playersToRemove = new ArrayList<>(previousGroup.getPlayers());
                playersToRemove.removeAll(group.getPlayers());
            }

            wrapper.setTeamOptions(team, group.getTeamOptions());
            packets.add(wrapper.getTeamActionPacket(team, created ? ScoreboardAction.CREATE : ScoreboardAction.UPDATE));
            if (!playersToAdd.isEmpty())
                packets.add(wrapper.getTeamPlayerActionPacket(team, playersToAdd, true));
            if (!playersToRemove.isEmpty())
                packets.add(wrapper.getTeamPlayerActionPacket(team, playersToRemove, false));

            if (joined != null) {
                packetsJoined.add(wrapper.getTeamActionPacket(team, ScoreboardAction.CREATE));
                if (!group.getPlayers().isEmpty())
                    packetsJoined.add(wrapper.getTeamPlayerActionPacket(team, group.getPlayers(), true));
            }
        });

        for (Player player : Bukkit.getOnlinePlayers())
            wrapper.sendPackets(player, player.equals(joined) ? packetsJoined : packets);
    }

    /**
     * Internal listener that handles player joins.
     */
    private static class TablistListener implements Listener {

        @EventHandler
        public void onJoin(PlayerJoinEvent event) {
            updateTablist(event.getPlayer());
        }
    }
}
