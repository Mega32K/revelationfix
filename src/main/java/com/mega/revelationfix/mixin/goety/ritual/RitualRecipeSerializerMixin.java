package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.google.gson.JsonObject;
import com.mega.revelationfix.safe.RitualRecipeInterface;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RitualRecipe.Serializer.class)
public class RitualRecipeSerializerMixin {
    @Inject(remap = false, method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lcom/Polarice3/Goety/common/crafting/RitualRecipe;", at = @At("RETURN"))
    private void injectField(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<RitualRecipe> cir) {
        RitualRecipe recipe = cir.getReturnValue();
        if (recipe == null) return;
        JsonObject jo = GsonHelper.getAsJsonObject(json, "result");
        boolean keepingNbt = GsonHelper.getAsBoolean(jo, "keepingNBT", false);
        ((RitualRecipeInterface) recipe).revelationfix$setKeepingNBT(keepingNbt);
    }

    @Inject(remap = false, method = "fromNetwork(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/network/FriendlyByteBuf;)Lcom/Polarice3/Goety/common/crafting/RitualRecipe;", at = @At("TAIL"))
    private void injectRead(ResourceLocation recipeId, FriendlyByteBuf buffer, CallbackInfoReturnable<RitualRecipe> cir) {
        RitualRecipe recipe = cir.getReturnValue();
        if (recipe == null) return;
        boolean keepingNbt = buffer.readBoolean();
        ((RitualRecipeInterface) recipe).revelationfix$setKeepingNBT(keepingNbt);
    }

    @Inject(remap = false, method = "toNetwork(Lnet/minecraft/network/FriendlyByteBuf;Lcom/Polarice3/Goety/common/crafting/RitualRecipe;)V", at = @At("TAIL"))
    private void injectWrite(FriendlyByteBuf buffer, RitualRecipe recipe, CallbackInfo ci) {
        buffer.writeBoolean(((RitualRecipeInterface) recipe).revelationfix$isKeepingNbt());
    }
}
