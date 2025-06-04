package com.mega.revelationfix.mixin.tetra;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.config.ItemConfig;
import com.Polarice3.Goety.utils.SEHelper;
import com.mega.revelationfix.common.compat.tetra.effect.SoulRepairEffect;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.ModularItem;

@Mixin(ModularItem.class)
@ModDependsMixin("tetra")
public abstract class ModularItemMixin implements ISoulRepair, IModularItem {
    @Override
    public void repairTick(ItemStack stack, Entity entityIn, boolean isSelected) {
        int level = this.getEffectLevel(stack, SoulRepairEffect.itemEffect);
        if (level <= 0)
            return;
        revelationfix$repairTick0(stack, entityIn, isSelected, level);
    }

    @Unique
    void revelationfix$repairTick0(ItemStack stack, Entity entityIn, boolean isSelected, int damage) {
        if (ItemConfig.SoulRepair.get() && entityIn instanceof Player player) {
            if ((!player.swinging || !isSelected) && stack.isDamaged() && SEHelper.getSoulsContainer(player)) {
                int i = 1;
                if (!stack.getAllEnchantments().isEmpty() && ItemConfig.SoulRepairEnchant.get()) {
                    i += stack.getAllEnchantments().size();
                }

                if (SEHelper.getSoulsAmount(player, ItemConfig.ItemsRepairAmount.get() * i) && player.tickCount % 20 == 0) {
                    stack.setDamageValue(Math.max(0, stack.getDamageValue() - damage));
                    SEHelper.decreaseSouls(player, ItemConfig.ItemsRepairAmount.get() * i);
                }
            }
        }

    }
}
