package com.mega.revelationfix.common.event.handler;

import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.common.data.TimeStopSavedData;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import com.mega.revelationfix.common.entity.projectile.GungnirSpearEntity;
import com.mega.revelationfix.common.event.EarlyLivingDeathEvent;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.common.item.combat.ICustomHurtWeapon;
import com.mega.revelationfix.common.item.curios.TheNeedleItem;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.c2s.TheEndDeathPacket;
import com.mega.revelationfix.common.network.s2c.TheEndPuzzleUpdatePacket;
import com.mega.revelationfix.common.network.s2c.timestop.TimeStopSkillPacket;
import com.mega.revelationfix.common.odamane.common.TheEndPuzzleItems;
import com.mega.revelationfix.safe.*;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.safe.entity.LivingEventEC;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import com.mega.revelationfix.util.ATAHelper2;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import com.mega.revelationfix.util.time.TimeStopEntityData;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import org.joml.Vector3f;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber
public class CommonEventHandler {
    private static final List<Item> vanillaItems = new ArrayList<>();
    /*
    @SubscribeEvent
    public static void sfdw(AttackEntityEvent event) {
        try {
            Unsafe unsafe = UnsafeAccess.UNSAFE;
            TransformingClassLoader classLoader = (TransformingClassLoader) unsafe.getObject(Launcher.INSTANCE, unsafe.objectFieldOffset(Launcher.class.getDeclaredField("classLoader")));
            Method method = TransformingClassLoader.class.getDeclaredMethod("buildTransformedClassNodeFor", String.class, String.class);
            method.setAccessible(true);
            byte[] bytes = (byte[]) method.invoke(classLoader, EventBus.class.getName(), "classloading");
            File file = new File("C:\\Users\\admin\\Desktop\\EventBus.class");
            FileOutputStream stream = new FileOutputStream(file);
            stream.write(bytes);
            stream.flush();
            stream.close();
        } catch (Throwable throwable) {}

    }
     */
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onMobChangeTarget(LivingChangeTargetEvent event) {
        LivingEntity target = event.getNewTarget();
        LivingEntity entity = event.getEntity();
        if (target != null && (target.tags.contains(EntityExpandedContext.GR_MAY_FRIENDLY_TAG) || EntityExpandedContext.isOwnerFriendlyTag(target))) {
            if (entity.tags.contains(EntityExpandedContext.GR_MAY_FRIENDLY_TAG) || EntityExpandedContext.isOwnerFriendlyTag(entity)) {
                if (entity.tags.contains(EntityExpandedContext.GR_FT_CHURCH) && target.tags.contains(EntityExpandedContext.GR_FT_CHURCH)) {
                    event.setCanceled(true);
                } else if (EntityExpandedContext.isOwnerFriendlyTag_Church(entity) && EntityExpandedContext.isOwnerFriendlyTag_Church(target))
                    event.setCanceled(true);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onLevelLoader(ServerStartedEvent event) {
        try {
            ServerLevel serverLevel = event.getServer().overworld();
            DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(serverLevel);
            GRSavedDataEC savedDataEC = (GRSavedDataEC) state;
            GRSavedDataExpandedContext context = savedDataEC.revelationfix$dataEC();
            TheEndPuzzleItems.bake();
            if (vanillaItems.isEmpty()) {
                for (Item item : ForgeRegistries.ITEMS.getValues()) {
                    if (item.getDefaultInstance().isEmpty()) continue;
                    if (item == Items.DRAGON_EGG) continue;
                    if (BuiltInRegistries.ITEM.getKey(item).getNamespace().equals("minecraft"))
                        vanillaItems.add(item);
                }
            }
            if (context.getTheEndCraftItem() == null) {
                int i = serverLevel.random.nextInt(0, vanillaItems.size());
                Item item = (Item) vanillaItems.toArray()[i];
                context.setTheEndCraftItem(item);
                vanillaItems.remove(item);
            }
            if (!TheEndPuzzleItems.validateItem(context.getPUZZLE1())) {
                int i = serverLevel.random.nextInt(0, TheEndPuzzleItems.puzzleItems.size());
                Item item = (Item) TheEndPuzzleItems.puzzleItems.keySet().toArray()[i];
                context.setPUZZLE1(item);
                TheEndPuzzleItems.puzzleItems.remove(item);
            }
            if (!TheEndPuzzleItems.validateItem(context.getPUZZLE2())) {
                int i = serverLevel.random.nextInt(0, TheEndPuzzleItems.puzzleItems.size());
                Item item = (Item) TheEndPuzzleItems.puzzleItems.keySet().toArray()[i];
                context.setPUZZLE2(item);
                TheEndPuzzleItems.puzzleItems.remove(item);
            }
            if (!TheEndPuzzleItems.validateItem(context.getPUZZLE3())) {
                int i = serverLevel.random.nextInt(0, TheEndPuzzleItems.puzzleItems.size());
                Item item = (Item) TheEndPuzzleItems.puzzleItems.keySet().toArray()[i];
                context.setPUZZLE3(item);
                TheEndPuzzleItems.puzzleItems.remove(item);
            }
            if (!TheEndPuzzleItems.validateItem(context.getPUZZLE4())) {
                int i = serverLevel.random.nextInt(0, TheEndPuzzleItems.puzzleItems.size());
                Item item = (Item) TheEndPuzzleItems.puzzleItems.keySet().toArray()[i];
                context.setPUZZLE4(item);
                TheEndPuzzleItems.puzzleItems.remove(item);
            }

            TheEndRitualItemContext.PUZZLE1 = ForgeRegistries.ITEMS.getValue(context.getPUZZLE1());
            TheEndRitualItemContext.PUZZLE2 = ForgeRegistries.ITEMS.getValue(context.getPUZZLE2());
            TheEndRitualItemContext.PUZZLE3 = ForgeRegistries.ITEMS.getValue(context.getPUZZLE3());
            TheEndRitualItemContext.PUZZLE4 = ForgeRegistries.ITEMS.getValue(context.getPUZZLE4());
            TheEndRitualItemContext.THE_END_CRAFT = ForgeRegistries.ITEMS.getValue(context.getTheEndCraftItem());
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE1.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES);
                TheEndRitualItemContext.PUZZLE1.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE2.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES2);
                TheEndRitualItemContext.PUZZLE2.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE3.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES3);
                TheEndRitualItemContext.PUZZLE3.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE4.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES4);
                TheEndRitualItemContext.PUZZLE4.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.THE_END_CRAFT.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_CRAFTING);
                TheEndRitualItemContext.THE_END_CRAFT.builtInRegistryHolder().bindTags(tagKeys);
            }
            PacketHandler.sendToAll(new TheEndPuzzleUpdatePacket(context.getPUZZLE1(), context.getPUZZLE2(), context.getPUZZLE3(), context.getPUZZLE4(), context.getTheEndCraftItem()));

        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    @SubscribeEvent
    public static void writeDataToNewPlayer(PlayerEvent.PlayerLoggedInEvent event) {
        try {
            if (event.getEntity().getServer() == null) return;
            DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(event.getEntity().getServer());
            GRSavedDataEC savedDataEC = (GRSavedDataEC) state;
            GRSavedDataExpandedContext context = savedDataEC.revelationfix$dataEC();
            if (isValid(context.getPUZZLE1()) && isValid(context.getPUZZLE2()) && isValid(context.getPUZZLE3()) && isValid(context.getPUZZLE4()) && isValid(context.getTheEndCraftItem()))
                PacketHandler.sendToAll(new TheEndPuzzleUpdatePacket(context.getPUZZLE1(), context.getPUZZLE2(), context.getPUZZLE3(), context.getPUZZLE4(), context.getTheEndCraftItem()));
            else
                RevelationFixMixinPlugin.LOGGER.debug("Checked Invalid puzzle/end_craft : {}", context.getPUZZLE1(), context.getPUZZLE2(), context.getPUZZLE3(), context.getPUZZLE4(), context.getTheEndCraftItem());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    static boolean isValid(ResourceLocation resourceLocation) {
        return resourceLocation != null && !resourceLocation.getPath().isEmpty();
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        if (event.getTarget() instanceof ObsidianMonolith monolith) {
            if (!monolith.level.isClientSide) {
                if (monolith.getMasterOwner() == event.getEntity() && monolith.getTrueOwner() instanceof ApostleServant servant && event.getEntity().isShiftKeyDown()) {
                    monolith.die(event.getEntity().damageSources().playerAttack(event.getEntity()));
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public static void resistanceAttributeHandler(LivingHurtEvent event) {
        LivingEntity living = event.getEntity();
        if (living.level.isClientSide) return;
        Attribute attribute = ModAttributes.DAMAGE_RESISTANCE.get();
        AttributeInstance attributeInstance = living.getAttribute(attribute);
        double value;
        if (attributeInstance != null && (value = living.getAttributeValue(attribute)) > 1.0D) {
            event.setAmount((float) (event.getAmount() * (2.0F - Mth.clamp(value, 1.0D, 2.0D))));
        }
    }

    @SubscribeEvent
    public static void odamaneFinalDeath(EarlyLivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && player.level.dimension() == Level.END) {
            if (player.tags.contains("odamaneFinalDeath")) {
                player.tags.remove("odamaneFinalDeath");
                player.setHealth(player.getMaxHealth());
                BlockPos blockpos = player.getRespawnPosition();
                float f = player.getRespawnAngle();
                boolean flag = player.isRespawnForced();
                ServerLevel serverlevel = player.server.getLevel(player.getRespawnDimension());
                Optional<Vec3> optional;
                if (serverlevel != null && blockpos != null) {
                    optional = Player.findRespawnPositionAndUseSpawnBlock(serverlevel, blockpos, f, flag, false);
                } else {
                    optional = Optional.empty();
                }
                ServerLevel serverlevel1 = serverlevel != null && optional.isPresent() ? serverlevel : player.server.overworld();
                if (optional.isPresent()) {
                    BlockState blockstate = serverlevel1.getBlockState(blockpos);
                    boolean flag1 = blockstate.is(Blocks.RESPAWN_ANCHOR);
                    Vec3 vec3 = optional.get();
                    float f1;
                    if (!blockstate.is(BlockTags.BEDS) && !flag1) {
                    } else {
                        Vec3 vec31 = Vec3.atBottomCenterOf(blockpos).subtract(vec3).normalize();
                    }
                    player.setPos(vec3.x, vec3.y, vec3.z);
                    PacketHandler.sendToAll(new TheEndDeathPacket(player.getId(), new Vector3f((float) vec3.x, (float) vec3.y, (float) vec3.z)));
                }

                event.setCanceled(true);
            }
        }
    }

    @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class TimeStopEvents {
        static boolean cannotMove(PlayerInteractEvent event) {
            return TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(event.getLevel()) && !TimeStopUtils.canMove(event.getEntity());
        }

        @SubscribeEvent
        public static void disablePlayerInteract(PlayerInteractEvent.EntityInteractSpecific event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (cannotMove(event))
                event.setCanceled(true);
        }

        @SubscribeEvent
        public static void disablePlayerInteract(PlayerInteractEvent.EntityInteract event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (cannotMove(event))
                event.setCanceled(true);
        }

        @SubscribeEvent
        public static void disablePlayerInteract(PlayerInteractEvent.RightClickBlock event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (cannotMove(event))
                event.setCanceled(true);
        }

        @SubscribeEvent
        public static void disablePlayerInteract(PlayerInteractEvent.RightClickItem event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (cannotMove(event))
                event.setCanceled(true);
        }

        @SubscribeEvent
        public static void disablePlayerInteract(PlayerInteractEvent.LeftClickBlock event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (cannotMove(event))
                event.setCanceled(true);
        }

        @SubscribeEvent
        //only server
        public static void dimensionChangeEvent(EntityTravelToDimensionEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (event.getEntity() instanceof Player player) {
                if (TimeStopUtils.isTimeStop) {
                    ResourceKey<Level> travellingTo = event.getDimension();
                    if (!player.level.isClientSide) {
                        if (travellingTo != null && !travellingTo.location().equals(player.level.dimension().location())) {
                            TimeStopEntityData.setTimeStopCount(player, 0);
                            TimeStopUtils.use(false, player);
                        }
                    }
                }
            }
        }

        @SubscribeEvent
        public static void disableTimeStop2(LivingDeathEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (event.getEntity() instanceof Player player && event.getPhase() == EventPriority.LOWEST) {
                if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(player.level())) {
                    Level level = player.level();
                    if (!level.isClientSide) {
                        TimeStopUtils.use(false, player);
                    }
                }
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void hurtTime0(LivingHurtEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(event.getEntity().level()) && !(event.getEntity() instanceof Player)) {
                event.getEntity().invulnerableTime = 0;
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void hurtTime0(LivingDamageEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(event.getEntity().level()) && !(event.getEntity() instanceof Player)) {
                event.getEntity().invulnerableTime = 0;
            }
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void hurtTime0(LivingAttackEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(event.getEntity().level()) && !(event.getEntity() instanceof Player)) {
                event.getEntity().invulnerableTime = 0;
            }
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void attack(AttackEntityEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            Level level = event.getEntity().level;
            if (TimeStopUtils.isTimeStop && !TimeStopUtils.canMove(event.getEntity()) && TimeStopUtils.andSameDimension(level)) {
                event.setCanceled(true);
            }
        }

        @SubscribeEvent
        public static void onPlayerLeave(PlayerEvent.PlayerLoggedOutEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(event.getEntity().level)) {
                try {
                    TimeStopEntityData.setTimeStopCount(event.getEntity(), 0);
                    PacketHandler.sendToPlayer((ServerPlayer) event.getEntity(), new TimeStopSkillPacket(false, event.getEntity().getUUID()));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        @SubscribeEvent
        public static void onPlayerLeave(PlayerEvent.PlayerLoggedInEvent event) {
            if (SafeClass.isFantasyEndingLoaded()) return;
            if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(event.getEntity().level)) {
                try {
                    PacketHandler.sendToPlayer((ServerPlayer) event.getEntity(), new TimeStopSkillPacket(true, event.getEntity().getUUID()));
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        }

        @SubscribeEvent
        public static void eternalWatchFinalAttack(LivingDamageEvent event) {
            if (event.getSource().getEntity() instanceof ServerPlayer player && ATAHelper2.hasEternalWatch(player)) {
                if (SafeClass.getTimeStopCount(player) > 1) {
                    SafeClass.enableTimeStop(player, false);
                    event.setAmount(event.getAmount() + event.getEntity().getMaxHealth() * (float) ItemConfig.ewFinalAttackPercentage);
                }
            }
        }
    }

    @Mod.EventBusSubscriber
    public static class NeedleAttributeHandler {

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            replaceAttributeModifier(event.getOriginal(), event.getEntity(), Attributes.MAX_HEALTH, TheNeedleItem.HEALTH.getId());
            replaceAttributeModifier(event.getOriginal(), event.getEntity(), Attributes.ATTACK_DAMAGE, TheNeedleItem.ATTACK_DAMAGE.getId());
            replaceAttributeModifier(event.getOriginal(), event.getEntity(), Attributes.ATTACK_SPEED, TheNeedleItem.ATTACK_SPEED.getId());
            replaceAttributeModifier(event.getOriginal(), event.getEntity(), ModAttributes.DAMAGE_RESISTANCE.get(), TheNeedleItem.RESISTANCE.getId());
            event.getEntity().attributes.getDirtyAttributes().add(event.getEntity().getAttribute(Attributes.ATTACK_DAMAGE));
            if (!event.getEntity().level.isClientSide)
                ((PlayerInterface) event.getEntity()).revelationfix$odamaneHaloExpandedContext().setBlasphemous(TimeStopSavedData.isPlayerBlasphemous(event.getEntity()));
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity().level.isClientSide) return;
            AttributeInstance attributeInstance = event.getEntity().getAttribute(Attributes.ATTACK_DAMAGE);
            if (attributeInstance != null && attributeInstance.hasModifier(TheNeedleItem.ATTACK_DAMAGE)) {
                event.getEntity().attributes.getDirtyAttributes().add(event.getEntity().getAttribute(Attributes.ATTACK_DAMAGE));
            }
        }

        @SubscribeEvent
        public static void onPlayerLoad(PlayerEvent.LoadFromFile event) {
            if (event.getEntity().level.isClientSide) return;
            AttributeInstance attributeInstance = event.getEntity().getAttribute(Attributes.ATTACK_DAMAGE);
            if (attributeInstance != null && attributeInstance.hasModifier(TheNeedleItem.ATTACK_DAMAGE)) {
                event.getEntity().attributes.getDirtyAttributes().add(event.getEntity().getAttribute(Attributes.ATTACK_DAMAGE));
            }
        }

        private static void replaceAttributeModifier(Player old, Player _new, Attribute attribute, UUID modifierID) {
            AttributeInstance attributeInstance = old.getAttribute(attribute);
            if (attributeInstance != null) {
                AttributeModifier modifier = attributeInstance.getModifier(modifierID);
                if (modifier != null) {
                    attributeInstance = _new.getAttribute(attribute);
                    if (attributeInstance != null)
                        attributeInstance.addPermanentModifier(modifier);
                }
            }
        }
    }

    @Mod.EventBusSubscriber
    public static class ModItemEvents {
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void modWeaponHurtEvent1(LivingHurtEvent event) {
            if (event.getSource().getEntity() instanceof LivingEntity living)
                if (living.getMainHandItem().getItem() instanceof ICustomHurtWeapon weapon)
                    weapon.onHurt(living.getMainHandItem(), event);
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void modWeaponHurtEvent2(LivingAttackEvent event) {
            if (event.getSource().getEntity() instanceof LivingEntity living)
                if (living.getMainHandItem().getItem() instanceof ICustomHurtWeapon weapon)
                    weapon.onAttack(living.getMainHandItem(), event);
            if (event.getSource().getDirectEntity() instanceof GungnirSpearEntity)
                ((LivingEventEC) event).revelationfix$hackedUnCancelable(true);
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void modWeaponHurtEvent3(LivingDamageEvent event) {
            if (event.getSource().getEntity() instanceof LivingEntity living)
                if (living.getMainHandItem().getItem() instanceof ICustomHurtWeapon weapon)
                    weapon.onDamage(living.getMainHandItem(), event);
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void modWeaponHurtEvent4(AttackEntityEvent event) {
            if (event.getEntity().getMainHandItem().getItem() instanceof ICustomHurtWeapon weapon)
                weapon.onAttackEntity(event.getEntity().getMainHandItem(), event);
        }

        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public static void modWeaponHurtEvent5(LivingDeathEvent event) {
            if (event.getSource().getEntity() instanceof LivingEntity living)
                if (living.getMainHandItem().getItem() instanceof ICustomHurtWeapon weapon)
                    weapon.onDeath(living.getMainHandItem(), event, EventPriority.HIGHEST);
        }

        @SubscribeEvent(priority = EventPriority.LOWEST)
        public static void modWeaponHurtEvent(LivingDeathEvent event) {
            if (event.getSource().getEntity() instanceof LivingEntity living)
                if (living.getMainHandItem().getItem() instanceof ICustomHurtWeapon weapon)
                    weapon.onDeath(living.getMainHandItem(), event, EventPriority.LOWEST);
        }
    }
}
