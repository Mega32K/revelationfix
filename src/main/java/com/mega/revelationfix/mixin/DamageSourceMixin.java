package com.mega.revelationfix.mixin;

import com.mega.revelationfix.common.apollyon.common.ExtraDamageTypes;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.safe.DamageSourceInterface;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DamageSource.class)
public class DamageSourceMixin implements DamageSourceInterface {
    @Unique
    private final boolean[] tags = new boolean[128];
    @Unique
    private boolean revelationfix$bypassArmor = false;
    @Unique
    private boolean revelationfix$trueKill = false;
    @Unique
    private boolean revelationfix$fePower = false;
    @Unique
    private boolean revelationfix$bypassAll = false;

    @Override
    public void revelationfix$setBypassArmor(boolean z) {
        this.revelationfix$bypassArmor = z;
    }

    @Override
    public boolean revelationfix$bypassArmor() {
        return this.revelationfix$bypassArmor;
    }

    @Override
    public void revelationfix$trueKill(boolean z) {
        revelationfix$trueKill = z;
    }

    @Override
    public boolean revelationfix$trueKill() {
        return revelationfix$trueKill;
    }

    @Override
    public boolean revelationfix$fePower() {
        return revelationfix$fePower;
    }

    @Override
    public void revelationfix$setBypassAll(boolean z) {
        revelationfix$bypassAll = z;
    }

    @Override
    public boolean revelationfix$isBypassAll() {
        return revelationfix$bypassAll;
    }

    @Override
    public void giveSpecialTag(byte tag) {
        tags[tag] = true;
    }

    @Override
    public void cleanSpecialTag(byte tag) {
        tags[tag] = false;
    }

    @Override
    public boolean hasTag(byte tag) {
        return tags[tag];
    }

    @Override
    public void revelationfix$fePower(boolean z) {
        revelationfix$fePower = z;
    }

    @Inject(method = "is(Lnet/minecraft/tags/TagKey;)Z", at = @At("HEAD"), cancellable = true)
    private void is(TagKey<DamageType> tagKey, CallbackInfoReturnable<Boolean> cir) {
        if (tagKey == DamageTypeTags.BYPASSES_ARMOR)
            if (this.revelationfix$bypassArmor()) cir.setReturnValue(true);
        if (revelationfix$fePower() || revelationfix$isBypassAll()) {
            if (tagKey == DamageTypeTags.BYPASSES_ARMOR || tagKey == DamageTypeTags.BYPASSES_SHIELD || tagKey == DamageTypeTags.BYPASSES_INVULNERABILITY || tagKey == DamageTypeTags.BYPASSES_COOLDOWN || tagKey == DamageTypeTags.BYPASSES_RESISTANCE || tagKey == DamageTypeTags.BYPASSES_EFFECTS)
                cir.setReturnValue(true);
        }
    }

    @Inject(method = "is(Lnet/minecraft/resources/ResourceKey;)Z", at = @At("HEAD"), cancellable = true)
    private void is(ResourceKey<DamageType> p_276108_, CallbackInfoReturnable<Boolean> cir) {
        if (p_276108_ == ExtraDamageTypes.FE_POWER)
            if (this.revelationfix$fePower()) cir.setReturnValue(true);
    }

    @Inject(method = "getEntity", at = @At("RETURN"), cancellable = true)
    private void modifyToTrueOwner(CallbackInfoReturnable<Entity> cir) {
        if (cir.getReturnValue() instanceof FakeSpellerEntity fakeSpellerEntity) {
            Entity entity = fakeSpellerEntity.getOwner();
            if (entity != null)
                cir.setReturnValue(entity);
        }
    }
}
