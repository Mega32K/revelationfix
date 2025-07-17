package com.mega.revelationfix.common.item;

import com.mega.revelationfix.client.font.OdamaneFont;
import net.minecraft.client.gui.Font;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class FontItemExtensions implements IClientItemExtensions {
    @Override
    public @Nullable Font getFont(ItemStack stack, FontContext context) {
        return IClientItemExtensions.super.getFont(stack, context);
    }
}
