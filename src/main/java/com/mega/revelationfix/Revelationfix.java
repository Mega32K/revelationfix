package com.mega.revelationfix;

import com.Polarice3.Goety.common.entities.hostile.cultists.Heretic;
import com.Polarice3.Goety.common.entities.hostile.cultists.Maverick;
import com.google.common.collect.ImmutableList;
import com.mega.revelationfix.common.advancement.ModCriteriaTriggers;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.*;
import com.mega.revelationfix.common.entity.FakeSpellerEntity;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import com.mega.revelationfix.common.init.*;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.odamane.common.TheEndPuzzleItems;
import com.mega.revelationfix.proxy.ClientProxy;
import com.mega.revelationfix.proxy.CommonProxy;
import com.mega.revelationfix.proxy.ModProxy;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.util.time.TimeContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Revelationfix.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Revelationfix {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "revelationfix";
    private static final ModProxy PROXY = DistExecutor.unsafeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

    public Revelationfix() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModBlocks.BLOCKS.register(modBus);
        ModBlocks.BLOCK_ENTITIES.register(modBus);
        ModAttributes.ATTRIBUTES.register(modBus);
        ModSounds.SOUNDS.register(modBus);
        ModEntities.ENTITIES.register(modBus);
        ModEffects.EFFECTS.register(modBus);
        ModEnchantments.ENCHANTMENTS.register(modBus);
        ModStructurePlacementTypes.STRUCTURE_PLACEMENT_TYPES.register(modBus);
        ModStructureTypes.STRUCTURE_TYPES.register(modBus);
        ModStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(modBus);
        ModParticleTypes.PARTICLE_TYPES.register(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::enqueueIMC);
        modBus.addListener(this::setupEntityAttributeCreation);
        modBus.addListener(this::buildCreativeTab);
        if (SafeClass.isTetraLoaded()) {
            SafeClass.registerTetraEvents();
        }
        PacketHandler.registerPackets();
        List<TagKey<DamageType>> tagKeys = new ArrayList<>();
        tagKeys.add(DamageTypeTags.IS_FIRE);//火焰
        tagKeys.add(DamageTypeTags.IS_FALL); //摔落
        tagKeys.add(DamageTypeTags.IS_DROWNING); //溺水
        tagKeys.add(DamageTypeTags.IS_LIGHTNING); //闪电
        tagKeys.add(DamageTypeTags.IS_EXPLOSION); //爆炸
        tagKeys.add(DamageTypeTags.IS_FREEZING); //冰冻
        OdamanePlayerExpandedContext.INVULNERABLE_TO_TAGS = ImmutableList.copyOf(tagKeys);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModpackCommonConfig.SPEC, MODID + "/" + MODID + "-modpack-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, MODID + "/" + MODID + "-common.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ItemConfig.SPEC, MODID + "/" + MODID + "-item.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, MODID + "/" + MODID + "-client.toml");
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, BlockConfig.SPEC, MODID + "/" + MODID + "-block.toml");

        if (!SafeClass.isFantasyEndingLoaded()) {
            DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> {
                Timer timer = new Timer(true);
                TimerTask task = new TimerTask() {
                    public void run() {
                        if (!TimeStopUtils.isTimeStop) {
                            ++TimeContext.Both.timeStopModifyMillis;
                        }
                    }
                };
                timer.scheduleAtFixedRate(task, 1L, 1L);
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void addAttributes(EntityAttributeModificationEvent event) {
        ModAttributes.addAttributes(event);
    }

    public void buildCreativeTab(BuildCreativeModeTabContentsEvent event) {
        if (SafeClass.isTetraLoaded()) {
            if (event.getTabKey().location().equals(new ResourceLocation("tetra:default"))) {
                event.accept(SafeClass.setupSchematics("sword/longinus", "sword/longinus", new String[]{"sword/longinus"}, false, 0, 0xff0000, 1, 1, 0, 9));
                event.accept(SafeClass.setupSchematics("sword/aeglos", "sword/aeglos", new String[]{"sword/longinus", "sword/aeglos"}, true, 2, 0xc3c3c3, 2, 7, 5, 8));

                event.accept(SafeClass.setupTreatise("apocalyptium_ingot_expertise", false, 0, 16777215, 4, 6, 6, 5));
            }
        }
    }

    public void commonSetup(final FMLCommonSetupEvent event) {
        CriteriaTriggers.register(ModCriteriaTriggers.TE_CRAFT_TRIGGER);
        CriteriaTriggers.register(ModCriteriaTriggers.IMPROVE_HAMMER_TRIGGER);
        TheEndPuzzleItems.bake();
    }

    private void setupEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HERETIC_SERVANT.get(), Heretic.setCustomAttributes().build());
        event.put(ModEntities.MAVERICK_SERVANT.get(), Maverick.setCustomAttributes().build());
        event.put(ModEntities.APOSTLE_SERVANT.get(), ApostleServant.setCustomAttributes().build());
        event.put(ModEntities.FAKE_SPELLER.get(), FakeSpellerEntity.createAttributes().build());
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", "register_type", () -> {
            return SlotTypePreset.HEAD.getMessageBuilder().build();
        });
        InterModComms.sendTo("curios", "register_type", () -> {
            return SlotTypePreset.HANDS.getMessageBuilder().build();
        });
        InterModComms.sendTo("curios", "register_type", () -> {
            return SlotTypePreset.CURIO.getMessageBuilder().size(0).build();
        });
        InterModComms.sendTo("curios", "register_type", () -> {
            return SlotTypePreset.NECKLACE.getMessageBuilder().build();
        });
        InterModComms.sendTo("curios", "register_type", () -> {
            return SlotTypePreset.BACK.getMessageBuilder().build();
        });
    }
}
