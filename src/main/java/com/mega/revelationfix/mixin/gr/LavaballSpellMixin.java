package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.projectiles.HellBlast;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.nether.LavaballSpell;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ATAHelper;

@Mixin(LavaballSpell.class)
public abstract class LavaballSpellMixin extends Spell {
    @Unique
    LivingEntity allTitlesApostle_1_20_1$user;
    @Unique
    ItemStack allTitlesApostle_1_20_1$stack;

    @Unique
    private static boolean revelationfix$isRightPlayer(LivingEntity living) {
        return ATAHelper2.hasOdamane(living) || ATAHelper.hasHalo(living);
    }

    @Inject(at = @At("HEAD"), method = "SpellResult", remap = false)
    private void getSpellUser(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$user = caster;
        this.allTitlesApostle_1_20_1$stack = staff;
    }

    @Inject(at = @At("RETURN"), method = "defaultSpellCooldown", cancellable = true, remap = false)
    private void getSpellCooldown(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ATAHelper.halfValueCondition(revelationfix$isRightPlayer(this.allTitlesApostle_1_20_1$user), cir.getReturnValueI()));
    }

    @Inject(at = @At("RETURN"), method = "defaultCastDuration", cancellable = true, remap = false)
    private void getUseTime(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ATAHelper.halfValueCondition(revelationfix$isRightPlayer(this.allTitlesApostle_1_20_1$user), cir.getReturnValueI()));
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "SpellResult")
    private boolean createHellBlast(ServerLevel instance, Entity p_8837_) {
        Vec3 vector3d = this.allTitlesApostle_1_20_1$user.getViewVector(1.0F);
        if ((ATAHelper.hasHalo(this.allTitlesApostle_1_20_1$user) && this.rightStaff(this.allTitlesApostle_1_20_1$stack)) || ATAHelper2.hasOdamane(this.allTitlesApostle_1_20_1$user)) {
            HellBlast hellBlast = new HellBlast(this.allTitlesApostle_1_20_1$user.getX() + vector3d.x / 2.0 + instance.random.nextGaussian(), this.allTitlesApostle_1_20_1$user.getEyeY() - 0.2, this.allTitlesApostle_1_20_1$user.getZ() + vector3d.z / 2.0 + instance.random.nextGaussian(), vector3d.x, vector3d.y, vector3d.z, instance);
            hellBlast.setOwner(this.allTitlesApostle_1_20_1$user);
            p_8837_ = hellBlast;
        }
        return instance.addFreshEntity(p_8837_);
    }
}
