package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.client.inventory.container.DarkAnvilMenu;
import com.mega.endinglib.util.annotation.NoModDependsMixin;
import com.mega.revelationfix.common.config.BlockConfig;
import com.mega.revelationfix.common.config.CommonConfig;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DarkAnvilMenu.class)
@NoModDependsMixin("goety_addition")
public class GoetyDarkAnvilMixin {
    @Redirect(method = {"createResult"}, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getMaxLevel()I", ordinal = 0))
    private int redirectMaxLevel(Enchantment instance) {
        return instance.getMaxLevel() + BlockConfig.darkAnvilLimitLevel;
    }
}
