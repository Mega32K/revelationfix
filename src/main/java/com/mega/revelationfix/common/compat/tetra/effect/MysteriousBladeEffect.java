package com.mega.revelationfix.common.compat.tetra.effect;

import com.Polarice3.Goety.api.entities.IOwned;
import com.mega.revelationfix.safe.DamageSourceInterface;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.IStatGetter;
import se.mickelus.tetra.gui.stats.getter.ITooltipGetter;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

public class MysteriousBladeEffect {
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.mysterious_blade");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.mysterious_blade.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.decimalLabel, new TooltipGetterMysteriousBlade());
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.exit(-1);
        }
    }

    public static float getDecimalPercentage(float percentage, float base) {
        return base * (percentage / 100.0F);
    }

    @SubscribeEvent
    public void onLivingAttackEvent(LivingAttackEvent event) {
        Entity attackingEntity = event.getSource().getEntity();
        LivingEntity target = event.getEntity();
        if (!((DamageSourceInterface) event.getSource()).hasTag((byte) 3)) {
            if (attackingEntity instanceof LivingEntity attacker) {
                ItemStack heldStack = attacker.getMainHandItem();
                Item var7 = heldStack.getItem();
                if (var7 instanceof ModularItem item) {
                    float level = (float) item.getEffectLevel(heldStack, itemEffect);
                    float baseAmount = event.getAmount();
                    float magicBonusDamage = getDecimalPercentage(level, baseAmount);
                    if (level > 0.0F) {
                        if (level > Math.random() * 100) {
                            for (LivingEntity living : attacker.level.getEntitiesOfClass(LivingEntity.class, attacker.getBoundingBox().inflate(8, 8, 8), (e) -> e instanceof IOwned owned && owned.getTrueOwner() == attacker)) {
                                living.heal(2F);
                            }
                        }
                        try {
                            DamageSource source = target.damageSources().source(DamageTypes.MAGIC, attacker);
                            ((DamageSourceInterface) source).giveSpecialTag((byte) 3);
                            target.hurt(source, magicBonusDamage);
                        } catch (Throwable var13) {
                            target.hurt(target.damageSources().magic(), magicBonusDamage);
                        }
                    }
                }
            }

        }
    }

    static class TooltipGetterMysteriousBlade implements ITooltipGetter {
        private static final IStatGetter levelGetter;

        static {
            levelGetter = new StatGetterEffectLevel(itemEffect, 1.0);
        }

        public TooltipGetterMysteriousBlade() {
        }

        public String getTooltipBase(Player player, ItemStack itemStack) {
            String level = String.format("%.0f%%", levelGetter.getValue(player, itemStack));
            return I18n.get("goety_revelation.effect.mysterious_blade.tooltip", level, level);
        }

        public boolean hasExtendedTooltip(Player player, ItemStack itemStack) {
            return false;
        }
    }
}
