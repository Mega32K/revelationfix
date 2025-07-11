package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.entity.armor.ArmorUtils;
import com.mega.revelationfix.api.event.entity.EarlyLivingDeathEvent;
import com.mega.revelationfix.common.event.handler.ArmorEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(value = ForgeHooks.class, priority = 888)
public class ForgeHooksMixin {
    @Inject(method = "onLivingDeath", at = @At("HEAD"), cancellable = true, remap = false)
    private static void onLivingDeath(LivingEntity entity, DamageSource src, CallbackInfoReturnable<Boolean> cir) {
        boolean flag = MinecraftForge.EVENT_BUS.post(new EarlyLivingDeathEvent(entity, src));
        if (flag)
            cir.setReturnValue(true);
    }
    @Inject(remap = false, method = "isLivingOnLadder", at = @At("HEAD"), cancellable = true)
    private static void isLivingOnLadder(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull LivingEntity entity, CallbackInfoReturnable<Optional<BlockPos>> cir) {
        if (entity.horizontalCollision)

            if (ArmorEvents.isSpiderSet(ArmorUtils.getArmorSet(entity))) {
                boolean isSpectator = (entity instanceof Player && entity.isSpectator());
                if (isSpectator) cir.setReturnValue(Optional.empty());
                if (!ForgeConfig.SERVER.fullBoundingBoxLadders.get())
                {
                    cir.setReturnValue(Optional.of(pos));
                }
                else
                {
                    AABB bb = entity.getBoundingBox();
                    int mX = Mth.floor(bb.minX);
                    int mY = Mth.floor(bb.minY);
                    int mZ = Mth.floor(bb.minZ);
                    for (int y2 = mY; y2 < bb.maxY; y2++)
                    {
                        for (int x2 = mX; x2 < bb.maxX; x2++)
                        {
                            for (int z2 = mZ; z2 < bb.maxZ; z2++)
                            {
                                BlockPos tmp = new BlockPos(x2, y2, z2);
                                cir.setReturnValue(Optional.of(tmp));
                            }
                        }
                    }
                    cir.setReturnValue(Optional.empty());
                }
            }
    }
}
