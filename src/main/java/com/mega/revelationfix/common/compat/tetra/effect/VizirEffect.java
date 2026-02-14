package com.mega.revelationfix.common.compat.tetra.effect;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.ally.illager.raider.AllyVex;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.common.compat.tetra.TetraVersionCompat;
import com.mega.revelationfix.safe.DamageSourceInterface;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatFormat;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import java.util.Map;

public class VizirEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.vizir");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.vizir.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.percentageLabel, TetraVersionCompat.createTGM("goety_revelation.effect.vizir.tooltip", new IStatGetter[]{statGetterEffectLevel}, StatFormat.oneDecimal));
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    @SubscribeEvent
    public void onHurtEntity(LivingHurtEvent event) {
        Entity attackingEntity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (!((DamageSourceInterface) event.getSource()).hasTag((byte) 3)) {
            if (attackingEntity instanceof LivingEntity attacker) {
                if (attacker.level() instanceof ServerLevel worldIn) {
                    ItemStack heldStack = attacker.getMainHandItem();
                    Item stackItem = heldStack.getItem();
                    if (stackItem instanceof ModularItem item) {
                        int level = item.getEffectLevel(heldStack, itemEffect);
                        if (level > 0.0F && level > Math.random() * 100F) {
                            float f = (float) Mth.atan2(target.getZ() - attacker.getZ(), target.getX() - attacker.getX());
                            WandUtil.spawnFangs(attacker, target.getX(), target.getZ(), target.getY(), target.getY() + 1.0, f, 1, 3, 0);
                            BlockPos blockpos = attacker.blockPosition().above(1);
                            AllyVex vexentity = new AllyVex(ModEntityType.VEX_SERVANT.get(), worldIn);
                            vexentity.setTrueOwner(attacker);
                            vexentity.moveTo(blockpos, 0.0F, 0.0F);
                            vexentity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockpos), MobSpawnType.MOB_SUMMONED, null, null);
                            vexentity.setBoundOrigin(blockpos);
                            vexentity.setLimitedLife(100);
                            {
                                Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(vexentity.getMainHandItem());
                                map.putIfAbsent(Enchantments.SHARPNESS, 3);
                                EnchantmentHelper.setEnchantments(map, vexentity.getMainHandItem());
                                vexentity.setItemSlot(EquipmentSlot.MAINHAND, vexentity.getMainHandItem());
                            }
                            if (attacker instanceof ServerPlayer serverPlayer) {
                                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, vexentity);
                            }
                            attacker.playSound(SoundEvents.EVOKER_CAST_SPELL);
                            vexentity.setTarget(target);
                            worldIn.addFreshEntity(vexentity);
                        }
                    }
                }
            }

        }
    }
}
