package com.mega.revelationfix.mixin.tetra;

import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.safe.mixinpart.tetra.GuiElementEC;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import se.mickelus.mutil.gui.GuiElement;

@Mixin(value = GuiElement.class, remap = false)
@ModDependsMixin("tetra")
public class GuiElementMixin implements GuiElementEC {
    @Unique
    private GuiElement tetraClip$parent;

    @Override
    public GuiElement tetraClip$parent() {
        return tetraClip$parent;
    }

    @Override
    public void tetraClip$setParent(GuiElement element) {
        this.tetraClip$parent = element;
    }

    @Inject(method = "addChild", at = @At("HEAD"))
    private void addChild(GuiElement child, CallbackInfo ci) {
        ((GuiElementEC) child).tetraClip$setParent((GuiElement) (Object) this);
    }
}
