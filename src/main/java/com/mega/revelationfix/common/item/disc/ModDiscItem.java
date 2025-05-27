package com.mega.revelationfix.common.item.disc;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.RecordItem;

import java.util.function.Supplier;

public class ModDiscItem extends RecordItem {
    public ModDiscItem(int comparatorValue, Supplier<SoundEvent> soundSupplier, Properties builder, int lengthInTicks) {
        super(comparatorValue, soundSupplier, builder, lengthInTicks);
    }

}
