package com.mega.revelationfix.mixin.goety.expand;

import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.items.curios.WayfarersBeltItem;
import com.google.common.collect.Multimap;
import com.mega.revelationfix.common.item.curios.OdamaneHalo;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;

import java.util.UUID;

@Mixin(WayfarersBeltItem.class)
public abstract class WayfarersBeltItemMixin {
    @Inject(remap = false, method = "getAttributeModifiers", at = @At(value = "INVOKE", target = "Lcom/google/common/collect/Multimap;put(Ljava/lang/Object;Ljava/lang/Object;)Z", shift = At.Shift.AFTER, ordinal = 2, remap = false), locals = LocalCapture.CAPTURE_FAILHARD)
    private void getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack, CallbackInfoReturnable<Multimap<Attribute, AttributeModifier>> cir, Multimap<Attribute, AttributeModifier> map) {
        CuriosApi.addSlotModifier(map, "charm",
                UUID.fromString("bf084d19-7ea7-479a-8e19-6edf2d90cd6e"), 1.0D, AttributeModifier.Operation.ADDITION);
    }
}
