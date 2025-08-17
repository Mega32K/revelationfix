package com.mega.revelationfix.common.item.other;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CuriosStorageCrystal extends Item {
    public CuriosStorageCrystal(Properties p_41383_) {
        super(p_41383_);
    }

    public static void store(ItemStack stack, List<ItemStack> toStore, String owner) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putString("OwnerName", owner);
        ListTag listTag = new ListTag();
        if (tag.contains("BannedCurios", 9)) {
            listTag = stack.getTag().getList("BannedCurios", 10);
        }
        for (ItemStack itemstack : toStore) {
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                itemstack.save(compoundtag);
                listTag.add(compoundtag);
            }
        }
        tag.put("BannedCurios", listTag);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemStack, @Nullable Level level, @NotNull List<Component> list, @NotNull TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.goety_revelation.curios_storage_crystal.desc"));
        if (!Screen.hasAltDown()) {
            list.add(Component.translatable("tooltip.revelationfix.holdAlt"));
        } else if (itemStack.hasTag()) {
            CompoundTag tag = itemStack.getTag();
            if (tag.contains("OwnerName", 8)) {
                list.add(Component.translatable("tooltip.revelationfix.owner", Component.literal(tag.getString("OwnerName")).withStyle(ChatFormatting.GOLD)));
            }
            if (tag.contains("BannedCurios", 9)) {
                ListTag listtag = itemStack.getTag().getList("BannedCurios", 10);
                for (int i = 0; i < listtag.size(); ++i) {
                    if (i < 6) {
                        ItemStack stack = ItemStack.of(listtag.getCompound(i));
                        list.add(Component.literal(stack.getDisplayName().getString() + " x").withStyle(ChatFormatting.DARK_GRAY).append(Integer.toString(stack.getCount())).withStyle(ChatFormatting.ITALIC, ChatFormatting.DARK_PURPLE));
                    } else {
                        list.add(Component.translatable("tooltip.revelationfix.remaing", Component.literal(String.valueOf(listtag.size() - i)).withStyle(ChatFormatting.GOLD)));
                        break;
                    }
                }
            } else list.add(Component.literal("None.").withStyle(ChatFormatting.GRAY));
        } else list.add(Component.literal("None.").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(itemStack, level, list, tooltipFlag);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        ItemStack itemStack = context.getItemInHand();
        Player player = context.getPlayer();
        BlockPos p = context.getClickedPos();
        if (itemStack.hasTag() && itemStack.getTag() != null && player != null) {
            if (itemStack.getTag().contains("BannedCurios", 9)) {
                if (!context.getLevel().isClientSide) {
                    ListTag listtag = itemStack.getTag().getList("BannedCurios", 10);
                    if (player.isShiftKeyDown()) {
                        if (!listtag.isEmpty())
                            if (player instanceof ServerPlayer serverPlayer)
                                serverPlayer.playNotifySound(SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 4F, 1F + (player.getRandom().nextFloat() * 0.2F));
                        while (!listtag.isEmpty()) {
                            CompoundTag compoundTag = listtag.getCompound(listtag.size() - 1);
                            ItemStack stack = ItemStack.of(compoundTag);
                            ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack);
                            itementity.setDefaultPickUpDelay();
                            context.getLevel().addFreshEntity(itementity);
                            listtag.remove(compoundTag);
                            itemStack.getTag().put("BannedCurios", listtag);
                        }
                    } else {
                        if (!listtag.isEmpty() && !player.getCooldowns().isOnCooldown(itemStack.getItem())) {
                            CompoundTag compoundTag = listtag.getCompound(listtag.size() - 1);
                            ItemStack stack = ItemStack.of(compoundTag);
                            ItemEntity itementity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), stack);
                            itementity.setDefaultPickUpDelay();
                            context.getLevel().addFreshEntity(itementity);
                            listtag.remove(compoundTag);
                            itemStack.getTag().put("BannedCurios", listtag);
                            if (player instanceof ServerPlayer serverPlayer)
                                serverPlayer.playNotifySound(SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 4F, 1F + (player.getRandom().nextFloat() * 0.2F));
                        }
                    }
                    if (listtag.isEmpty() && !player.getAbilities().instabuild) {
                        itemStack.shrink(1);
                    }
                }
                player.getCooldowns().addCooldown(itemStack.getItem(), 5);
            }
        }

        return super.useOn(context);
    }

    @Override
    public boolean canBeHurtBy(DamageSource p_41387_) {
        if (!p_41387_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.canBeHurtBy(p_41387_);
    }

}
