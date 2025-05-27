package com.mega.revelationfix.common.item.other;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.List;

public class AtonementVoucher extends Item {
    public AtonementVoucher(Properties p_41383_) {
        super(p_41383_);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (!level.isClientSide) {
            player.getItemInHand(hand).shrink(1);
            for (Apostle apostle : level.getEntitiesOfClass(Apostle.class, new AABB(player.blockPosition).inflate(72D), (e) -> ((ApollyonAbilityHelper) e).allTitlesApostle_1_20_1$isApollyon())) {
                apostle.hurt(apostle.damageSources().fellOutOfWorld(), 1F);
                apostle.kill();
            }
        }
        return super.use(level, player, hand);
    }

    @Override
    public void appendHoverText(ItemStack p_41421_, @Nullable Level p_41422_, List<Component> p_41423_, TooltipFlag p_41424_) {
        p_41423_.add(Component.translatable("item.goety_revelation.atonement_voucher.desc"));
        super.appendHoverText(p_41421_, p_41422_, p_41423_, p_41424_);
    }
}
