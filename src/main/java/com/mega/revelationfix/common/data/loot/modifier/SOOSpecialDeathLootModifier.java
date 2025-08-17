package com.mega.revelationfix.common.data.loot.modifier;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.items.ModItems;
import com.google.common.base.Suppliers;
import com.mega.revelationfix.common.config.ItemConfig;
import com.mega.revelationfix.util.entity.ATAHelper2;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

public class SOOSpecialDeathLootModifier implements IGlobalLootModifier {
    public static final Supplier<Codec<SOOSpecialDeathLootModifier>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(
                    inst -> inst.group(Codec.intRange(0, 8).fieldOf("flag").forGetter((lm) -> lm.flag))
                            .apply(inst, SOOSpecialDeathLootModifier::new)
            ));
    private final int flag;

    public SOOSpecialDeathLootModifier(int flag) {
        this.flag = flag;
    }

    public static SpecialEntityType isSpecialDeathLootEntity(Entity entity) {
        if (entity == null) return SpecialEntityType.NONE;
        EntityType<?> entityType = entity.getType();
        if (entityType == EntityType.WITHER_SKELETON)
            return SpecialEntityType.WITHER_SKELETON;
        else if (entityType == ModEntityType.HERETIC.get())
            return SpecialEntityType.HERETIC;
        else if (entityType == ModEntityType.WARLOCK.get())
            return SpecialEntityType.WARLOCK;
        else if (entityType == EntityType.WITCH)
            return SpecialEntityType.WITCH;
        else return SpecialEntityType.NONE;
    }

    @Override
    public @NotNull ObjectArrayList<ItemStack> apply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        Entity killer = context.getParamOrNull(LootContextParams.KILLER_ENTITY);
        SpecialEntityType specialEntityType = isSpecialDeathLootEntity(entity);
        if (specialEntityType != SpecialEntityType.NONE) {
            if (killer instanceof Player player && ATAHelper2.hasSoulOfObsidian(player)) {
                ItemStack itemStack = specialEntityType.getSpecialLootItem();
                boolean has = false;
                for (ItemStack stack : generatedLoot) {
                    if (stack.getItem() == itemStack.getItem()) {
                        has = true;
                        break;
                    }
                }
                if (!has) {
                    if (context.getRandom().nextFloat() <= ItemConfig.soulOfObsidianSpecialLootIncrease * 0.01F)
                        generatedLoot.add(itemStack);
                }
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }

    public enum SpecialEntityType {
        NONE(Items.AIR::getDefaultInstance),
        WITHER_SKELETON(() -> new ItemStack(Items.WITHER_SKELETON_SKULL)),
        HERETIC(() -> ModItems.INFERNAL_TOME.get().getDefaultInstance()),
        WARLOCK(() -> ModItems.WARLOCK_SASH.get().getDefaultInstance()),
        WITCH(() -> ModItems.WITCH_HAT.get().getDefaultInstance());
        private final Supplier<ItemStack> specialLootItem;

        SpecialEntityType(Supplier<ItemStack> specialLootItem) {
            this.specialLootItem = specialLootItem;
        }

        public ItemStack getSpecialLootItem() {
            return specialLootItem.get();
        }
    }
}
