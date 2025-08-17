package com.mega.revelationfix.common.item.curios.robe;

import com.Polarice3.Goety.common.items.curios.UnholyRobeItem;
import com.mega.endinglib.api.client.text.TextColorUtils;
import com.mega.endinglib.util.SafeClass;
import com.mega.revelationfix.common.apollyon.common.RevelationRarity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import org.jetbrains.annotations.NotNull;

public class ApollyonRobeItem extends UnholyRobeItem {
    @Override
    public @NotNull Rarity getRarity(@NotNull ItemStack p_41461_) {
        return RevelationRarity.REVELATION;
    }

    @Override
    public @NotNull Component getName(@NotNull ItemStack p_41458_) {
        Component component = super.getName(p_41458_);
        if (!SafeClass.isModernUILoaded()) {
            if (component instanceof MutableComponent mc)
                return mc.withStyle(TextColorUtils.MIDDLE);
        }
        return component;
    }
}
