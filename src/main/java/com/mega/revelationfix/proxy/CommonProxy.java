package com.mega.revelationfix.proxy;

import com.Polarice3.Goety.common.entities.hostile.cultists.Heretic;
import com.Polarice3.Goety.common.entities.hostile.cultists.Maverick;
import com.mega.endinglib.api.capability.EntitySyncCapabilityBase;
import com.mega.endinglib.common.capability.ELCapabilityManager;
import com.mega.revelationfix.common.advancement.ModCriteriaTriggers;
import com.mega.revelationfix.common.capability.entity.GoetyRevelationPlayerCapability;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import com.mega.revelationfix.common.entity.boss.IceHermitEntity;
import com.mega.revelationfix.common.init.ModEntities;
import com.mega.revelationfix.common.init.ModPotions;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.data.RitualDataSyncPacket;
import com.mega.revelationfix.common.odamane.common.TheEndPuzzleItems;
import com.mega.revelationfix.util.entity.CapabilityGetter;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Pillager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import top.theillusivec4.curios.api.SlotTypePreset;

import java.util.List;

public class CommonProxy implements ModProxy {
    public static LazyOptional<Capability<GoetyRevelationPlayerCapability>> PLAYER_CAPABILITY = LazyOptional.of(()-> ELCapabilityManager.getCapability(GoetyRevelationPlayerCapability.NAME.toString()));
    public CommonProxy(FMLJavaModLoadingContext context) {
        IEventBus modBus = context.getModEventBus();
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::enqueueIMC);
        modBus.addListener(this::setupEntityAttributeCreation);
        modBus.addListener(this::buildCreativeTab);
        MinecraftForge.EVENT_BUS.register(this);
    }
    public static Capability<GoetyRevelationPlayerCapability> getPlayerCap() {
        return PLAYER_CAPABILITY.orElse(ELCapabilityManager.getCapability(GoetyRevelationPlayerCapability.NAME.toString()));
    }
    public static GoetyRevelationPlayerCapability getPlayerCapInstance(Player player) {
        return CapabilityGetter.unsafeGetCapability(getPlayerCap(), player);
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
        ModPotions.registerMix();
        TheEndPuzzleItems.bake();
        {
            ELCapabilityManager.regsterCapability(GoetyRevelationPlayerCapability.INSTANCE_SUPPLIER.get());
        }
    }

    private void setupEntityAttributeCreation(EntityAttributeCreationEvent event) {
        event.put(ModEntities.HERETIC_SERVANT.get(), Heretic.setCustomAttributes().build());
        event.put(ModEntities.MAVERICK_SERVANT.get(), Maverick.setCustomAttributes().build());
        event.put(ModEntities.APOSTLE_SERVANT.get(), ApostleServant.setCustomAttributes().build());
        event.put(ModEntities.FAKE_SPELLER.get(), FakeSpellerEntity.createAttributes().build());
        event.put(ModEntities.ICE_HERMIT.get(), IceHermitEntity.createAttributes().build());
    }

    private void enqueueIMC(InterModEnqueueEvent event) {
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.HEAD.getMessageBuilder().build());
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.HANDS.getMessageBuilder().build());
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.CURIO.getMessageBuilder().size(0).build());
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.BACK.getMessageBuilder().build());
        InterModComms.sendTo("curios", "register_type", () -> SlotTypePreset.CHARM.getMessageBuilder().size(2).build());
    }

    @SubscribeEvent
    public void onDataSync(OnDatapackSyncEvent event) {
        List<ServerPlayer> players = event.getPlayers();
        if (!players.isEmpty()) {
            for (ServerPlayer sp : players)
                PacketHandler.sendToPlayer(sp, new RitualDataSyncPacket(RitualDataManager.getRegistries()));
        }
    }
}
