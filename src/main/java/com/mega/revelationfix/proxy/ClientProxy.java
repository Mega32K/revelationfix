package com.mega.revelationfix.proxy;

import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.Polarice3.Goety.client.render.ApostleRenderer;
import com.mega.endinglib.api.client.levelevent.LevelEventManager;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.CuriosRenderer;
import com.mega.revelationfix.client.PlayerRendererContext;
import com.mega.revelationfix.client.citadel.GRShaders;
import com.mega.revelationfix.client.citadel.PostEffectRegistry;
import com.mega.revelationfix.client.key.CuriosSkillKeyMapping;
import com.mega.revelationfix.client.model.curio.ApollyonRobeModel;
import com.mega.revelationfix.client.model.entity.SpectreDarkmageHatModel;
import com.mega.revelationfix.client.model.entity.SpiderArmorModel;
import com.mega.revelationfix.client.model.entity.SpiderDarkmageArmorModel;
import com.mega.revelationfix.client.particle.FrostFlowerParticle;
import com.mega.revelationfix.client.renderer.entity.*;
import com.mega.revelationfix.client.screen.post.PostProcessingShaders;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.block.blockentity.renderer.RuneReactorBERenderer;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.client.model.entity.TeleportEntityModel;
import com.mega.revelationfix.common.compat.mui.ModernUIWrapped;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModBlocks;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.common.init.ModParticleTypes;
import com.mega.revelationfix.common.item.tool.combat.bow.BowOfRevelationItem;
import com.mega.revelationfix.common.item.other.MysteryFragment;
import com.mega.revelationfix.common.network.PacketClientProxy;
import com.mega.revelationfix.common.odamane.client.OdamaneHaloLayer;
import com.mega.revelationfix.common.odamane.client.OdamaneHaloModel;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;
import z1gned.goetyrevelation.util.ATAHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ClientProxy implements ModProxy {
    public static final ResourceLocation HOLOGRAM_SHADER = new ResourceLocation(Revelationfix.MODID, "shaders/post/hologram.json");
    public static final ResourceLocation ODAMANE_SHADER = new ResourceLocation(Revelationfix.MODID, "shaders/post/odamane.json");
    private static ClientProxy INSTANCE;
    private PlayerRendererContext playerRendererContext;

    public ClientProxy(FMLJavaModLoadingContext context) {
        try {
            PostEffectRegistry.registerEffect(HOLOGRAM_SHADER);
            PostEffectRegistry.registerEffect(ODAMANE_SHADER);
            IEventBus modBus = context.getModEventBus();
            modBus.addListener(this::registerShaders);
            modBus.addListener(this::clientSetup);
            modBus.addListener(this::registerKeys);
            modBus.addListener(this::onAddLayers);
            modBus.addListener(this::onRegisterLayers);
            modBus.addListener(this::onParticleFactoryRegistration);
            ReloadableResourceManager manager = (ReloadableResourceManager) Minecraft.getInstance().getResourceManager();
            manager.registerReloadListener(PostProcessingShaders.INSTANCE);
            if (SafeClass.isModernUILoaded()) {
                ModernUIWrapped.registerCalls();
            }
            //PostEffectHandler.registerEffect(new TheEndEffect());
            INSTANCE = this;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
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

    public void onRegisterLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(QuietusVirtualRenderer.MODEL_LAYER_LOCATION, QuietusVirtualRenderer::createBodyLayer);
        event.registerLayerDefinition(OdamaneHaloModel.LAYER_LOCATION, OdamaneHaloModel::createBodyLayer);
        event.registerLayerDefinition(TeleportEntityModel.LAYER_LOCATION, TeleportEntityModel::createBodyLayer);
        event.registerLayerDefinition(SpiderArmorModel.OUTER, SpiderArmorModel::creteOuter);
        event.registerLayerDefinition(SpiderDarkmageArmorModel.OUTER, SpiderDarkmageArmorModel::creteOuter);
        event.registerLayerDefinition(SpectreDarkmageHatModel.LAYER_LOCATION, SpectreDarkmageHatModel::createHatLayer);
        event.registerLayerDefinition(ApollyonRobeModel.LAYER_LOCATION, ApollyonRobeModel::createBodyLayer);
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
            EntityRenderers.register(ModEntities.TELEPORT_ENTITY.get(), TeleportEntityRenderer::new);
            //if (!SafeClass.isYSMLoaded())
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
            copyOldArtIfMissing();
            copyGrArtIfMissing();
            registerLevelEvents();
            CuriosRenderer.register();
        });
    }
    public void registerKeys(RegisterKeyMappingsEvent evt) {
        evt.register(CuriosSkillKeyMapping.ACTIVE_SKILL);
    }
    public void onParticleFactoryRegistration(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(ModParticleTypes.FROST_FLOWER.get(), FrostFlowerParticle.Provider::new);
    }
    private static void copyOldArtIfMissing() {
        File dir = new File(".", "resourcepacks");
        File target = new File(dir, "GR Old Textures.zip");
        if (!target.exists()) {
            try {
                dir.mkdirs();
                InputStream in = Revelationfix.class.getResourceAsStream("/assets/revelationfix/gr_old_textures.zip");
                FileOutputStream out = new FileOutputStream(target);
                byte[] buf = new byte[16384];
                if (in != null) {
                    int len;
                    while((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                }

                out.close();
            } catch (IOException var6) {
            }
        }

    }
    private static void copyGrArtIfMissing() {
        File dir = new File(".", "resourcepacks");
        File target = new File(dir, "GoetyRevelation art style.zip");
        if (!target.exists()) {
            try {
                dir.mkdirs();
                InputStream in = Revelationfix.class.getResourceAsStream("/assets/revelationfix/gr_art_style_textures.zip");
                FileOutputStream out = new FileOutputStream(target);
                byte[] buf = new byte[16384];
                if (in != null) {
                    int len;
                    while((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }

                    in.close();
                }

                out.close();
            } catch (IOException var6) {
            }
        }

    }
    public static void registerLevelEvents() {
        LevelEventManager.registerLevelEvent(232424314, (pos, rand, i) -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;
            switch ((byte) i) {
                case 0 -> {
                    Minecraft.getInstance().level.playLocalSound(pos, SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1.0F, 0.5F + rand.nextFloat() * 1.2F, false);
                    level.playLocalSound(pos, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F + rand.nextFloat() * 1.2F, false);
                }
                case 1 -> level.playLocalSound(pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0F, 0.5F + rand.nextFloat(), false);
                case 2 -> PacketClientProxy.doFrostParticles(pos.getX() + 0.5D, pos.getY() + 0.4D, pos.getZ() + 0.5D);
                case 3 -> level.addParticle(new CircleExplodeParticleOption(.70F, .45F, .95F, 3F, 1), pos.getX() + RuneReactorBlock.BASE_SHAPE.bounds().getXsize() / 2F, pos.getY() + RuneReactorBlock.BASE_SHAPE.bounds().getYsize() / 2F, pos.getZ() + RuneReactorBlock.BASE_SHAPE.bounds().getZsize() / 2F, 0, 0, 0);
                case 4 -> {
                    double x = pos.getX() + RuneReactorBlock.BASE_SHAPE.bounds().getXsize() / 2F;
                    double z = pos.getZ() + RuneReactorBlock.BASE_SHAPE.bounds().getZsize() / 2F;
                    PacketClientProxy.doFrostParticles(x, pos.getY() + RuneReactorBlock.BASE_SHAPE.bounds().getYsize() / 2F, z);
                    for (int j = 0; j < 16; j++) {
                        level.addParticle(com.Polarice3.Goety.client.particles.ModParticleTypes.FROST_NOVA.get(), x, pos.getY() + j / 2F + RuneReactorBlock.BASE_SHAPE.bounds().getYsize() / 2F, z, 0F, 0F, 0F);
                    }
                }
                case 5 -> level.addParticle(new CircleExplodeParticleOption(.47F, .41F, .32F, 2F, 2), pos.getX() + .5F, pos.getY() + 0.2F, pos.getZ() + .5F, 0, 0.02, 0);
                case 6 -> {
                    pos = pos.above();
                    BlockState state = Blocks.GLASS.defaultBlockState();
                    SoundType soundtype = state.getSoundType(level, pos, null);
                    level.playLocalSound(pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.7F + rand.nextFloat() * 0.2F, false);
                    level.addDestroyBlockEffect(pos, state);
                }
                case 7 -> level.playLocalSound(pos, SoundEvents.WARDEN_ATTACK_IMPACT, SoundSource.PLAYERS, 1.0F, 1.0F, false);
            }
        });
        LevelEventManager.registerLevelEvent(499366777, (pos, rand, i) -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;
            switch ((byte) i) {
                case 0 -> {
                    for(int j = 0; j < 40; ++j) {
                        double d0 = rand.nextGaussian() * 0.2;
                        double d1 = rand.nextGaussian() * 0.2;
                        double d2 = rand.nextGaussian() * 0.2;
                        level.addAlwaysVisibleParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, d0, d1, d2);
                    }
                }
            }
        });
    }
}
