package com.mega.revelationfix.mixin.tetra;

import com.mega.endinglib.util.annotation.ModDependsMixin;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiText;
import se.mickelus.tetra.blocks.scroll.gui.ScrollScreen;

@Mixin(value = ScrollScreen.class, remap = false)
@ModDependsMixin("tetra")
public interface ScrollScreenAccessor {
    @Accessor("currentPage")
    static int currentPage() {
        return 0;
    }

    @Accessor("currentPage")
    static void setCurrentPage(int page) {

    }

    @Accessor("pages")
    String[] pages();

    @Accessor("gui")
    GuiElement gui();

    @Accessor("text")
    GuiText text();

    @Accessor("pages")
    void setPages(String[] strings);

    @Accessor("gui")
    void setGui(GuiElement element);

    @Accessor("text")
    void setText(GuiText text);
}
