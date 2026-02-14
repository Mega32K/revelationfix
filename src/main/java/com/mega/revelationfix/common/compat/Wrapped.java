package com.mega.revelationfix.common.compat;

import com.Polarice3.Goety.client.audio.PostBossMusic;
import com.Polarice3.Goety.client.events.BossBarEvent;
import com.Polarice3.Goety.client.events.ClientEvents;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.endinglib.mixin.accessor.AccessorClientLevel;
import com.mega.endinglib.util.mixin.data_expand.ExtraPlayerRenderer;
import com.mega.revelationfix.client.citadel.PostEffectRegistry;
import com.mega.revelationfix.client.renderer.entity.WrappedPlayerRenderer;
import com.mega.revelationfix.common.entity.ShadowPlayerEntity;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.UUID;
import java.util.function.Predicate;

public class Wrapped {
    static Minecraft minecraft = Minecraft.getInstance();

    @OnlyIn(Dist.CLIENT)
    public static boolean isNetherApollyonLoaded(Predicate<Apostle> predicate) {
        return BossBarEvent.BOSS_BARS.values().stream().anyMatch(mob ->
                mob instanceof Apostle apostle
                        && apostle.isInNether()
                        && predicate.test(apostle)
                        && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()
        );
    }

    public static boolean isClientPlayerHalo() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return false;
        return ATAHelper.hasHalo(player);
    }

    public static boolean isClientPlayerOdamane() {
        Player player = Minecraft.getInstance().player;
        if (player == null) return false;
        return ATAHelper2.hasOdamane(player);
    }

    public static Player clientPlayer() {
        return minecraft.player;
    }

    public static Level clientLevel() {
        return minecraft.level;
    }

    public static void handleClientEntityEvent(byte b, Entity entity) {
        if (b == OdamanePlayerExpandedContext.ODAMANE_REVIVE_EVENT) {
            minecraft.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            minecraft.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);
            if (entity == minecraft.player) {
                minecraft.gameRenderer.displayItemActivation(new ItemStack(GRItems.HALO_OF_THE_END));
            }
        } else if (b == OdamanePlayerExpandedContext.REVIVE_EVENT) {
            minecraft.particleEngine.createTrackingEmitter(entity, ParticleTypes.TOTEM_OF_UNDYING, 30);
            minecraft.level.playLocalSound(entity.getX(), entity.getY(), entity.getZ(), SoundEvents.TOTEM_USE, entity.getSoundSource(), 1.0F, 1.0F, false);

        }
    }

    public static void play(Apostle apostle) {
        ClientEvents.BOSS_MUSIC = null;
        SoundManager soundHandler = Minecraft.getInstance().getSoundManager();
        soundHandler.queueTickingSound(new PostBossMusic(ModMain.APOLLYON_THEME_POST.get(), apostle, 1F, 1F));
    }

    public static Entity getEntityByUUID(UUID uuid) {
        if (uuid == null)
            return null;
        if (clientLevel() == null) return null;
        return ((AccessorClientLevel) clientLevel()).invokeGetEntities().get(uuid);
    }

    public static void onShaderModeChange() {
        PostEffectRegistry.resize(minecraft.getWindow().getWidth(), minecraft.getWindow().getHeight());

    }
    public static void setShadowPlayerRenderer(ShadowPlayerEntity entity, Player player) {
        if (player instanceof AbstractClientPlayer clientPlayer) {
            Minecraft mc = Minecraft.getInstance();
            EntityRenderer<? super Player> renderer = mc.getEntityRenderDispatcher().getRenderer(player);
            if ((Object)(renderer) instanceof PlayerRenderer playerRenderer) {
                entity.renderer = new WrappedPlayerRenderer(
                        new EntityRendererProvider.Context(mc.getEntityRenderDispatcher(),mc.getItemRenderer(),mc.getBlockRenderer(),mc.gameRenderer.itemInHandRenderer, mc.getResourceManager(), mc.getEntityModels(), mc.font),
                        ((ExtraPlayerRenderer) playerRenderer).endinglib$isSlim()
                );
            }
            entity.sWalkSpeed = player.walkAnimation.speed(mc.getPartialTick());
            entity.sWalkPosition = player.walkAnimation.position(mc.getPartialTick());
            entity.sAttackAnim = player.getAttackAnim(mc.getPartialTick());
        }
    }
}
