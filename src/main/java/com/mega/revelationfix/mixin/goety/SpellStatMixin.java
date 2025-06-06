package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.magic.SpellStat;
import com.mega.revelationfix.safe.mixinpart.goety.SpellStatEC;
import net.minecraft.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Arrays;

@Mixin(SpellStat.class)
@SuppressWarnings("AddedMixinMembersNamePattern")
public class SpellStatMixin implements SpellStatEC {
    @Unique
    private final boolean[] tag = Util.make(() -> {
        boolean[] array = new boolean[128];
        for (int i=0;i<128;i++) {
            array[i] = false;
        }
        return array;
    });
    @Override
    public boolean isModifiedByAttributes() {
        return tag[0];
    }

    @Override
    public boolean isModifiedByRuneReactor() {
        return tag[1];
    }

    @Override
    public void giveModifiedTag(byte tag, boolean z) {
        this.tag[tag] = z;
    }
}
