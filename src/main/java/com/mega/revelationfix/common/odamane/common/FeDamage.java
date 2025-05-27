package com.mega.revelationfix.common.odamane.common;

import com.mega.revelationfix.common.apollyon.common.ExtraDamageTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class FeDamage extends DamageSource {
    boolean isMagic;

    public FeDamage(Holder<DamageType> p_270811_, @Nullable Entity p_270660_) {
        super(p_270811_, p_270660_);
    }

    public static FeDamage get(LivingEntity target, LivingEntity attacker) {
        return new FeDamage(target.level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ExtraDamageTypes.FE_POWER), attacker);
    }

    @Override
    public Component getLocalizedDeathMessage(LivingEntity living) {
        String s = "death.attack." + this.type().msgId().replace(".0", "") + "." + living.level.random.nextInt(0, 3);
        if (getEntity() != null)
            return Component.translatable(s, living.getDisplayName(), getEntity().getDisplayName());
        else if (getDirectEntity() != null)
            return Component.translatable(s, living.getDisplayName(), getDirectEntity().getDisplayName());
        return Component.empty();
    }

    @Override
    public boolean is(TagKey<DamageType> tagKey) {
        if (tagKey == DamageTypeTags.BYPASSES_ARMOR || tagKey == DamageTypeTags.BYPASSES_SHIELD || tagKey == DamageTypeTags.BYPASSES_INVULNERABILITY || tagKey == DamageTypeTags.BYPASSES_COOLDOWN || tagKey == DamageTypeTags.BYPASSES_RESISTANCE || tagKey == DamageTypeTags.BYPASSES_EFFECTS)
            return true;
        return super.is(tagKey);
    }

    @Override
    public boolean is(ResourceKey<DamageType> p_276108_) {
        if (p_276108_ == ExtraDamageTypes.FE_POWER)
            return true;
        if (p_276108_ == DamageTypes.MAGIC)
            if (isMagic) return true;
        return super.is(p_276108_);
    }

    public void setMagic(boolean magic) {
        isMagic = magic;
    }
}
