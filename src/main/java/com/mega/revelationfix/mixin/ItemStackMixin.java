package com.mega.revelationfix.mixin;

import com.mega.revelationfix.api.item.IDamageLimitItem;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.armor.ApocalyptiumArmor;
import com.mega.revelationfix.common.item.other.MysteryFragment;
import com.mega.revelationfix.common.item.other.RandomDisplayItem;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Shadow
    public abstract boolean hasTag();

    @Shadow
    @Nullable
    public abstract CompoundTag getTag();

    @Shadow
    public abstract Item getItem();

    @Shadow
    public abstract boolean is(TagKey<Item> p_204118_);

    @Inject(method = "is(Lnet/minecraft/tags/TagKey;)Z", at = @At("HEAD"), cancellable = true)
    private void is(TagKey<Item> p_204118_, CallbackInfoReturnable<Boolean> cir) {
        if (p_204118_ == GRItems.VANISHING_CB) {
            if (this.getItem() instanceof EnchantedBookItem bookItem) {
                cir.setReturnValue(bookItem.getEnchantmentLevel((ItemStack) (Object) this, Enchantments.VANISHING_CURSE) > 0);
            }
        }
        if (p_204118_ == GRItems.THE_END_CRAFTING) {
            cir.setReturnValue((this.hasTag() && this.getTag().getBoolean(GRItems.NBT_CRAFTING) && this.getItem() == TheEndRitualItemContext.THE_END_CRAFT) || this.getItem() instanceof RandomDisplayItem);
        } else if (p_204118_ == GRItems.THE_END_PUZZLES) {
            cir.setReturnValue((this.hasTag() && this.getTag().getBoolean(GRItems.NBT_PUZZLES)) || this.getItem() == TheEndRitualItemContext.PUZZLE1);
        } else if (p_204118_ == GRItems.THE_END_PUZZLES2) {
            cir.setReturnValue((this.hasTag() && this.getTag().getBoolean(GRItems.NBT_PUZZLES2)) || this.getItem() == TheEndRitualItemContext.PUZZLE2);
        } else if (p_204118_ == GRItems.THE_END_PUZZLES3) {
            cir.setReturnValue((this.hasTag() && this.getTag().getBoolean(GRItems.NBT_PUZZLES3)) || this.getItem() == TheEndRitualItemContext.PUZZLE3);
        } else if (p_204118_ == GRItems.THE_END_PUZZLES4) {
            cir.setReturnValue((this.hasTag() && this.getTag().getBoolean(GRItems.NBT_PUZZLES4)) || this.getItem() == TheEndRitualItemContext.PUZZLE4);
        } else if (this.getItem() instanceof MysteryFragment) {
            if (p_204118_ == GRItems.MYSTERY_0) {
                cir.setReturnValue(this.hasTag() && this.getTag().getInt("fragment") == 0);
            } else if (p_204118_ == GRItems.MYSTERY_1) {
                cir.setReturnValue(this.hasTag() && this.getTag().getInt("fragment") == 1);
            } else if (p_204118_ == GRItems.MYSTERY_2) {
                cir.setReturnValue(this.hasTag() && this.getTag().getInt("fragment") == 2);
            } else if (p_204118_ == GRItems.MYSTERY_3) {
                cir.setReturnValue(this.hasTag() && this.getTag().getInt("fragment") == 3);
            }
        }
    }

    @ModifyVariable(method = "hurt", at = @At("HEAD"), argsOnly = true)
    private int maxHurtDamage(int orginal) {
        if (this.getItem() instanceof IDamageLimitItem item)
            return Math.min(item.getUseDamageLimit((ItemStack) (Object)this), orginal);
        return orginal;
    }
}
