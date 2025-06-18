package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.items.magic.DarkWand;
import com.Polarice3.Goety.common.items.magic.NamelessStaff;
import com.Polarice3.Goety.common.magic.Spell;
import com.mega.revelationfix.common.apollyon.client.effect.LoreHelper;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.safe.mixinpart.goety.ILevelWand;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(DarkWand.class)
public class DarkWandMixin implements ILevelWand {
    @Shadow(remap = false) public SpellType spellType;

    @ModifyVariable(remap = false, method = "SoulUse", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private LivingEntity SoulUse2(LivingEntity value) {
        if (value instanceof FakeSpellerEntity spellerEntity && spellerEntity.getOwner() != null)
            value = spellerEntity.getOwner();
        return value;
    }
    @Inject(remap = false, method = "SoulUse", at = @At("RETURN"), cancellable = true)
    private void SoulUse(LivingEntity entityLiving, ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (ATAHelper2.hasOdamane(entityLiving))
            cir.setReturnValue(Math.min(1, cir.getReturnValue()));
    }

    /*
    @Redirect(method = "setSpellConditions", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/api/magic/ISpell;castDuration(Lnet/minecraft/world/entity/LivingEntity;)I"), remap = false)
    private int duration(ISpell instance, LivingEntity caster) {
        return EventUtil.castDuration(instance.castDuration(caster), instance, caster);
    }
     */
    @Inject(method = "appendHoverText", at = @At("HEAD"))
    private void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        tooltip.add(Component.translatable("tooltip.goety_revelation.wand_type").append(spellType.getName()));
        tooltip.add(Component.translatable("tooltip.goety_revelation.wand_level").append(LoreHelper.getStaffLevelDesc(this, stack)));
    }
    @Inject(method = "onUseTick", at = @At("HEAD"), cancellable = true)
    private void onUseTick(Level worldIn, LivingEntity livingEntityIn, ItemStack stack, int count, CallbackInfo ci) {
        if (livingEntityIn instanceof FakeSpellerEntity)
            ci.cancel();
    }
    @Override
    public boolean expandedRightStaffLogic(Spell spell, ItemStack stack) {
        return spell.getSpellType() == this.spellType;
    }

    @Override
    public boolean expandedTypeStaffLogic(SpellType spellType, ItemStack stack) {
        return spellType == this.spellType;
    }

    @Override
    public int getStaffLevel() {
        if(this.equals(ModItems.DARK_WAND.get()))
            return 0;
        if ((Object)this instanceof NamelessStaff)
            return 2;
        return 1;
    }
}
