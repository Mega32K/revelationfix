package com.mega.revelationfix.common.data.ingrident;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class EnchantmentBookIngredient extends Ingredient {
    private final ResourceLocation enchantmentID;
    private final @Nullable Enchantment enchantment;
    private final @Nullable ItemStack stack;
    public EnchantmentBookIngredient(String enchantmentID) {
        super(Stream.of(new TagValue(GRItems.VANISHING_CB)));
        this.enchantmentID = new ResourceLocation(enchantmentID);
        this.enchantment = ForgeRegistries.ENCHANTMENTS.getValue(this.enchantmentID);
        stack = new ItemStack(Items.ENCHANTED_BOOK);
        if (this.enchantment != null)
            EnchantedBookItem.addEnchantment(stack, new EnchantmentInstance(this.enchantment, 1));
    }

    @Override
    public boolean test(@Nullable ItemStack itemStack) {
        if (itemStack == null) return false;
        return itemStack.getItem() instanceof EnchantedBookItem bookItem && EnchantmentHelper.getEnchantments(itemStack).containsKey(enchantment);
    }

    @Override
    public ItemStack @NotNull [] getItems() {
        if (stack == null) return new ItemStack[] {Items.AIR.getDefaultInstance()};
        return new ItemStack[] {stack};
    }

    public ResourceLocation getEnchantmentID() {
        return enchantmentID;
    }

    @Override
    public @NotNull IIngredientSerializer<? extends Ingredient> getSerializer() {
        return EnchantmentBookIngredientSerializer.INSTANCE;
    }
}
