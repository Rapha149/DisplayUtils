package de.rapha149.displayutils.version;

import de.rapha149.displayutils.display.npc.NPC;
import de.rapha149.displayutils.display.scoreboard.TeamOptions;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Predicate;

/**
 * Interface for version wrappers.
 */
public interface VersionWrapper {

    /**
     * Sends the packets to the player.
     * @param player The player to send the packets to.
     * @param packets The packets to send.
     */
    void sendPackets(Player player, List<Object> packets);

    /**
     * Adds a packet listener to the player's outgoing packet pipeline.
     *
     * @param player The player to add the listener to.
     * @param handlerName The name of the handler (must be unique).
     * @param listener The listener to add (returning false will cancel the packet being sent to the player).
     */
    void addOutgoingPacketHandler(Player player, String handlerName, Predicate<Object> listener);

    /**
     * Adds a packet listener to the player's incoming packet pipeline.
     *
     * @param player The player to add the listener to.
     * @param handlerName The name of the handler (must be unique).
     * @param listener The listener to add (returning false will cancel the packet being received by the server).
     */
    void addIncomingPacketHandler(Player player, String handlerName, Predicate<Object> listener);

    /**
     * Constructs a new scoreboard instance to be used in packets.
     * @return The new scoreboard instance.
     */
    Object newScoreboard();

    /**
     * Constructs a new objective instance to be used in packets.
     * @param scoreboard The scoreboard to add the objective to.
     * @param name The name of the objective.
     * @param displayName The display name of the objective.
     * @return The new objective instance.
     */
    Object newObjective(Object scoreboard, String name, String displayName);

    /**
     * Constructs a new team instance to be used in packets.
     * @param scoreboard The scoreboard to add the team to.
     * @param name The name of the team.
     * @return The new team instance.
     */
    Object newTeam(Object scoreboard, String name);

    /**
     * Returns the team name of a given team.
     * @param team The team.
     * @return The team name.
     */
    String getTeamName(Object team);

    /**
     * Returns the team options of a given team.
     * @param team The team.
     * @return The team options.
     */
    TeamOptions getTeamOptions(Object team);

    /**
     * Applies the given team options to the team.
     * @param team The team.
     * @param options The team options.
     */
    void setTeamOptions(Object team, TeamOptions options);

    /**
     * Constructs a new team action packet.
     * Can either create, remove or update a team.
     * @param team The team.
     * @param action The action.
     * @return The new team action packet.
     */
    Object getTeamActionPacket(Object team, ScoreboardAction action);

    /**
     * Constructs a packet to add/remove players to/from a team.
     * @param team The team.
     * @param players The names of the players to add/remove.
     * @param add Whether to add or remove the players.
     * @return The new team player action packet.
     */
    Object getTeamPlayerActionPacket(Object team, List<String> players, boolean add);

    /**
     * Constructs a new objective action packet.
     * Can either create, remove or update an objective.
     * @param objective The objective.
     * @param action The action.
     * @return The new objective action packet.
     */
    Object getObjectiveActionPacket(Object objective, ScoreboardAction action);

    /**
     * Constructs a new display objective packet.
     * @param objective The objective.
     * @param position The position to display the objective at.
     * @return The new display objective packet.
     */
    Object getDisplayObjectivePacket(Object objective, ScoreboardPosition position);

    /**
     * Constructs a new change score packet.
     * @param scoreboard The scoreboard.
     * @param objective The objective.
     * @param name The name to set the score for.
     * @param value The new value of the score.
     * @return The new change score packet.
     */
    Object getChangeScorePacket(Object scoreboard, Object objective, String name, int value);

    /**
     * Constructs a new remove score packet.
     * @param objective The objective.
     * @param name The name to remove the score for.
     * @return The new remove score packet.
     */
    Object getRemoveScorePacket(Object objective, String name);

    /**
     * Constructs a new spawn living entity packet.
     * @param entity The entity to spawn.
     * @return The new spawn living entity packet.
     */
    Object getSpawnLivingEntityPacket(LivingEntity entity);

    /**
     * Constructs a new entity metadata packet.
     * @param entity The entity to get the metadata from.
     * @return The new entity metadata packet.
     */
    Object getEntityMetadataPacket(LivingEntity entity);

    /**
     * Constructs a new destroy entities packet.
     * @param entities The entities to destroy.
     * @return The new destroy entities packet.
     */
    Object getDestroyEntitiesPacket(List<LivingEntity> entities);

    /**
     * Constructs a new {@link ArmorStand} instance that has the given location.
     * This method is necessary because you can't construct ArmorStand instances without spawning them.
     * @param loc The location.
     * @return The new armorstand.
     */
    ArmorStand createArmorStand(Location loc);

    /**
     * Constructs a new {@link Player} instance with information from the {@link NPC} instance.
     * @param npc The npc holding the information.
     * @return The new player.
     */
    Player createPlayer(NPC npc);

    /**
     * Sets the player's sneaking state.
     * This is necessary because in newer versions you need to call a nms method in order for the packet to be sent.
     * @param player The player.
     * @param sneaking Whether the player should be sneaking or not.
     */
    void setSneaking(Player player, boolean sneaking);

    /**
     * Constructs a new player info packet.
     * @param player The player.
     * @param add Whether to add or remove the player from the tablist.
     * @return The new player info packet.
     */
    Object getPlayerInfoPacket(Player player, boolean add);

    /**
     * Constructs a new named entity spawn packet.
     * @param player The player to spawn.
     * @return The new named entity spawn packet.
     */
    Object getNamedEntitySpawnPacket(Player player);

    /**
     * Constructs a new entity head rotation packet.
     * @param entity The entity.
     * @param yaw The yaw to set.
     * @return The new entity head rotation packet.
     */
    Object getEntityHeadRotationPacket(Entity entity, float yaw);

    /**
     * Constructs new update rotation packets.
     * Automatically included the entity head rotation packet from {@link #getEntityHeadRotationPacket(Entity, float)}
     * @param entity The entity.
     * @param yaw The yaw to set.
     * @param pitch The pitch to set.
     * @return The new update rotation packets.
     */
    List<Object> getUpdateRotationPackets(Entity entity, float yaw, float pitch);

    /**
     * Extracts the entity id from a use entity packet.
     * @param packet The packet.
     * @return The entity id.
     */
    Integer getIdFromUseEntityPacket(Object packet);

    /**
     * Extracts the action from a use entity packet.
     * @param packet The packet.
     * @return The action.
     */
    UseEntityAction getActionFromUseEntityPacket(Object packet);
}
