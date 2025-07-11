package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.Polarice3.Goety.common.magic.Spell;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.safe.mixinpart.goety.ILevelWand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Spell.class)
public class SpellMixin {
    @Inject(method = "rightStaff", at = @At("HEAD"), cancellable = true, remap = false)
    private void rightStaff(ItemStack staff, CallbackInfoReturnable<Boolean> cir) {
        if (staff.getItem() instanceof DarkWand wand) {
            ILevelWand wandItf = (ILevelWand) wand;
            if (wandItf.expandedRightStaffLogic((Spell) (Object)this, staff))
                cir.setReturnValue(true);
        }
    }
    @Inject(method = "typeStaff", at = @At("HEAD"), cancellable = true, remap = false)
    private void typeStaff(ItemStack staff, SpellType spellType, CallbackInfoReturnable<Boolean> cir) {
        if (staff.getItem() instanceof DarkWand wand) {
            ILevelWand wandItf = (ILevelWand) wand;
            if (wandItf.expandedTypeStaffLogic(spellType, staff))
                cir.setReturnValue(true);
        }
    }
    @ModifyVariable(remap = false, method = "playSound(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/entity/Entity;Lnet/minecraft/sounds/SoundEvent;FF)V", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private Entity modifyEntity(Entity entity) {
        if (entity instanceof FakeSpellerEntity spellerEntity && spellerEntity.getOwner() instanceof Player player)
            return player;
        return entity;
    }
    @ModifyVariable(remap = false, method = "getTarget(Lnet/minecraft/world/entity/LivingEntity;I)Lnet/minecraft/world/entity/LivingEntity;", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private int range(int src, LivingEntity caster, int src_) {
        if (caster != null) {
            AttributeInstance attributeInstance = caster.getAttribute(ModAttributes.SPELL_RANGE.get());
            if (attributeInstance != null) {
                src = (int) Math.round(src + attributeInstance.getValue());
            }
        }
        return src;
    }
}
