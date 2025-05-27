package com.mega.revelationfix.common.item.other;

import com.mega.revelationfix.common.compat.SafeClass;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public class ApocalyptiumIngotItem extends Item {
    @Nullable
    private String descriptionId;

    public ApocalyptiumIngotItem(Properties properties) {
        super(properties);
    }

    @Override
    public boolean canBeHurtBy(DamageSource damageSource) {
        if (!damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.canBeHurtBy(damageSource);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected @NotNull String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("item", BuiltInRegistries.ITEM.getKey(this)) + (SafeClass.yearDay() == 41 ? ".fool" : "");
        }

        return this.descriptionId;
    }
}
