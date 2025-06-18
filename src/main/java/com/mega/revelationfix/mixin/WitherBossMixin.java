package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(WitherBoss.class)
public abstract class WitherBossMixin extends Monster {
    @Shadow
    @Final
    @Mutable
    private static Predicate<LivingEntity> LIVING_ENTITY_SELECTOR;

    protected WitherBossMixin(EntityType<? extends Monster> p_33002_, Level p_33003_) {
        super(p_33002_, p_33003_);
    }

    @Inject(
            at = {@At(
                    value = "FIELD",
                    shift = At.Shift.AFTER,
                    target = "Lnet/minecraft/world/entity/boss/wither/WitherBoss;LIVING_ENTITY_SELECTOR:Ljava/util/function/Predicate;"
            )},
            method = {"<clinit>"}
    )

    private static void addFormatting(CallbackInfo ci) {
        LIVING_ENTITY_SELECTOR = LIVING_ENTITY_SELECTOR.and((e) -> !(e instanceof Player player) || !ATAHelper2.hasOdamane(player));
    }

    @Inject(method = "dropCustomDeathLoot", at = @At("TAIL"))
    private void dropCustomDeathLoot(DamageSource p_31464_, int p_31465_, boolean p_31466_, CallbackInfo ci) {
        if (random.nextFloat() < 0.025F) {
            ItemEntity itementity = this.spawnAtLocation(GRItems.QUIETUS_STAR.get());
            if (itementity != null) {
                itementity.setExtendedLifetime();
            }
        }
    }
}
