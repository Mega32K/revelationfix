package com.mega.revelationfix.common.compat.tetra.effect;

import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.LifeStealParticlesS2CPacket;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.*;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class BeelzebubEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.beelzebub");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.beelzebub.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.decimalLabel, new TooltipGetterCursedBlade());
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
        }
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingDamageEvent event) {
        LivingEntity target = event.getEntity();
        Entity attackingEntity = event.getSource().getEntity();
        if (attackingEntity instanceof LivingEntity attacker) {
            ItemStack heldStack = attacker.getMainHandItem();
            Item var7 = heldStack.getItem();
            if (var7 instanceof ModularItem item) {
                float level = (float) item.getEffectLevel(heldStack, itemEffect);
                float eff = item.getEffectEfficiency(heldStack, itemEffect);
                if (level > 0.0F && level > target.getRandom().nextFloat() * 100.0F) {
                    attacker.heal(eff);
                    if (attacker instanceof Player player) {
                        FoodData data = player.getFoodData();
                        if (data.getSaturationLevel() < 20) {
                            data.setSaturation(data.getSaturationLevel() + eff);
                        }
                        if (data.getFoodLevel() < 20) {
                            data.setFoodLevel(data.getFoodLevel() + (int) eff);
                        }
                    }
                    target.playSound(SoundEvents.WITHER_HURT, 0.2F, 1.0F);
                    PacketHandler.sendToEntity(new LifeStealParticlesS2CPacket(target.getX(), target.getY(0.5), target.getZ()), target);
                }
            }
        }

    }

    static class TooltipGetterCursedBlade implements ITooltipGetter {
        private static final IStatGetter efficiencyGetter;
        private static final IStatGetter levelGetter;

        static {
            efficiencyGetter = new StatGetterEffectEfficiency(itemEffect, 1.0);
            levelGetter = new StatGetterEffectLevel(itemEffect, 1.0);
        }

        public TooltipGetterCursedBlade() {
        }

        public String getTooltipBase(Player player, ItemStack itemStack) {
            String level = String.format("%.0f%%", levelGetter.getValue(player, itemStack));
            String efficiency = String.format("%s", efficiencyGetter.getValue(player, itemStack));
            return I18n.get("goety_revelation.effect.beelzebub.tooltip", level, efficiency, efficiency);
        }
    }
}
