package com.mega.revelationfix.common.event;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.event.RenderTooltipEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RenderTooltipPostEvent extends RenderTooltipEvent {
    public RenderTooltipPostEvent(@NotNull ItemStack itemStack, GuiGraphics graphics, int x, int y) {
        super(itemStack, graphics, x, y, Minecraft.getInstance().font, List.of());
    }
}
