package com.mega.revelationfix.proxy;

import com.Polarice3.Goety.client.render.ApostleRenderer;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.PlayerRendererContext;
import com.mega.revelationfix.client.citadel.GRShaders;
import com.mega.revelationfix.client.citadel.PostEffectRegistry;
import com.mega.revelationfix.client.key.CuriosSkillKeyMapping;
import com.mega.revelationfix.client.particle.FrostFlowerParticle;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.client.screen.post.PostProcessingShaders;
import com.mega.revelationfix.client.screen.post.custom.AberrationDistortionPostEffect;
import com.mega.revelationfix.client.screen.post.custom.PuzzleEffect;
import com.mega.revelationfix.client.screen.post.custom.TimeStoppingGrayPostEffect;
import com.mega.revelationfix.common.block.blockentity.renderer.RuneReactorBERenderer;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.entity.renderer.*;
import com.mega.revelationfix.common.entity.renderer.mob.HereticServantRenderer;
import com.mega.revelationfix.common.entity.renderer.mob.MaverickServantRenderer;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.common.init.ModParticleTypes;
import com.mega.revelationfix.common.item.combat.BowOfRevelationItem;
import com.mega.revelationfix.common.item.other.MysteryFragment;
import com.mega.revelationfix.common.odamane.client.OdamaneHaloLayer;
import com.mega.revelationfix.util.ATAHelper2;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import com.mega.revelationfix.util.time.TimeContext;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import z1gned.goetyrevelation.util.ATAHelper;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ClientProxy implements ModProxy {
    public static final ResourceLocation HOLOGRAM_SHADER = new ResourceLocation(Revelationfix.MODID, "shaders/post/hologram.json");
    public static final ResourceLocation ODAMANE_SHADER = new ResourceLocation(Revelationfix.MODID, "shaders/post/odamane.json");
    private static ClientProxy INSTANCE;
    private PlayerRendererContext playerRendererContext;

    public ClientProxy() {
        try {
            PostEffectRegistry.registerEffect(HOLOGRAM_SHADER);
            PostEffectRegistry.registerEffect(ODAMANE_SHADER);
            IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
            modBus.addListener(this::registerShaders);
            modBus.addListener(this::clientSetup);
            modBus.addListener(this::registerKeys);
            modBus.addListener(this::onAddLayers);
            modBus.addListener(this::onParticleFactoryRegistration);
            ReloadableResourceManager manager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
            manager.registerReloadListener(PostProcessingShaders.INSTANCE);
            PostEffectHandler.registerEffect(new PuzzleEffect());
            PostEffectHandler.registerEffect(new AberrationDistortionPostEffect());
            PostEffectHandler.registerEffect(new TimeStoppingGrayPostEffect());
            //PostEffectHandler.registerEffect(new TheEndEffect());
            INSTANCE = this;
        } catch (Throwable throwable) {
        }
    }

    public static ClientProxy getInstance() {
        return INSTANCE;
    }

    public PlayerRendererContext getPlayerRendererContext() {
        return playerRendererContext;
    }

    private void registerShaders(final RegisterShadersEvent e) {
        try {
            e.registerShader(new ShaderInstance(e.getResourceProvider(), new ResourceLocation(Revelationfix.MODID, "rendertype_hologram"), DefaultVertexFormat.POSITION_COLOR), GRShaders::setRenderTypeHologramShader);
            e.registerShader(new ShaderInstance(e.getResourceProvider(), new ResourceLocation(Revelationfix.MODID, "rendertype_light_beacon_beam"), DefaultVertexFormat.BLOCK), GRShaders::setLightBeaconBeam);

            RevelationFixMixinPlugin.LOGGER.info("registered internal shaders");
        } catch (IOException exception) {
            RevelationFixMixinPlugin.LOGGER.error("could not register internal shaders");
            exception.printStackTrace();
        }
    }

    public void onAddLayers(EntityRenderersEvent.AddLayers event) {
        playerRendererContext = new PlayerRendererContext();
        playerRendererContext.init(event.getContext());
    }

    public void clientSetup(final FMLClientSetupEvent event) {
        if (SafeClass.isTetraLoaded())
            SafeClass.registerTetraItemEffects();

        event.enqueueWork(() -> {
            EntityRenderers.register(ModEntities.FAKE_ITEM_ENTITY.get(), FakeItemEntityRenderer::new);
            EntityRenderers.register(ModEntities.REVELATION_CAGE_ENTITY.get(), RevelationCageEntityRenderer::new);
            EntityRenderers.register(ModEntities.HERETIC_SERVANT.get(), HereticServantRenderer::new);
            EntityRenderers.register(ModEntities.MAVERICK_SERVANT.get(), MaverickServantRenderer::new);
            EntityRenderers.register(ModEntities.QUIETUS_VIRTUAL_ENTITY.get(), QuietusVirtualRenderer::new);
            EntityRenderers.register(ModEntities.APOSTLE_SERVANT.get(), ApostleRenderer::new);
            EntityRenderers.register(ModEntities.BLOCK_SHAKING_ENTITY.get(), BlockShakingEntityRenderer::new);
            EntityRenderers.register(ModEntities.THE_END_HELLFIRE.get(), TheEndHellfireRenderer::new);
            EntityRenderers.register(ModEntities.STAR_ENTITY.get(), StarArrowRenderer::new);
            EntityRenderers.register(ModEntities.GUNGNIR.get(), GungnirSpearRenderer::new);
            EntityRenderers.register(ModEntities.FAKE_SPELLER.get(), FakeSpellerRenderer::new);
            CuriosRendererRegistry.register(GRItems.HALO_OF_THE_END, OdamaneHaloLayer::new);
            BlockEntityRenderers.register(ModBlocks.RUNE_REACTOR_ENTITY.get(), RuneReactorBERenderer::new);
            BowOfRevelationItem bow = (BowOfRevelationItem) GRItems.BOW_OF_REVELATION.get();
            ItemProperties.register(bow, new ResourceLocation("pulling"), (itemStack, clientWorld, livingEntity, i)
                    -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
            ItemProperties.register(bow, new ResourceLocation("pull"), (itemStack, clientWorld, livingEntity, i) -> {
                if (livingEntity == null) {
                    return 0.0F;
                } else {
                    float time = livingEntity.getUseItem() != itemStack ? 0.0F : (float) (itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
                    if (time > 2.0F && !(ATAHelper.hasHalo(livingEntity) || ATAHelper2.hasOdamane(livingEntity)))
                        return 2.0F;
                    return time;
                }
            });
            MysteryFragment mysteryFragment = (MysteryFragment) GRItems.MYSTERY_FRAGMENT.get();
            ItemProperties.register(mysteryFragment, new ResourceLocation("fragment"), (itemStack, clientWorld, livingEntity, i)
                    -> livingEntity != null ? itemStack.getOrCreateTag().getInt("fragment") : 0);
        });
        if (!SafeClass.isFantasyEndingLoaded())
            event.enqueueWork(() -> {
                Minecraft mc = Minecraft.getInstance();
                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                    if (TimeContext.Client.timeStopGLFW == 0L)
                        TimeContext.Client.timeStopGLFW = (long) (GLFW.glfwGetTime() * 1000L);

                    if (!SafeClass.isClientTimeStop()) {
                        ++TimeContext.Both.timeStopModifyMillis;
                        if (!mc.isPaused()) TimeContext.Client.timeStopGLFW++;
                    }
                    TimeContext.Client.count++;
                }, 0L, 1L, TimeUnit.MILLISECONDS);
            });
    }
    public void registerKeys(RegisterKeyMappingsEvent evt) {
        evt.register(CuriosSkillKeyMapping.ACTIVE_SKILL);
    }
    public void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.FROST_FLOWER.get(), FrostFlowerParticle.Provider::new);
    }
}
