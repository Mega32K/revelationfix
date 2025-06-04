package com.mega.revelationfix.client.screen;

import com.mega.revelationfix.mixin.DeathScreenAccessor;
import com.mega.revelationfix.mixin.ScreenAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.DeathScreen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;


public class TheEndDeathScreen extends DeathScreen {
    public InvisibleButton waitForTheEndButton;
    private final DeathScreenAccessor accessor;

    public TheEndDeathScreen(@Nullable Component p_95911_, boolean p_95912_) {
        super(p_95911_, p_95912_);
        accessor = (DeathScreenAccessor) this;
    }

    @Override
    protected void init() {
        super.init();
        this.waitForTheEndButton = this.addRenderableWidget(new InvisibleButton.InvisibleBuilder(Component.translatable("screen.goety_revelation.button.wait_for_the_end"), (p_280794_) -> {

            this.minecraft.setScreen(null);
            p_280794_.active = false;
        }).bounds(this.width / 2 - 100, this.height / 4 + 120, 200, 20).build());
        accessor.exitButtons().add(this.waitForTheEndButton);
        this.waitForTheEndButton.active = false;
    }

    public void render(GuiGraphics p_283488_, int p_283551_, int p_283002_, float p_281981_) {
        Minecraft mc = Minecraft.getInstance();
        for (Button button : accessor.exitButtons()) {
            if (button != waitForTheEndButton)
                button.active = false;
        }
        ((ScreenAccessor) this).setTitle(Component.translatable("deathScreen.title.hardcore"));
        super.render(p_283488_, p_283551_, p_283002_, p_281981_);
    }
}
