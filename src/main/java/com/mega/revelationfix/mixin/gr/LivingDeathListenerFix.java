package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.safe.GRSavedDataExpandedContext;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;
import z1gned.goetyrevelation.event.LivingDeathListener;
import z1gned.goetyrevelation.item.ModItems;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(value = LivingDeathListener.class, remap = false)
public class LivingDeathListenerFix {
    @Inject(method = "onLivingDeath", at = @At("HEAD"), cancellable = true)
    private static void fix(LivingDeathEvent event, CallbackInfo ci) {
        ci.cancel();
        Entity entity = event.getEntity();
        if (entity.level().isClientSide || !(entity.level() instanceof ServerLevel)) {
            return;
        }
        if (entity instanceof Apostle apostle) {
            if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
                ItemStack medalS = new ItemStack(ModItems.DOOM_MEDAL.get());
                if (apostle.isInNether()) {
                    apostle.spawnAtLocation(new ItemStack(GRItems.DISC_2.get()));
                } else {
                    apostle.spawnAtLocation(new ItemStack(GRItems.DISC_1.get()));
                }
                if (apostle.isInNether()) medalS.setCount(10);
                ItemEntity medal = new ItemEntity(entity.level(), apostle.getX(), apostle.getY(), apostle.getZ(), medalS);
                ServerLevel world = (ServerLevel) entity.level();
                world.addFreshEntity(medal);
                if (apostle.isInNether()) {
                    DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(((ServerLevel) entity.level()).server);
                    if (!state.isDropped()) {
                        state.setDropped(true);
                        ItemEntity item = new ItemEntity(world, apostle.getX(), apostle.getY(), apostle.getZ(), new ItemStack(ModItems.WITHER_QUIETUS.get()));
                        world.addFreshEntity(item);
                        state.setDirty();
                    }
                }
            }
        }

    }
}
