package com.mega.revelationfix.common.item.research;

import com.Polarice3.Goety.common.items.research.Scroll;
import com.mega.endinglib.api.item.IInvulnerableItem;
import com.mega.revelationfix.common.research.ModResearches;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;

public class GodForgingResearchScrollItem extends Scroll implements IInvulnerableItem {
    public GodForgingResearchScrollItem() {
        super(new Properties().fireResistant().rarity(Rarity.UNCOMMON).fireResistant().stacksTo(1), ModResearches.GOD_FORGING);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(this);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }
}
