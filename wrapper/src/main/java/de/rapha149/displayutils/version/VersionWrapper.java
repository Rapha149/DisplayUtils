package de.rapha149.displayutils.version;

import de.rapha149.displayutils.utilityclasses.npc.NPC;
import de.rapha149.displayutils.utilityclasses.scoreboard.TeamOptions;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for version wrappers
 */
public interface VersionWrapper {

    void sendPackets(Player player, List<Object> packets);

    /**
     * Adds a packet listener to the player's outgoing packet pipeline.
     *
     * @param player      The player to add the listener to
     * @param handlerName The name of the handler (must be unique)
     * @param listener    The listener to add (returning false will cancel the packet being sent to the player)
     */
    void addOutgoingPacketHandler(Player player, String handlerName, Predicate<Object> listener);

    /**
     * Adds a packet listener to the player's incoming packet pipeline.
     *
     * @param player      The player to add the listener to
     * @param handlerName The name of the handler (must be unique)
     * @param listener    The listener to add (returning false will cancel the packet being received by the server)
     */
    void addIncomingPacketHandler(Player player, String handlerName, Predicate<Object> listener);

    Object newScoreboard();

    Object newObjective(Object scoreboard, String name, String displayName);

    Object newTeam(Object scoreboard, String name);

    String getTeamName(Object team);

    TeamOptions getTeamOptions(Object team);

    void setTeamOptions(Object team, TeamOptions options);

    Object getTeamActionPacket(Object team, ScoreboardAction action);

    Object getTeamPlayerActionPacket(Object team, List<String> players, boolean add);

    Object getObjectiveActionPacket(Object objective, ScoreboardAction action);

    Object getDisplayObjectivePacket(Object objective, ScoreboardPosition position);

    Object getChangeScorePacket(Object scoreboard, Object objective, String name, int value);

    Object getRemoveScorePacket(Object objective, String name);

    Object getSpawnLivingEntityPacket(LivingEntity entity);

    Object getEntityMetadataPacket(LivingEntity entity);

    Object getDestroyEntitiesPacket(List<LivingEntity> entities);

    ArmorStand createArmorStand(Location loc);

    Player createPlayer(NPC npc);

    void setSneaking(Player player, boolean sneaking);

    Object getPlayerInfoPacket(Player player, boolean add);

    Object getNamedEntitySpawnPacket(Player player);

    Object getEntityHeadRotationPacket(Entity entity, float yaw);

    List<Object> getUpdateRotationPackets(Entity entity, float yaw, float pitch);

    Integer getIdFromUseEntityPacket(Object packet);

    UseEntityAction getActionFromUseEntityPacket(Object packet);
}
