package com.mega.revelationfix.mixin.gr;

import com.mega.revelationfix.safe.GRSavedDataEC;
import com.mega.revelationfix.safe.GRSavedDataExpandedContext;
import net.minecraft.nbt.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;

@Mixin(DefeatApollyonInNetherState.class)
public class DefeatApollyonInNetherStateMixin implements GRSavedDataEC {
    @Unique
    private GRSavedDataExpandedContext context;

    @Inject(method = "createNbt", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;getBoolean(Ljava/lang/String;)Z", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private static void createNbt(CompoundTag nbt, CallbackInfoReturnable<DefeatApollyonInNetherState> cir, DefeatApollyonInNetherState state) {
        ((GRSavedDataEC) state).revelationfix$dataEC().read(nbt);
    }

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(CallbackInfo ci) {
        this.revelationfix$setEC(new GRSavedDataExpandedContext((DefeatApollyonInNetherState) (Object) this));
    }

    @Inject(method = "save", at = @At(value = "INVOKE", target = "Lnet/minecraft/nbt/CompoundTag;putBoolean(Ljava/lang/String;Z)V"))
    private void save(CompoundTag p_77763_, CallbackInfoReturnable<CompoundTag> cir) {
        this.revelationfix$dataEC().save(p_77763_);
    }

    @Override
    public GRSavedDataExpandedContext revelationfix$dataEC() {
        return context;
    }

    @Override
    public void revelationfix$setEC(GRSavedDataExpandedContext data) {
        context = data;
    }
}
