package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.entity.armor.ArmorUtils;
import com.mega.revelationfix.common.item.armor.ModArmorMaterials;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SculkShriekerBlockEntity.class)
public class SculkShriekerBlockEntityMixin {
    @Inject(method = "tryShriek", at = @At("HEAD"), cancellable = true)
    private void tryShriek(ServerLevel p_222842_, ServerPlayer player, CallbackInfo ci) {
        if (player != null) {
            if (ArmorUtils.findBoots(player, ModArmorMaterials.SPECTRE) || ArmorUtils.findBoots(player, ModArmorMaterials.SPECTRE_DARKMAGE))
                ci.cancel();
        }
    }
}
