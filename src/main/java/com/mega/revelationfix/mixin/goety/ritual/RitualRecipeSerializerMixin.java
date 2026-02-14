package com.mega.revelationfix.mixin.goety.ritual;

import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.mega.endinglib.api.data.CompoundTagUtils;
import com.mega.revelationfix.safe.RitualRecipeInterface;
import it.unimi.dsi.fastutil.Pair;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

@Mixin(RitualRecipe.Serializer.class)
public class RitualRecipeSerializerMixin {
    @Unique
    private static Pair<Boolean, ItemStack> hasRemaining(@Nullable JsonElement element) {
        if (element instanceof JsonPrimitive primitive)
            return Pair.of(primitive.getAsBoolean(), null);
        else if (element instanceof JsonObject jsonObject) {
            return Pair.of(true, CraftingHelper.getItemStack(jsonObject, true, true));
        } else return Pair.of(false, null);
    }
    @Inject(remap = false, method = "fromJson(Lnet/minecraft/resources/ResourceLocation;Lcom/google/gson/JsonObject;)Lcom/Polarice3/Goety/common/crafting/RitualRecipe;", at = @At("RETURN"))
    private void injectField(ResourceLocation recipeId, JsonObject json, CallbackInfoReturnable<RitualRecipe> cir) {
        RitualRecipe recipe = cir.getReturnValue();
        if (recipe == null) return;
        JsonObject jo = GsonHelper.getAsJsonObject(json, "result");
        boolean keepingNbt = GsonHelper.getAsBoolean(jo, "keepingNBT", false);
        RitualRecipeInterface rItf = RitualRecipeInterface.of(recipe);
        rItf.revelationfix$setKeepingNBT(keepingNbt);
        JsonArray ingredients = GsonHelper.getAsJsonArray(json, "ingredients");
        Set<Integer> remainingIndexes = rItf.inputsToRemaining();
        for (int i=0;i<ingredients.size();i++) {
            Pair<Boolean, ItemStack> pair;
            if ((pair = hasRemaining(ingredients.get(i).getAsJsonObject().get("remainingInRitual"))).left()) {
                remainingIndexes.add(i);
                ItemStack stack = pair.right();
                if (stack != null)
                    rItf.remaining().put(i, stack);
            }
        }
    }

    @Inject(remap = false, method = "fromNetwork(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/network/FriendlyByteBuf;)Lcom/Polarice3/Goety/common/crafting/RitualRecipe;", at = @At("TAIL"))
    private void injectRead(ResourceLocation recipeId, FriendlyByteBuf buffer, CallbackInfoReturnable<RitualRecipe> cir) {
        RitualRecipe recipe = cir.getReturnValue();
        if (recipe == null) return;
        RitualRecipeInterface rItf = RitualRecipeInterface.of(recipe);
        boolean keepingNbt = buffer.readBoolean();
        rItf.revelationfix$setKeepingNBT(keepingNbt);
        int flag = buffer.readShort();
        if (CompoundTagUtils.getIntFlag(flag, 1)) {
            rItf.inputsToRemaining().clear();
            rItf.inputsToRemaining().addAll(buffer.readList(FriendlyByteBuf::readInt));
        }
        if (CompoundTagUtils.getIntFlag(flag, 2)) {
            rItf.remaining().clear();
            rItf.remaining().putAll(buffer.readMap(FriendlyByteBuf::readInt, FriendlyByteBuf::readItem));
        }
    }

    @Inject(remap = false, method = "toNetwork(Lnet/minecraft/network/FriendlyByteBuf;Lcom/Polarice3/Goety/common/crafting/RitualRecipe;)V", at = @At("TAIL"))
    private void injectWrite(FriendlyByteBuf buffer, RitualRecipe recipe, CallbackInfo ci) {
        RitualRecipeInterface rItf = RitualRecipeInterface.of(recipe);
        int flag = 0;
        AtomicInteger f = new AtomicInteger(0);
        boolean hasNonEmptyRemaining = rItf.remaining().values().stream().anyMatch(itemStack -> !itemStack.isEmpty());
        if (!rItf.inputsToRemaining().isEmpty())
            CompoundTagUtils.setIntFlags(f::set, flag, 1, true);
        if (hasNonEmptyRemaining)
            CompoundTagUtils.setIntFlags(f::set, flag, 2, true);
        buffer.writeBoolean(rItf.revelationfix$isKeepingNbt());
        buffer.writeShort(f.get());
        if (!rItf.inputsToRemaining().isEmpty())
            buffer.writeCollection(rItf.inputsToRemaining(), FriendlyByteBuf::writeInt);
        if (hasNonEmptyRemaining)
            buffer.writeMap(rItf.remaining(), (FriendlyByteBuf::writeInt), ((byteBuf, itemStack) -> byteBuf.writeItemStack(itemStack, false)));
    }
}
