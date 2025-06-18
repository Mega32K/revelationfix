package com.mega.revelationfix.client.font.effect;

import com.mega.revelationfix.util.java.ExeCallable;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public enum LoreStyle {
    NONE(mutableComponent -> mutableComponent),
    ATTRIBUTE_PREFIX(mutableComponent -> Component.literal("§5- ").append(mutableComponent)),
    INDENTATION(mutableComponent -> Component.literal("  ").append(mutableComponent)),
    INDENTATION2(mutableComponent -> Component.literal("    ").append(mutableComponent));
    private final ExeCallable<MutableComponent> delegate;

    LoreStyle(ExeCallable<MutableComponent> delegate) {
        this.delegate = delegate;
    }

    public ExeCallable<MutableComponent> getDelegate() {
        return this.delegate;
    }
}
