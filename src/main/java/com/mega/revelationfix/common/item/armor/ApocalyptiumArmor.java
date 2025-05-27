package com.mega.revelationfix.common.item.armor;

import com.Polarice3.Goety.api.items.ISoulRepair;
import com.Polarice3.Goety.api.items.armor.ISoulDiscount;
import com.mega.revelationfix.common.item.IInvulnerableItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import z1gned.goetyrevelation.ModMain;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class ApocalyptiumArmor extends ArmorItem implements ISoulRepair, ISoulDiscount, IInvulnerableItem {
    public ApocalyptiumArmor(ArmorItem.Type p_40387_) {
        super(ModArmorMaterials.APOCALYPTIUM, p_40387_, new Properties().fireResistant().rarity(Rarity.UNCOMMON));
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String layer) {
        if (slot == EquipmentSlot.LEGS) {
            return ModMain.MODID + ":textures/models/armor/apocalyptium_armor_layer_1.png";
        } else {
            return ModMain.MODID + ":textures/models/armor/apocalyptium_armor_layer_0.png";
        }
    }

    public int getSoulDiscount(EquipmentSlot equipmentSlot) {
        return 16;
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        /*
        consumer.accept(new IClientItemExtensions() {
            public @NotNull HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original) {
                EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
                ModelPart root = modelSet.bakeLayer(equipmentSlot == EquipmentSlot.LEGS ? ModModelLayer.DARK_ARMOR_INNER : ModModelLayer.DARK_ARMOR_OUTER);
                DarkArmorModel model = new DarkArmorModel(root).animate(livingEntity);
                model.hat.visible = equipmentSlot == EquipmentSlot.HEAD;
                model.body.visible = equipmentSlot == EquipmentSlot.CHEST;
                model.rightArm.visible = equipmentSlot == EquipmentSlot.CHEST;
                model.leftArm.visible = equipmentSlot == EquipmentSlot.CHEST;
                model.bottom.visible = equipmentSlot == EquipmentSlot.LEGS;
                model.rightLeg.visible = equipmentSlot == EquipmentSlot.FEET;
                model.leftLeg.visible = equipmentSlot == EquipmentSlot.FEET;

                if (livingEntity instanceof AbstractClientPlayer player){
                    if (player.isCapeLoaded() && player.isModelPartShown(PlayerModelPart.CAPE) && player.getCloakTextureLocation() != null){
                        model.cape.visible = false;
                    }
                }

                model.young = original.young;
                model.crouching = original.crouching;
                model.riding = original.riding;
                model.rightArmPose = original.rightArmPose;
                model.leftArmPose = original.leftArmPose;

                return model;
            }
        });
         */
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        tooltip.add(this.soulDiscountTooltip(stack));
    }

}