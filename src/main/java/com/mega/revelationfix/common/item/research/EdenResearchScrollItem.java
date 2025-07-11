package com.mega.revelationfix.common.item.research;

import com.Polarice3.Goety.common.items.research.Scroll;
import com.mega.endinglib.api.item.IDragonLightRendererItem;
import com.mega.endinglib.api.item.IInvulnerableItem;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import com.mega.revelationfix.common.item.FontItemExtensions;
import com.mega.revelationfix.common.research.ModResearches;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

import java.util.function.Consumer;

public class EdenResearchScrollItem extends Scroll implements IInvulnerableItem, IDragonLightRendererItem {
    public EdenResearchScrollItem() {
        super(new Properties().fireResistant().rarity(RevelationRarity.EDEN_NAME).setNoRepair().fireResistant().stacksTo(1), ModResearches.EDEN);
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return itemStack.copy();
    }

    @Override
    public boolean enableDragonLightRenderer(ItemStack itemStack) {
        return true;
    }

    @Override
    public int dragonRendererStartColor(ItemStack stack) {
        return 0x907f8492;
    }

    @Override
    public int dragonRendererEndColor(ItemStack stack) {
        return 0x00b6b6b6;
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new FontItemExtensions());
    }
}
