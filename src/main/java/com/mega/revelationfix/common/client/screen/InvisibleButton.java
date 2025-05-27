package com.mega.revelationfix.common.client.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;

public class InvisibleButton extends Button {
    public InvisibleButton(int p_259075_, int p_259271_, int p_260232_, int p_260028_, Component p_259351_, OnPress p_260152_, CreateNarration p_259552_) {
        super(p_259075_, p_259271_, p_260232_, p_260028_, p_259351_, p_260152_, p_259552_);
    }

    public InvisibleButton(InvisibleBuilder builder) {
        this(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, builder.createNarration);
    }

    @Override
    public void render(GuiGraphics p_282421_, int p_93658_, int p_93659_, float p_93660_) {
        super.render(p_282421_, p_93658_, p_93659_, p_93660_);
    }

    @OnlyIn(Dist.CLIENT)
    public static class InvisibleBuilder extends Button.Builder {
        private final Component message;
        private final Button.OnPress onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private Button.CreateNarration createNarration = Button.DEFAULT_NARRATION;

        public InvisibleBuilder(Component p_254097_, Button.OnPress p_253761_) {
            super(p_254097_, p_253761_);
            this.message = p_254097_;
            this.onPress = p_253761_;
        }

        public InvisibleBuilder pos(int p_254538_, int p_254216_) {
            this.x = p_254538_;
            this.y = p_254216_;
            return this;
        }

        public InvisibleBuilder width(int p_254259_) {
            this.width = p_254259_;
            return this;
        }

        public InvisibleBuilder size(int p_253727_, int p_254457_) {
            this.width = p_253727_;
            this.height = p_254457_;
            return this;
        }

        public InvisibleBuilder bounds(int p_254166_, int p_253872_, int p_254522_, int p_253985_) {
            return this.pos(p_254166_, p_253872_).size(p_254522_, p_253985_);
        }

        public InvisibleBuilder tooltip(@Nullable Tooltip p_259609_) {
            this.tooltip = p_259609_;
            return this;
        }

        public InvisibleBuilder createNarration(Button.CreateNarration p_253638_) {
            this.createNarration = p_253638_;
            return this;
        }

        public InvisibleButton build() {
            return build0(InvisibleButton::new);
        }

        public InvisibleButton build0(java.util.function.Function<InvisibleBuilder, InvisibleButton> builder) {
            return builder.apply(this);
        }
    }
}
