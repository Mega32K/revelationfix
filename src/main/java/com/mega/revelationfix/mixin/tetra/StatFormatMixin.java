package com.mega.revelationfix.mixin.tetra;

import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import org.spongepowered.asm.mixin.Mixin;
import se.mickelus.tetra.gui.stats.getter.StatFormat;

@Mixin(StatFormat.class)
@ModDependsMixin("tetra")
public class StatFormatMixin {
}
