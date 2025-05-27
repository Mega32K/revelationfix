package com.mega.revelationfix.mixin.tetra;

import com.mega.revelationfix.safe.ModDependsMixin;
import com.mega.revelationfix.safe.mixinpart.tetra.HammerBaseEntityEC;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.blocks.forged.hammer.HammerBaseBlockEntity;

@Mixin(HammerBaseBlockEntity.class)
@ModDependsMixin("tetra")
public class HammerBaseBlockEntityMixin implements HammerBaseEntityEC {
    @Unique
    private int revelationfix$improvedTimes;

    @Override
    public boolean revelationfix$isDarkIngot() {
        return revelationfix$improvedTimes >= 7;
    }

    @Override
    public void revelationfix$setImproved(int currentImprovements) {
        revelationfix$improvedTimes = currentImprovements;
    }

    @Override
    public int revelationfix$getImproved() {
        return revelationfix$improvedTimes;
    }


    @Inject(method = "saveAdditional", at = @At("TAIL"))
    private void saveAdditional(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("darkIngotImproved", this.revelationfix$improvedTimes);
    }

    @Inject(method = "load", at = @At("TAIL"))
    private void load(CompoundTag compound, CallbackInfo ci) {
        this.revelationfix$improvedTimes = compound.getInt("darkIngotImproved");
    }

    @Inject(method = "getJamChance", remap = false, at = @At("HEAD"), cancellable = true)
    private void getJamChance(CallbackInfoReturnable<Float> cir) {
        if (this.revelationfix$isDarkIngot()) cir.setReturnValue(-1F);
    }
}
