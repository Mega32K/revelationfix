package com.mega.revelationfix.mixin.fantasy_ending;

import com.mega.uom.item.egg.UomSpawnEgg;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.ForgeSpawnEggItem;
import org.spongepowered.asm.mixin.Mixin;

import java.util.function.Supplier;

@Mixin(UomSpawnEgg.class)
public abstract class UomSpawnEggMixin extends ForgeSpawnEggItem {
    UomSpawnEggMixin(Supplier<? extends EntityType<? extends Mob>> type, int backgroundColor, int highlightColor, Properties props) {
        super(type, backgroundColor, highlightColor, props);
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemStack) {
        return new ItemStack(Items.DRAGON_EGG);
    }
}
