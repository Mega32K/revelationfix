package com.mega.revelationfix.mixin.patchouli;

import com.google.gson.JsonElement;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.mega.revelationfix.common.compat.patchouli.PatchouliBrewEntriesInjector;
import com.mega.revelationfix.safe.mixinpart.ModDependsMixin;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.patchouli.client.book.BookContentLoader;
import vazkii.patchouli.client.book.BookContentResourceListenerLoader;
import vazkii.patchouli.common.book.Book;

@Mixin(BookContentResourceListenerLoader.class)
@ModDependsMixin("patchouli")
public class BookContentResourceListenerLoaderMixin {
    @Inject(method = "loadJson", at = @At("HEAD"), remap = false)
    private void modifyGoetyBrewEntries(Book book, ResourceLocation file, CallbackInfoReturnable<BookContentLoader.LoadResult> cir, @Share(value = "fileIndex", namespace = "goety_revelation") LocalIntRef fileIndexRef) {
        int index = PatchouliBrewEntriesInjector.getCategoryIndex(file);
        fileIndexRef.set(index);
    }
    @ModifyArg(method = "loadJson", at = @At(value = "INVOKE", target = "Lvazkii/patchouli/client/book/BookContentLoader$LoadResult;<init>(Lcom/google/gson/JsonElement;Ljava/lang/String;)V", remap = false), index = 0, remap = false)
    private JsonElement modifyArg0(JsonElement element, @Share(value = "fileIndex", namespace = "goety_revelation") LocalIntRef fileIndexRef) {
        if (fileIndexRef.get() > -1)
            return PatchouliBrewEntriesInjector.modifyInvokers[fileIndexRef.get()].get(element);
        return element;
    }
}
