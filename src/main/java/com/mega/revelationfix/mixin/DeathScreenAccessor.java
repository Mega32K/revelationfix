package com.mega.revelationfix.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(DeathScreen.class)
public interface DeathScreenAccessor {
    @Accessor("exitButtons")
    List<Button> exitButtons();

    @Accessor("exitToTitleButton")
    Button exitToTitleButton();
}
