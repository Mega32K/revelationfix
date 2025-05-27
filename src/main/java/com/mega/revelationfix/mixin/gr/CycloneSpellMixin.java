package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.projectiles.FireTornado;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.wind.CycloneSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.WandUtil;
import net.minecraft.server.level.ServerLevel;
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

@Mixin(CycloneSpell.class)
public abstract class CycloneSpellMixin extends Spell {
    @Unique
    LivingEntity allTitlesApostle_1_20_1$user;
    @Unique
    ItemStack allTitlesApostle_1_20_1$stack;

    @Inject(at = @At("HEAD"), method = "SpellResult", remap = false)
    private void getSpellUser(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        this.allTitlesApostle_1_20_1$user = caster;
        this.allTitlesApostle_1_20_1$stack = staff;
    }

    @Inject(at = @At("RETURN"), method = "defaultSpellCooldown", cancellable = true, remap = false)
    private void getSpellCooldown(CallbackInfoReturnable<Integer> cir) {
        cir.setReturnValue(ATAHelper.halfValueCondition(ATAHelper.hasHalo(this.allTitlesApostle_1_20_1$user), cir.getReturnValueI()) / 2);
    }

    @Inject(at = @At("HEAD"), method = "SpellResult", cancellable = true, remap = false)
    private void createFireTornado(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        if (ATAHelper.hasHalo(caster) && staff.is(ModItems.NETHER_STAFF.get())) {
            ci.cancel();
            if (this.getTarget(caster) != null) {
                Vec3 vector3d = caster.getViewVector(1.0F);
                FireTornado cyclone = new FireTornado(worldIn, caster.getX() + vector3d.x / 2.0, caster.getEyeY() - 0.2, caster.getZ() + vector3d.z / 2.0, vector3d.x, vector3d.y, vector3d.z);
                cyclone.setOwnerId(caster.getUUID());
                cyclone.setTarget(this.getTarget(caster));
                float size = 2.0F;
                float damage = 7.0F;
                if (this.rightStaff(staff)) {
                    ++size;
                    ++damage;
                }

                cyclone.setOwner(caster);
                cyclone.setDamage(damage * (float) (WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster) + 1));
                cyclone.setTotalLife(2400 * (WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1));
                cyclone.setBoltSpeed(WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster));
                cyclone.setSize(size + (float) WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 10.0F);
                worldIn.addFreshEntity(cyclone);
                worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), ModSounds.APOSTLE_PREPARE_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
            } else {
                FireTornado cyclone = new FireTornado(worldIn, caster, caster.getX(), caster.getY(), caster.getZ());
                cyclone.setOwnerId(caster.getUUID());
                float size = 2.0F;
                float damage = 7.0F;
                if (this.rightStaff(staff)) {
                    ++size;
                    ++damage;
                }

                cyclone.setOwner(caster);
                cyclone.setDamage(damage * (float) (WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster) + 1));
                cyclone.setTotalLife(2400 * (WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1));
                cyclone.setBoltSpeed(WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster));
                cyclone.setSize(size + (float) WandUtil.getLevels(ModEnchantments.RADIUS.get(), caster) / 10.0F);
                worldIn.addFreshEntity(cyclone);
                worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), ModSounds.APOSTLE_PREPARE_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
            }
        }
    }

}
