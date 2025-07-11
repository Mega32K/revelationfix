package com.mega.revelationfix.common.init;

import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.entity.*;
import com.mega.revelationfix.common.entity.binding.BlockShakingEntity;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.entity.binding.RevelationCageEntity;
import com.mega.revelationfix.common.entity.binding.TeleportEntity;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import com.mega.revelationfix.common.entity.cultists.HereticServant;
import com.mega.revelationfix.common.entity.cultists.MaverickServant;
import com.mega.revelationfix.common.entity.misc.QuietusVirtualEntity;
import com.mega.revelationfix.common.entity.projectile.GungnirSpearEntity;
import com.mega.revelationfix.common.entity.projectile.StarArrow;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import z1gned.goetyrevelation.ModMain;

public class ModEntities {
    public static final TagKey<EntityType<?>> PREVENT_DISCARD_BY_APOLLYON = TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation(ModMain.MODID, "prevent_apollyon_discard"));
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Revelationfix.MODID);
    public static final RegistryObject<EntityType<FakeItemEntity>> FAKE_ITEM_ENTITY = register("fake_item_entity", EntityType.Builder
            .<FakeItemEntity>of(FakeItemEntity::new, MobCategory.MISC)
            .sized(0.25F, 0.25F)
            .clientTrackingRange(16)
            .updateInterval(20));
    public static final RegistryObject<EntityType<QuietusVirtualEntity>> QUIETUS_VIRTUAL_ENTITY = register("quietus_virtual_entity", EntityType.Builder
            .<QuietusVirtualEntity>of(QuietusVirtualEntity::new, MobCategory.MISC).
            sized(1.0F, 1.0F).
            clientTrackingRange(64));
    public static final RegistryObject<EntityType<RevelationCageEntity>> REVELATION_CAGE_ENTITY = register("revelation_cage", EntityType.Builder.
                    <RevelationCageEntity>of(RevelationCageEntity::new, MobCategory.MISC)
            .fireImmune()
            .setShouldReceiveVelocityUpdates(false)
            .sized(.25F, .25F)
            .clientTrackingRange(6)
            .updateInterval(2)
    );
    public static final RegistryObject<EntityType<TheEndHellfire>> THE_END_HELLFIRE = register("the_end_hellfire", EntityType.Builder.
                    <TheEndHellfire>of(TheEndHellfire::new, MobCategory.MISC)
            .fireImmune()
            .sized(0.8F, 1.0F)
            .clientTrackingRange(10)
    );
    public static final RegistryObject<EntityType<StarArrow>> STAR_ENTITY = register("star_arrow", EntityType.Builder.
                    <StarArrow>of(StarArrow::new, MobCategory.MISC)
            .sized(0.1F, 0.1F)
            .clientTrackingRange(40)
    );
    public static final RegistryObject<EntityType<BlockShakingEntity>> BLOCK_SHAKING_ENTITY = register("block_shaking_entity", EntityType.Builder.
                    <BlockShakingEntity>of(BlockShakingEntity::new, MobCategory.MISC)
            .fireImmune()
            .setShouldReceiveVelocityUpdates(false)
            .sized(.001F, .001F)
            .clientTrackingRange(6)
            .updateInterval(2)
    );
    public static final RegistryObject<EntityType<HereticServant>> HERETIC_SERVANT = register("heretic_servant",
            EntityType.Builder.of(HereticServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8));
    public static final RegistryObject<EntityType<MaverickServant>> MAVERICK_SERVANT = register("maverick_servant",
            EntityType.Builder.of(MaverickServant::new, MobCategory.MONSTER)
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8));
    public static final RegistryObject<EntityType<ApostleServant>> APOSTLE_SERVANT = register("apostle_servant",
            EntityType.Builder.of(ApostleServant::new, MobCategory.MONSTER)
                    .canSpawnFarFromPlayer()
                    .fireImmune()
                    .sized(0.6F, 1.95F)
                    .clientTrackingRange(8));
    public static final RegistryObject<EntityType<GungnirSpearEntity>> GUNGNIR = register("gungnir_spear",
            EntityType.Builder.<GungnirSpearEntity>of(GungnirSpearEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20));
    public static final RegistryObject<EntityType<FakeSpellerEntity>> FAKE_SPELLER = register("fake_speller",
            EntityType.Builder.<FakeSpellerEntity>of(FakeSpellerEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.2F, 0.2F)
                    .clientTrackingRange(4)
                    .updateInterval(20));
    public static final RegistryObject<EntityType<TeleportEntity>> TELEPORT_ENTITY = register("teleport_entity", EntityType.Builder.
                    <TeleportEntity>of(TeleportEntity::new, MobCategory.MISC)
            .fireImmune()
            .setShouldReceiveVelocityUpdates(false)
            .sized(.001F, .001F)
            .clientTrackingRange(6)
            .updateInterval(2)
    );
    private static <T extends Entity> RegistryObject<EntityType<T>> register(String p_20635_, EntityType.Builder<T> p_20636_) {
        return ModEntities.ENTITIES.register(p_20635_, () -> p_20636_.build(new ResourceLocation(Revelationfix.MODID, p_20635_).toString()));
    }
}
