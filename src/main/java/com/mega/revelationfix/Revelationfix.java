package com.mega.revelationfix;

import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.*;
import com.mega.revelationfix.common.data.ingrident.EnchantmentBookIngredientSerializer;
import com.mega.revelationfix.common.data.ingrident.MysteryFragmentIngredientSerializer;
import com.mega.revelationfix.common.data.ingrident.PuzzleIngredientSerializer;
import com.mega.revelationfix.common.data.ingrident.TheEndCraftingIngredientSerializer;
import com.mega.revelationfix.common.data.loot.modifier.ModLootModifiersInit;
import com.mega.revelationfix.common.init.*;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.ritual.ModRitualTypes;
import com.mega.revelationfix.proxy.ClientProxy;
import com.mega.revelationfix.proxy.CommonProxy;
import com.mega.revelationfix.proxy.ModProxy;
import com.mega.revelationfix.proxy.ServerProxy;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import z1gned.goetyrevelation.ModMain;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Revelationfix.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Revelationfix {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "revelationfix";
    private final ModProxy PROXY;

    public Revelationfix(FMLJavaModLoadingContext context) {
        PROXY = DistExecutor.unsafeRunForDist(() -> () -> new ClientProxy(context), () -> () -> new ServerProxy(context));
        IEventBus modBus = context.getModEventBus();
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
        ModLootModifiersInit.GLOBAL_LOOT_MODIFIER.register(modBus);
        modBus.addListener(this::registerRecipeSerializers);
        ModPotions.register(modBus);
        ModRitualTypes.initModRituals();
        new CommonProxy(context);
        if (SafeClass.isTetraLoaded()) {
            SafeClass.registerTetraEvents();
        }
        PacketHandler.registerPackets();
        OdamanePlayerExpandedContext.setInvulnerableTo();
        context.registerConfig(ModConfig.Type.COMMON, GoetyModificationCommonConfig.SPEC, MODID + "/" + "goety-modify-common.toml");
        context.registerConfig(ModConfig.Type.COMMON, GoetyModificationClientConfig.SPEC, MODID + "/" + "goety-modify-client.toml");
        context.registerConfig(ModConfig.Type.COMMON, ModpackCommonConfig.SPEC, MODID + "/" + MODID + "-modpack-common.toml");
        context.registerConfig(ModConfig.Type.COMMON, CommonConfig.SPEC, MODID + "/" + MODID + "-common.toml");
        context.registerConfig(ModConfig.Type.COMMON, ItemConfig.SPEC, MODID + "/" + MODID + "-item.toml");
        context.registerConfig(ModConfig.Type.CLIENT, ClientConfig.SPEC, MODID + "/" + MODID + "-client.toml");
        context.registerConfig(ModConfig.Type.COMMON, BlockConfig.SPEC, MODID + "/" + MODID + "-block.toml");
        context.registerConfig(ModConfig.Type.SERVER, ServerConfig.SPEC, MODID + "/" + MODID + "-server.toml");

    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void addAttributes(EntityAttributeModificationEvent event) {
        ModAttributes.addAttributes(event);
    }

    public void registerRecipeSerializers(RegisterEvent event) {
        if (event.getRegistryKey().equals(ForgeRegistries.Keys.RECIPE_SERIALIZERS)) {
            CraftingHelper.register(new ResourceLocation(ModMain.MODID, "puzzle"), PuzzleIngredientSerializer.INSTANCE);
            CraftingHelper.register(new ResourceLocation(ModMain.MODID, "mystery_fragment"), MysteryFragmentIngredientSerializer.INSTANCE);
            CraftingHelper.register(new ResourceLocation(ModMain.MODID, "te_craft"), TheEndCraftingIngredientSerializer.INSTANCE);
            CraftingHelper.register(new ResourceLocation(ModMain.MODID, "enchantment"), EnchantmentBookIngredientSerializer.INSTANCE);
        }
    }

}
