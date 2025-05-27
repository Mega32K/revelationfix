package com.mega.revelationfix.mixin.tetra;

import com.mega.revelationfix.safe.ModDependsMixin;
import com.mega.revelationfix.safe.mixinpart.tetra.ScrollScreenData;
import com.mega.revelationfix.safe.mixinpart.tetra.ScrollScreenEC;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiText;
import se.mickelus.tetra.blocks.scroll.gui.ScrollScreen;

@Mixin(ScrollScreen.class)
@ModDependsMixin("tetra")
public abstract class ScrollScreenMixin extends Screen implements ScrollScreenEC {
    @Shadow(remap = false)
    private static int currentPage;
    @Shadow(remap = false)
    @Final
    @Mutable
    private String[] pages;
    @Shadow(remap = false)
    @Final
    @Mutable
    private GuiElement gui;
    @Shadow(remap = false)
    @Final
    @Mutable
    private GuiText text;
    @Unique
    private final ScrollScreenData data = new ScrollScreenData((ScrollScreen) (Object) this);
    protected ScrollScreenMixin(Component p_96550_) {
        super(p_96550_);
    }

    @Shadow(remap = false)
    protected abstract void changePage(int index);

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(String key, CallbackInfo ci) {
        data.init(key);
    }

    @Override
    protected void init() {
        data.screenInit();
    }

    @Override
    public void revelationfix$changePage(int page) {
        data.changePage(page);
        this.changePage(page);
    }
}
