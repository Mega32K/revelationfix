package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.NetherMeteor;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.nether.WitherSkullSpell;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.common.entity.projectile.StarArrow;
import com.mega.revelationfix.common.init.ModSounds;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import z1gned.goetyrevelation.util.ATAHelper;

@Mixin(WitherSkullSpell.class)
public abstract class WitherSkullSpellMixin extends Spell {
    @Unique
    LivingEntity allTitlesApostle_1_20_1$user;

    @Unique
    private static boolean revelationfix$isRightPlayer(LivingEntity living) {
        return ATAHelper2.hasOdamane(living) || ATAHelper.hasHalo(living);
    }

    @Inject(at = @At("RETURN"), method = "defaultSpellCooldown", cancellable = true, remap = false)
    private void getSpellCooldown(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ATAHelper.halfValueCondition(revelationfix$isRightPlayer(this.allTitlesApostle_1_20_1$user), cir.getReturnValueI()));
    }

    @Inject(at = @At("RETURN"), method = "defaultCastDuration", cancellable = true, remap = false)
    private void getUseTime(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ATAHelper.halfValueCondition(revelationfix$isRightPlayer(this.allTitlesApostle_1_20_1$user), cir.getReturnValueI()));
    }

    @Inject(at = @At("HEAD"), method = "SpellResult", cancellable = true, remap = false)
    private void createStar(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$user = caster;
        if (ATAHelper2.hasOdamane(caster)) {
            ci.cancel();
            Vec3 vector3d = caster.getViewVector(1.0F);
            float extraBlast = (float) WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.5F;
            StarArrow netherStar = new StarArrow(worldIn, caster, caster.getLookAngle().x * 4, caster.getLookAngle().y * 4, caster.getLookAngle().z * 4);
            netherStar.setOwner(caster);
            Vec3 shootPos = caster.getEyePosition().add(caster.getHandHoldingItemAngle(staff.getItem()));
            double d0 = shootPos.x;
            double d1 = shootPos.y;
            double d2 = shootPos.z;
            netherStar.setPosRaw(d0, d1, d2);
            netherStar.setAlpha(1F);
            if (this.isShifting(caster)) {
                netherStar.setDangerous(true);
            }
            if (rayTrace(worldIn, caster, 64, 3) instanceof EntityHitResult entityHitResult)
                netherStar.setTarget(entityHitResult.getEntity());
            netherStar.setDamageMultiplier((float) WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster) / 10F + 1F);
            worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), ModSounds.STAR_CAST.get(), this.getSoundSource(), 1.0F, 1.0F);
            worldIn.addFreshEntity(netherStar);
        } else if (this.rightStaff(staff) && ATAHelper.hasHalo(this.allTitlesApostle_1_20_1$user)) {
            ci.cancel();
            Vec3 vector3d = caster.getViewVector(1.0F);
            float extraBlast = (float) WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 2.5F;
            NetherMeteor netherStar = new NetherMeteor(caster.level(), caster.getX() + vector3d.x / 2.0, caster.getEyeY() - 0.2, caster.getZ() + vector3d.z / 2.0, vector3d.x, vector3d.y, vector3d.z);
            netherStar.setOwner(caster);
            if (this.isShifting(caster)) {
                netherStar.setDangerous(true);
            }

            netherStar.setExtraDamage((float) WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster));
            netherStar.setFiery(WandUtil.getLevels(ModEnchantments.BURNING.get(), caster));
            netherStar.setExplosionPower(netherStar.getExplosionPower() + extraBlast);
            worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), SoundEvents.BLAZE_SHOOT, this.getSoundSource(), 1.0F, 1.0F);
            worldIn.addFreshEntity(netherStar);
        }
    }
}
