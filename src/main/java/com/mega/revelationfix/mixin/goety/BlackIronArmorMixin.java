package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.items.armor.BlackIronArmor;
import com.mega.endinglib.api.client.cmc.LoreHelper;
import com.mega.revelationfix.api.item.armor.IGoetyDamageResistanceArmor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(BlackIronArmor.class)
public class BlackIronArmorMixin extends ArmorItem implements IGoetyDamageResistanceArmor {
    BlackIronArmorMixin(ArmorMaterial p_40386_, Type p_266831_, Properties p_40388_) {
        super(p_40386_, p_266831_, p_40388_);
    }

    @Inject(method = "appendHoverText", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", shift = At.Shift.BEFORE))
    private void appendOtherTooltip(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn, CallbackInfo ci) {
        if (LoreHelper.hasShiftDown()) {
            tooltip.add(Component.translatable("tooltip.endinglib.specialEffect"));
            tooltip.addAll(this.damageResistanceTooltip(this, stack));
        } else tooltip.add(Component.translatable("tooltip.endinglib.holdShiftEffect"));
    }
}
