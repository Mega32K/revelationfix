package com.mega.revelationfix.common.init;

import com.mega.revelationfix.api.event.register.RunestoneRitualExecutorRegisterEvent;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.kjs.KjsSafeClass;
import com.mega.revelationfix.safe.entity.EntityExpandedContext;
import com.mega.revelationfix.util.LivingEntityEC;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;

public class RunestoneRitualInit {
    public static Int2ObjectOpenHashMap<RunestoneRitualExe> registries = new Int2ObjectOpenHashMap<>();
    public static void register(int structureCode, RunestoneRitualExe exe) {
        if (registries.containsKey(structureCode))
            throw new RuntimeException("Duplicate runestone ritual exe: " + structureCode);
        registries.put(structureCode, exe);
    }
    static {
        {
            register(1111, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, reactorBlockEntity.getRitualRange(), EntityExpandedContext.NO_GODS)) {
                    if (living == reactorBlockEntity.getOwner()) continue;
                    EntityExpandedContext ec = ((LivingEntityEC) living).revelationfix$livingECData();
                    ec.banAnySpelling = 20;
                }
            }));
            register(4000, ((serverLevel, reactorPos, reactorState, reactorBlockEntity) -> {
                int k = serverLevel.getGameRules().getInt(GameRules.RULE_RANDOMTICKING);
                if (k > 0 && serverLevel.random.nextBoolean()) {
                    ProfilerFiller profilerfiller = serverLevel.getProfiler();
                    AABB aabb = reactorBlockEntity.getRitualRange();
                    profilerfiller.push("rune_reactor_randomTick");
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
                for (LivingEntity living : serverLevel.getEntitiesOfClass(LivingEntity.class, reactorBlockEntity.getRitualRange(), EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
                    EntityExpandedContext ec = ((LivingEntityEC) living).revelationfix$livingECData();
                    ec.banAnySpelling = 20;
                }
            }));
        }
        MinecraftForge.EVENT_BUS.start();
        MinecraftForge.EVENT_BUS.post(new RunestoneRitualExecutorRegisterEvent());
        if (SafeClass.isKJSLoaded()) {
            KjsSafeClass.postRunestoneEvent_0();
        }
    }
}
