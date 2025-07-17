package com.mega.revelationfix.common.network;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.network.c2s.TeleportEntityTryEvent;
import com.mega.revelationfix.common.network.c2s.TheEndDeathPacket;
import com.mega.revelationfix.common.network.c2s.TryTimeStopSkill;
import com.mega.revelationfix.common.network.s2c.*;
import net.minecraft.core.Holder;
import net.minecraft.network.protocol.game.ClientboundSoundEntityPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketHandler {

    private static final String PROTOCOL_VERSION = "1";
    public static SimpleChannel INSTANCE;
    private static int id = 0;

    public static void registerPackets() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(Revelationfix.MODID, "goety_revelation_packet"), () -> PROTOCOL_VERSION, s -> true, s -> true);
        INSTANCE.registerMessage(id(), TheEndPuzzleUpdatePacket.class, TheEndPuzzleUpdatePacket::encode, TheEndPuzzleUpdatePacket::decode, TheEndPuzzleUpdatePacket::handle);
        INSTANCE.registerMessage(id(), TheEndRitualEventPacket.class, TheEndRitualEventPacket::encode, TheEndRitualEventPacket::decode, TheEndRitualEventPacket::handle);
        INSTANCE.registerMessage(id(), TheEndStructureMessagePacket.class, TheEndStructureMessagePacket::encode, TheEndStructureMessagePacket::decode, TheEndStructureMessagePacket::handle);
        INSTANCE.registerMessage(id(), TryTimeStopSkill.class, TryTimeStopSkill::encode, TryTimeStopSkill::decode, TryTimeStopSkill::handle);
        INSTANCE.registerMessage(id(), PlayApollyonPostThemePacket.class, PlayApollyonPostThemePacket::encode, PlayApollyonPostThemePacket::decode, PlayApollyonPostThemePacket::handle);
        INSTANCE.registerMessage(id(), TheEndDeathPacket.class, TheEndDeathPacket::encode, TheEndDeathPacket::decode, TheEndDeathPacket::handle);
        INSTANCE.registerMessage(id(), LifeStealParticlesS2CPacket.class, LifeStealParticlesS2CPacket::encode, LifeStealParticlesS2CPacket::decode, LifeStealParticlesS2CPacket::handle);
        INSTANCE.registerMessage(id(), SpellCircleStatePacket.class, SpellCircleStatePacket::encode, SpellCircleStatePacket::decode, SpellCircleStatePacket::handle);
        INSTANCE.registerMessage(id(), IceSpellParticlePacket.class, IceSpellParticlePacket::encode, IceSpellParticlePacket::decode, IceSpellParticlePacket::handle);
        INSTANCE.registerMessage(id(), TeleportEntityTryEvent.class, TeleportEntityTryEvent::encode, TeleportEntityTryEvent::decode, TeleportEntityTryEvent::handle);
    }

    public static int id() {
        return id++;
    }

    public static <MSG> void sendToAll(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }

    public static <MSG> void sendToServer(MSG msg) {
        INSTANCE.sendToServer(msg);
    }

    public static <MSG> void sendToPlayer(ServerPlayer player, MSG msg) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MGS> void sendToEntity(MGS message, LivingEntity entity) {
        INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }

    public static void playSound(ServerPlayer serverPlayer, SoundEvent soundEvent, SoundSource source, float volume, float s) {
        ServerLevel serverLevel = serverPlayer.serverLevel();
        for (ServerPlayer player : serverLevel.players()) {
            if (player.level().dimension() == serverPlayer.level().dimension()) {
                player.connection.send(new ClientboundSoundEntityPacket(Holder.direct(soundEvent), source, serverPlayer, volume, s, player.getRandom().nextLong()));
            }
        }
    }

    public static void playSound(ServerPlayer serverPlayer, SoundEvent soundEvent, float volume, float s) {
        playSound(serverPlayer, soundEvent, SoundSource.PLAYERS, volume, s);
    }

    public static void playSound(ServerLevel level, Entity source, SoundEvent soundEvent, SoundSource soundSource, float volume, float s) {
        for (ServerPlayer player : level.players()) {
            if (player.level().dimension() == source.level().dimension()) {
                player.connection.send(new ClientboundSoundEntityPacket(Holder.direct(soundEvent), soundSource, source, volume, s, player.getRandom().nextLong()));
            }
        }
    }

    public static void playSound(ServerLevel level, Entity source, SoundEvent soundEvent, float volume, float s) {
        for (ServerPlayer player : level.players()) {
            if (player.level().dimension() == source.level().dimension()) {
                player.connection.send(new ClientboundSoundEntityPacket(Holder.direct(soundEvent), SoundSource.VOICE, source, volume, s, player.getRandom().nextLong()));
            }
        }
    }
}
