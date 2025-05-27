package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.mixin.ItemAccessor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.item.ModFocusItem;

@Mixin(ModFocusItem.class)
public abstract class ModFocusItemMixin extends Item {
    public ModFocusItemMixin(Properties p_41383_) {
        super(p_41383_);
    }

    @Redirect(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/Item$Properties;rarity(Lnet/minecraft/world/item/Rarity;)Lnet/minecraft/world/item/Item$Properties;"))
    private static Item.Properties init(Item.Properties instance, Rarity p_41498_) {
        return instance.rarity(RevelationRarity.REVELATION).setNoRepair().durability(7).fireResistant();
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        int srcDamage = stack.getOrCreateTag().getInt("Damage");
        damage = Math.max(damage, srcDamage);
        super.setDamage(stack, damage);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        //setDamage(itemStack, getDamage(itemStack)+1);
        ItemStack stack = itemStack.copy();
        setDamage(stack, getDamage(stack) + 1);
        if (getDamage(stack) >= 7)
            stack = ItemStack.EMPTY;
        return stack;
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        ((ItemAccessor) this).setRemaining(this);
    }

    @Override
    public boolean isBookEnchantable(ItemStack stack, ItemStack book) {
        return false;
    }

    @Override
    public boolean isEnchantable(ItemStack p_41456_) {
        return false;
    }

    @Override
    public boolean isFoil(ItemStack p_41453_) {
        return false;
    }
}
