package de.rapha149.displayutils.version;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.rapha149.displayutils.display.npc.NPC;
import de.rapha149.displayutils.display.npc.NPCSkin;
import de.rapha149.displayutils.display.npc.NPCSkin.NPCSkinPart;
import de.rapha149.displayutils.display.scoreboard.TeamOptionStatus;
import de.rapha149.displayutils.display.scoreboard.TeamOptions;
import de.rapha149.displayutils.display.scoreboard.TeamOptionsBuilder;
import de.rapha149.displayutils.version.ReflectionUtil.ReflectionException;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.MessageToMessageDecoder;
import net.minecraft.server.v1_16_R3.*;
import net.minecraft.server.v1_16_R3.IScoreboardCriteria.EnumScoreboardHealthDisplay;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntity.PacketPlayOutEntityLook;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumNameTagVisibility;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase.EnumTeamPush;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftLivingEntity;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Predicate;

public class Wrapper1_16_R3 implements VersionWrapper {

    @Override
    public void sendPackets(Player player, List<Object> packets) {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;
        packets.forEach(packet -> conn.sendPacket((Packet<?>) packet));
    }

    @Override
    public void addOutgoingPacketHandler(Player player, String handlerName, Predicate<Object> listener) {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;

        ChannelPipeline pipeline = conn.networkManager.channel.pipeline();
        if (pipeline.names().contains(handlerName))
            pipeline.remove(handlerName);

        pipeline.addAfter("packet_handler", handlerName, new ChannelDuplexHandler() {

            @Override
            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                try {
                    if (msg instanceof Packet<?>) {
                        if (!listener.test(msg))
                            return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                super.write(ctx, msg, promise);
            }
        });
    }

    @Override
    public void addIncomingPacketHandler(Player player, String handlerName, Predicate<Object> listener) {
        PlayerConnection conn = ((CraftPlayer) player).getHandle().playerConnection;

        ChannelPipeline pipeline = conn.networkManager.channel.pipeline();
        if (pipeline.names().contains(handlerName))
            pipeline.remove(handlerName);

        pipeline.addAfter("decoder", handlerName, new MessageToMessageDecoder<Packet<?>>() {

            @Override
            protected void decode(ChannelHandlerContext chc, Packet<?> packet, List<Object> out) {
                try {
                    if (!listener.test(packet))
                        return;
                } catch (Exception e) {
                    e.printStackTrace();
                }

                out.add(packet);
            }
        });
    }

    @Override
    public int getMaxTeamPrefixSuffixLength() {
        return -1;
    }

    @Override
    public int getMaxObjetiveDisplayNameLength() {
        return -1;
    }

    @Override
    public Object newScoreboard() {
        return new Scoreboard();
    }

    @Override
    public Object newObjective(Object scoreboard, String name, String displayName) {
        IChatBaseComponent displayNameComponent = new ChatComponentText(displayName != null ? displayName : name);
        ScoreboardObjective objective = new ScoreboardObjective((Scoreboard) scoreboard, name, IScoreboardCriteria.DUMMY,
                displayNameComponent, EnumScoreboardHealthDisplay.INTEGER);
        return objective;
    }

    @Override
    public Object newTeam(Object scoreboard, String name) {
        return new ScoreboardTeam((Scoreboard) scoreboard, name);
    }

    @Override
    public String getTeamName(Object team) {
        return ((ScoreboardTeam) team).getName();
    }

    @Override
    public TeamOptions getTeamOptions(Object team) {
        ScoreboardTeam t = (ScoreboardTeam) team;

        TeamOptionsBuilder builder = new TeamOptionsBuilder()
                .setPrefix(t.getPrefix().getString())
                .setSuffix(t.getSuffix().getString())
                .setColor(ChatColor.valueOf(t.getColor().name()))
                .setFriendlyFire(t.allowFriendlyFire())
                .setSeeFriendlyInvisibles(t.canSeeFriendlyInvisibles())
                .setNameTagVisibility(parseVisibilityOption(t.getNameTagVisibility()))
                .setDeathMessageVisibility(parseVisibilityOption(t.getDeathMessageVisibility()));

        // this may seem wrong, but the names of EnumTeamPush are not logical,
        // PUSH_OTHER_TEAMS means not pushing other teams, and PUSH_OWN_TEAM means not pushing the own team
        switch (t.getCollisionRule()) {
            case ALWAYS:
                builder.setCollisionRule(TeamOptionStatus.ALWAYS);
                break;
            case NEVER:
                builder.setCollisionRule(TeamOptionStatus.NEVER);
                break;
            case PUSH_OTHER_TEAMS:
                builder.setCollisionRule(TeamOptionStatus.OFF_FOR_OTHER_TEAMS);
                break;
            case PUSH_OWN_TEAM:
                builder.setCollisionRule(TeamOptionStatus.OFF_FOR_OWN_TEAM);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + t.getCollisionRule());
        }

        return builder.build();
    }

    @Override
    public void setTeamOptions(Object team, TeamOptions options) {
        ScoreboardTeam t = (ScoreboardTeam) team;
        String prefix = options.getPrefix();
        ChatColor color = options.getColor();
        t.setPrefix(new ChatComponentText(prefix + (prefix.length() <= 14 ? color : "")));
        t.setSuffix(new ChatComponentText(options.getSuffix()));
        t.setColor(EnumChatFormat.valueOf(color.name()));
        t.setAllowFriendlyFire(options.isFriendlyFire());
        t.setCanSeeFriendlyInvisibles(options.isSeeFriendlyInvisibles());
        t.setNameTagVisibility(formatVisibilityOption(options.getNameTagVisibility()));
        t.setDeathMessageVisibility(formatVisibilityOption(options.getDeathMessageVisibility()));

        // this may seem wrong, but the names of EnumTeamPush are not logical,
        // PUSH_OTHER_TEAMS means not pushing other teams, and PUSH_OWN_TEAM means not pushing the own team
        switch (options.getCollisionRule()) {
            case ALWAYS:
                t.setCollisionRule(EnumTeamPush.ALWAYS);
                break;
            case NEVER:
                t.setCollisionRule(EnumTeamPush.NEVER);
                break;
            case OFF_FOR_OTHER_TEAMS:
                t.setCollisionRule(EnumTeamPush.PUSH_OTHER_TEAMS);
                break;
            case OFF_FOR_OWN_TEAM:
                t.setCollisionRule(EnumTeamPush.PUSH_OWN_TEAM);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + options.getCollisionRule());
        }
    }

    private TeamOptionStatus parseVisibilityOption(EnumNameTagVisibility visibility) {
        switch (visibility) {
            case ALWAYS:
                return TeamOptionStatus.ALWAYS;
            case NEVER:
                return TeamOptionStatus.NEVER;
            case HIDE_FOR_OTHER_TEAMS:
                return TeamOptionStatus.OFF_FOR_OTHER_TEAMS;
            case HIDE_FOR_OWN_TEAM:
                return TeamOptionStatus.OFF_FOR_OWN_TEAM;
            default:
                throw new IllegalStateException("Unexpected value: " + visibility);
        }
    }

    private EnumNameTagVisibility formatVisibilityOption(TeamOptionStatus status) {
        switch (status) {
            case ALWAYS:
                return EnumNameTagVisibility.ALWAYS;
            case NEVER:
                return EnumNameTagVisibility.NEVER;
            case OFF_FOR_OTHER_TEAMS:
                return EnumNameTagVisibility.HIDE_FOR_OTHER_TEAMS;
            case OFF_FOR_OWN_TEAM:
                return EnumNameTagVisibility.HIDE_FOR_OWN_TEAM;
            default:
                throw new IllegalStateException("Unexpected value: " + status);
        }
    }

    @Override
    public Object getTeamActionPacket(Object team, ScoreboardAction action) {
        return new PacketPlayOutScoreboardTeam((ScoreboardTeam) team, action.getMode());
    }

    @Override
    public Object getTeamPlayerActionPacket(Object team, List<String> players, boolean add) {
        return new PacketPlayOutScoreboardTeam((ScoreboardTeam) team, players, add ? 3 : 4);
    }

    @Override
    public Object getObjectiveActionPacket(Object objective, ScoreboardAction action) {
        return new PacketPlayOutScoreboardObjective((ScoreboardObjective) objective, action.getMode());
    }

    @Override
    public Object getDisplayObjectivePacket(Object objective, ScoreboardPosition position) {
        return new PacketPlayOutScoreboardDisplayObjective(position.getPosition(), (ScoreboardObjective) objective);
    }

    @Override
    public Object getChangeScorePacket(Object scoreboard, Object objective, String name, int value) {
        return new PacketPlayOutScoreboardScore(ScoreboardServer.Action.CHANGE, ((ScoreboardObjective) objective).getName(), name, value);
    }

    @Override
    public Object getRemoveScorePacket(Object objective, String name) {
        return new PacketPlayOutScoreboardScore(ScoreboardServer.Action.REMOVE, ((ScoreboardObjective) objective).getName(), name, 0);
    }

    @Override
    public Object getSpawnLivingEntityPacket(LivingEntity entity) {
        return new PacketPlayOutSpawnEntityLiving(((CraftLivingEntity) entity).getHandle());
    }

    @Override
    public Object getEntityMetadataPacket(LivingEntity entity) {
        return new PacketPlayOutEntityMetadata(entity.getEntityId(), ((CraftLivingEntity) entity).getHandle().getDataWatcher(), true);
    }

    @Override
    public Object getDestroyEntitiesPacket(List<LivingEntity> entities) {
        return new PacketPlayOutEntityDestroy(entities.stream().map(LivingEntity::getEntityId).mapToInt(Integer::intValue).toArray());
    }

    @Override
    public ArmorStand createArmorStand(Location loc) {
        return (ArmorStand) new EntityArmorStand(((CraftWorld) loc.getWorld()).getHandle(), loc.getX(), loc.getY(), loc.getZ()).getBukkitEntity();
    }

    @Override
    public Player createPlayer(NPC npc) {
        NPCSkin skin = npc.getSkin();
        Location loc = npc.getLoc();

        GameProfile profile = new GameProfile(UUID.randomUUID(), npc.getName());
        profile.getProperties().put("properties", new Property(
                "textures",
                skin.getTexture(),
                skin.getSignature()
        ));

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer world = ((CraftWorld) loc.getWorld()).getHandle();
        EntityPlayer entityPlayer = new EntityPlayer(server, world, profile, new PlayerInteractManager(world));
        entityPlayer.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        entityPlayer.getDataWatcher().set(new DataWatcherObject<>(16, DataWatcherRegistry.a),
                (byte) skin.getEnabledParts().stream().mapToInt(NPCSkinPart::getBit).sum());
        return entityPlayer.getBukkitEntity();
    }

    @Override
    public void setSneaking(Player player, boolean sneaking) {
        player.setSneaking(sneaking);
        ((CraftPlayer) player).getHandle().setPose(sneaking ? EntityPose.CROUCHING : EntityPose.STANDING);
    }

    @Override
    public Object getPlayerInfoPacket(Player player, boolean add) {
        return new PacketPlayOutPlayerInfo(add ? PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER :
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ((CraftPlayer) player).getHandle());
    }

    @Override
    public Object getNamedEntitySpawnPacket(Player player) {
        return new PacketPlayOutNamedEntitySpawn(((CraftPlayer) player).getHandle());
    }

    @Override
    public Object getEntityHeadRotationPacket(Entity entity, float yaw) {
        return new PacketPlayOutEntityHeadRotation(((CraftEntity) entity).getHandle(), (byte) ((yaw % 360) * 256 / 360));
    }

    @Override
    public List<Object> getUpdateRotationPackets(Entity entity, float yaw, float pitch) {
        return Arrays.asList(
                new PacketPlayOutEntityLook(entity.getEntityId(), (byte) ((yaw % 360) * 256 / 360), (byte) ((pitch % 360) * 256 / 360), entity.isOnGround()),
                getEntityHeadRotationPacket(entity, yaw)
        );
    }

    @Override
    public Integer getIdFromUseEntityPacket(Object packet) {
        if (!(packet instanceof PacketPlayInUseEntity))
            throw new IllegalArgumentException("Packet must be of type PacketPlayInUseEntity");

        try {
            return ReflectionUtil.getField(packet, "a");
        } catch (ReflectionException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UseEntityAction getActionFromUseEntityPacket(Object packet) {
        if (!(packet instanceof PacketPlayInUseEntity))
            throw new IllegalArgumentException("Packet must be of type PacketPlayInUseEntity");

        return UseEntityAction.valueOf(((PacketPlayInUseEntity) packet).b().toString());
    }
}
