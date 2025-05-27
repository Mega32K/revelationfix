package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.hostile.servants.Malghast;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.common.magic.spells.nether.GhastSpell;
import com.Polarice3.Goety.config.SpellConfig;
import com.Polarice3.Goety.utils.EffectsUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.util.ATAHelper;

@Mixin(GhastSpell.class)
public abstract class GhastSpellMixin extends SummonSpell {
    @Unique
    LivingEntity allTitlesApostle_1_20_1$user;
    @Unique
    ItemStack allTitlesApostle_1_20_1$stack;
    @Unique
    SpellStat allTitlesApostle_1_20_1$spellStat;

    @Inject(at = @At("HEAD"), method = "SpellResult", remap = false)
    private void getSpellUser(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$user = caster;
        this.allTitlesApostle_1_20_1$stack = staff;
        this.allTitlesApostle_1_20_1$spellStat = spellStat;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"), method = "SpellResult")
    private boolean createGhast(ServerLevel instance, Entity p_8837_) {
        if (ATAHelper.hasHalo(this.allTitlesApostle_1_20_1$user) && this.rightStaff(this.allTitlesApostle_1_20_1$stack)) {
            BlockPos blockpos = this.allTitlesApostle_1_20_1$user.blockPosition().offset(-2 + this.allTitlesApostle_1_20_1$user.getRandom().nextInt(5), 1, -2 + this.allTitlesApostle_1_20_1$user.getRandom().nextInt(5));
            Malghast malghast = new Malghast(ModEntityType.MALGHAST.get(), instance);
            malghast.setTrueOwner(this.allTitlesApostle_1_20_1$user);
            malghast.moveTo(blockpos, this.allTitlesApostle_1_20_1$user.getYRot(), 0.0F);
            malghast.finalizeSpawn(instance, this.allTitlesApostle_1_20_1$user.level().getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, null, null);
            this.SummonSap(this.allTitlesApostle_1_20_1$user, malghast);
            int boost = Mth.clamp(this.allTitlesApostle_1_20_1$spellStat.potency, 0, 10);
            malghast.setFireBallDamage((float) (boost - EffectsUtil.getAmplifierPlus(this.allTitlesApostle_1_20_1$user, MobEffects.WEAKNESS)));
            float extraBlast = (float) Mth.clamp(this.allTitlesApostle_1_20_1$spellStat.potency, 0, SpellConfig.MaxRadiusLevel.get()) / 2.5F;
            malghast.setExplosionPower((malghast).getExplosionPower() + extraBlast);
            this.setTarget(this.allTitlesApostle_1_20_1$user, malghast);
            p_8837_ = malghast;
        }

        return instance.addFreshEntity(p_8837_);
    }
}