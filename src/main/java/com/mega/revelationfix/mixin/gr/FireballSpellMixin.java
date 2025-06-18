package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.projectiles.HellBolt;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.nether.FireballSpell;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ATAHelper;

@Mixin(FireballSpell.class)
public abstract class FireballSpellMixin extends Spell {
    @Unique
    LivingEntity allTitlesApostle_1_20_1$user;

    @Unique
    private static boolean revelationfix$isRightPlayer(LivingEntity living) {
        return ATAHelper2.hasOdamane(living) || ATAHelper.hasHalo(living);
    }

    @Inject(at = @At("HEAD"), method = "SpellResult", remap = false)
    private void getSpellUser(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$user = caster;
    }

    @Inject(at = @At("RETURN"), method = "defaultSpellCooldown", cancellable = true, remap = false)
    private void getSpellCooldown(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(revelationfix$isRightPlayer(this.allTitlesApostle_1_20_1$user) ? 0 : cir.getReturnValueI());
    }

    @Inject(at = @At("RETURN"), method = "defaultCastDuration", cancellable = true, remap = false)
    private void getUseTime(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ATAHelper.halfValueCondition(revelationfix$isRightPlayer(this.allTitlesApostle_1_20_1$user), cir.getReturnValueI()));
    }

    @Inject(at = @At("HEAD"), method = "SpellResult", cancellable = true, remap = false)
    private void createFireball(ServerLevel worldIn, LivingEntity entityLiving, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        if ((this.rightStaff(staff) && ATAHelper.hasHalo(this.allTitlesApostle_1_20_1$user)) || ATAHelper2.hasOdamane(entityLiving)) {
            ci.cancel();
            Vec3 vector3d = entityLiving.getViewVector(1.0F);
            HellBolt hellBolt = new HellBolt(entityLiving.getX() + vector3d.x / 2.0, entityLiving.getEyeY() - 0.2, entityLiving.getZ() + vector3d.z / 2.0, vector3d.x, vector3d.y, vector3d.z, worldIn);
            hellBolt.setOwner(entityLiving);
            worldIn.playSound(null, entityLiving.getX(), entityLiving.getY(), entityLiving.getZ(), SoundEvents.BLAZE_SHOOT, this.getSoundSource(), 1.0F, 1.0F);
            worldIn.addFreshEntity(hellBolt);
        }
    }
}
