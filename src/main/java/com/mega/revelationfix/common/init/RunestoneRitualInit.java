package com.mega.revelationfix.common.init;

import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.projectiles.DeathArrow;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.init.ModTags;
import com.Polarice3.Goety.utils.MobUtil;
import com.mega.revelationfix.api.event.register.RunestoneRitualExecutorRegisterEvent;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.kjs.KjsSafeClass;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.LivingEntityEC;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;

public class RunestoneRitualInit {
    public static final int RITUAL_0 = 1111;
    public static final int RITUAL_1 = 4000;
    public static final int RITUAL_2 = 4;
    public static final int RITUAL_3 = 1003;
    public static final int RITUAL_4 = 13;
    public static final int RITUAL_5 = 103;
    public static final int RITUAL_6 = 400;
    public static final int RITUAL_7 = 202;
    public static final int RITUAL_8 = 220;
    public static final int RITUAL_9 = 3010;
    private static final double ZOMBIE_SPEED = 0.0623D;
    public static Int2ObjectOpenHashMap<RunestoneRitualExe> registries = new Int2ObjectOpenHashMap<>();
    public static void register(int structureCode, RunestoneRitualExe exe) {
        if (registries.containsKey(structureCode))
            throw new RuntimeException("Duplicate runestone ritual exe: " + structureCode);
        registries.put(structureCode, exe);
    }
    static {

        {
            register(RITUAL_0, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, reactorBlockEntity.getRitualRange().inflate(0.5), EntityExpandedContext.NO_GODS.and((entity -> !MobUtil.areAllies(entity, reactorBlockEntity.getOwner()))))) {
                    if (living == reactorBlockEntity.getOwner()) continue;
                    EntityExpandedContext ec = ((LivingEntityEC) living).revelationfix$livingECData();
                    ec.banAnySpelling = 20;
                }
            }));
            register(RITUAL_1, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                int k = serverLevel.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
                if (k > 0 && serverLevel.random.nextBoolean()) {
                    ProfilerFiller profilerfiller = serverLevel.getProfiler();
                    AABB aabb = reactorBlockEntity.getRitualRange();
                    for (int i=(int)aabb.minX;i<=(int)aabb.maxX;i++) {
                        for (int j=(int)aabb.minZ;j<=(int)aabb.maxZ;j++) {
                            for (int k0=(int)aabb.minY+6;k0<=(int)aabb.maxY-6;k0++) {
                                BlockPos pos = new BlockPos(i, k0, j);
                                BlockState blockstate2 = serverLevel.getBlockState(pos);
                                Block block = blockstate2.getBlock();
                                if ((block instanceof IPlantable || block instanceof FarmBlock) && blockstate2.isRandomlyTicking()) {
                                    block.randomTick(blockstate2, serverLevel, pos, serverLevel.random);
                                }
                                FluidState fluidstate = blockstate2.getFluidState();
                                if (fluidstate.isRandomlyTicking()) {
                                    fluidstate.randomTick(serverLevel, pos, serverLevel.random);
                                }
                            }

                        }
                    }
                    profilerfiller.pop();
                }
            }));

            register(RITUAL_2, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                AABB aabb = reactorBlockEntity.getRitualRange().inflate(0.5);
                double size = aabb.getSize();
                double size1 = size / 3D;
                Vec3 center = aabb.getCenter();
                float reverse = reactorBlockEntity.getInsertItem().is(ModTags.Items.TOTEMS) ? -1F : 1F;
                for (Entity entity : serverLevel.getEntitiesOfClass(Entity.class, aabb, EntityExpandedContext.NO_GODS)) {
                    if (entity == reactorBlockEntity.getOwner() || entity instanceof DeathArrow) continue;
                    float multiplier = 1F;
                    if (entity instanceof Projectile projectile) {
                        multiplier = 3F;
                    }
                    double distanceToCenter = Math.max(0.5F, entity.distanceToSqr(center));
                    Vec3 pos = (entity.position().add(center.scale(-1F))).normalize().scale(size1 / distanceToCenter).scale(multiplier*reverse);
                    entity.hurtMarked = true;
                    entity.push(Mth.clamp(pos.x, -0.5F, 0.5F), Mth.clamp(pos.y, -0.5F, 0.5F), Mth.clamp(pos.z, -0.5F, 0.5F));
                }
            }));
            register(RITUAL_3, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                AABB aabb = reactorBlockEntity.getRitualRange().inflate(0.5);
                Item item = reactorBlockEntity.getInsertItem().getItem();
                double power = 0F;
                if (item == ModItems.TOTEM_OF_ROOTS.get())
                    power = 1D;
                else if (item == ModItems.TOTEM_OF_SOULS.get())
                    power = 1.5D;
                else if (item == ModItems.SOUL_TRANSFER.get())
                    power = 2D;
                Vec3i direction = reactorState.getValue(RuneReactorBlock.FACING).getNormal();
                Vec3 motion = new Vec3(direction.getX() * 0.1F, direction.getY() * 0.1F, direction.getZ() * 0.1F).scale(power * -1F);
                for (Entity entity : serverLevel.getEntitiesOfClass(Entity.class, aabb, EntityExpandedContext.NO_GODS)) {
                    if (entity == reactorBlockEntity.getOwner() && !entity.isShiftKeyDown()) continue;
                    entity.hurtMarked = true;
                    entity.push(motion.x, motion.y, motion.z);
                }
            }));
            register(RITUAL_4, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                AABB aabb = reactorBlockEntity.getRitualRange().inflate(0.5);
                Item item = reactorBlockEntity.getInsertItem().getItem();
                double power = 0F;
                if (item == ModItems.TOTEM_OF_ROOTS.get())
                    power = .5D;
                else if (item == ModItems.TOTEM_OF_SOULS.get())
                    power = 1D;
                else if (item == ModItems.SOUL_TRANSFER.get())
                    power = 2D;
                for (Entity entity : serverLevel.getEntitiesOfClass(Entity.class, aabb, EntityExpandedContext.NO_GODS)) {
                    if (entity == reactorBlockEntity.getOwner()) entity.resetFallDistance();
                    if ((entity == reactorBlockEntity.getOwner() && !entity.isShiftKeyDown()) || entity instanceof Projectile) continue;
                    entity.hurtMarked = true;
                    entity.push(0F, 0.1F * power, 0F);
                }
            }));
            register(RITUAL_5, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                AABB aabb = reactorBlockEntity.getRitualRange().inflate(0.5);
                Item item = reactorBlockEntity.getInsertItem().getItem();
                double power = 0F;
                if (item == ModItems.TOTEM_OF_ROOTS.get())
                    power = .5D;
                else if (item == ModItems.TOTEM_OF_SOULS.get())
                    power = 1D;
                else if (item == ModItems.SOUL_TRANSFER.get())
                    power = 2D;
                for (Entity entity : serverLevel.getEntitiesOfClass(Entity.class, aabb, EntityExpandedContext.NO_GODS)) {
                    if (entity == reactorBlockEntity.getOwner()) entity.resetFallDistance();
                    if ((entity == reactorBlockEntity.getOwner() && !entity.isShiftKeyDown()) || entity instanceof Projectile) continue;
                    entity.hurtMarked = true;
                    entity.push(0F, -0.1F * power, 0F);
                }
            }));
            register(RITUAL_6, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                if (reactorBlockEntity.tickCount % (reactorBlockEntity.getRitualDelay()*4) == 0) {
                    for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, reactorBlockEntity.getRitualRange().inflate(0.5), EntityExpandedContext.NO_GODS.and((entity -> !MobUtil.areAllies(entity, reactorBlockEntity.getOwner()))))) {
                        if (living == reactorBlockEntity.getOwner()) continue;
                        living.invulnerableTime = 0;
                        if (living.hurt(living.damageSources().starve(), 1.5F)) {
                            living.addEffect(new MobEffectInstance(MobEffects.HUNGER, 80, 1));
                            living.addEffect(new MobEffectInstance(GoetyEffects.SOUL_HUNGER.get(), 80, 0));
                            living.level().levelEvent(232424314, living.blockPosition(), 5);
                            if (reactorBlockEntity.getOwner() != null)
                                reactorBlockEntity.getOwner().addEffect(new MobEffectInstance(MobEffects.SATURATION, 10, 0));
                        }
                        living.invulnerableTime = 0;
                    }
                }
            }));
            register(RITUAL_7, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, reactorBlockEntity.getRitualRange().inflate(0.5), EntityExpandedContext.NO_GODS.and((entity -> !MobUtil.areAllies(entity, reactorBlockEntity.getOwner()))))) {
                    if (living == reactorBlockEntity.getOwner()) continue;
                    double d0 = living.getDeltaMovement().x;
                    double d1 = living.getDeltaMovement().z;
                    double speed = Math.sqrt(d0*d0 + d1*d1);
                    if (speed <= 0.05) continue;
                    if (living.hurt(living.damageSources().flyIntoWall(), (float) Math.max(1F, speed / ZOMBIE_SPEED))) {
                        living.level().levelEvent(232424314, living.blockPosition(), 6);
                    }
                }
            }));
            register(RITUAL_8, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, reactorBlockEntity.getRitualRange().inflate(0.5), EntityExpandedContext.NO_GODS.and((entity -> !MobUtil.areAllies(entity, reactorBlockEntity.getOwner()))))) {
                    if (living == reactorBlockEntity.getOwner()) continue;
                    living.addEffect(new MobEffectInstance(ModEffects.COUNTERSPELL.get(), 100, 3));
                }
            }));
            register(RITUAL_9, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                //FINISHED
            }));
        }
        MinecraftForge.EVENT_BUS.start();
        MinecraftForge.EVENT_BUS.post(new RunestoneRitualExecutorRegisterEvent());
        if (SafeClass.isKJSLoaded()) {
            KjsSafeClass.postRunestoneEvent_0();
        }
    }
}
