package com.mega.revelationfix.common.compat.tetra.effect;

import com.Polarice3.Goety.api.entities.IOwned;
import com.mega.revelationfix.common.compat.tetra.TetraVersionCompat;
import com.mega.revelationfix.util.LivingEntityEC;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import se.mickelus.tetra.blocks.workbench.gui.WorkbenchStatsGui;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.stats.StatsHelper;
import se.mickelus.tetra.gui.stats.bar.GuiStatBar;
import se.mickelus.tetra.gui.stats.getter.LabelGetterBasic;
import se.mickelus.tetra.gui.stats.getter.StatFormat;
import se.mickelus.tetra.gui.stats.getter.StatGetterEffectLevel;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.items.modular.impl.holo.gui.craft.HoloStatsGui;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class FadingEffect {
    private static final UUID MAX_HEALTH = UUID.fromString("490791fd-833c-4141-bd68-2a495e57f4ad");
    private static final UUID ATTACK_DAMAGE = UUID.fromString("b01065c1-ffcd-4098-97ba-e1e939b5d1af");
    private static final UUID ATTACK_SPEED = UUID.fromString("c8e83dd9-8142-4408-ab0b-b5932a33741f");
    private static final UUID ARMOR = UUID.fromString("bba6b1d0-9aa4-4694-826a-b2ea17d23ef6");
    private static final UUID ARMOR_TOUGHNESS = UUID.fromString("0e6ead1e-9837-46e8-8e3c-6c1678f79ead");
    private static final UUID MOVEMENT_SPEED = UUID.fromString("a63f962f-9d88-4c17-b8fb-214e4d7793c6");
    private static final Map<Attribute, UUID> attributes = new HashMap<>();
    public static ItemEffect itemEffect = ItemEffect.get("goety_revelation.fading");

    @OnlyIn(Dist.CLIENT)
    public static void init() {
        try {
            StatGetterEffectLevel statGetterEffectLevel = new StatGetterEffectLevel(itemEffect, 1.0D);
            MultiStatGetterEffectLevel statGetterEffectLevel1 = new MultiStatGetterEffectLevel(itemEffect, 3.0D, 0D, statGetterEffectLevel);
            GuiStatBar effectBar = new GuiStatBar(0, 0, 59, "goety_revelation.effect.fading.name", 0.0D, 100.0, false, statGetterEffectLevel, LabelGetterBasic.decimalLabel, TetraVersionCompat.createTGM("goety_revelation.effect.fading.tooltip", StatsHelper.withStats(statGetterEffectLevel, statGetterEffectLevel1), StatFormat.noDecimal, StatFormat.noDecimal));
            WorkbenchStatsGui.addBar(effectBar);
            HoloStatsGui.addBar(effectBar);
        } catch (Throwable throwable) {
        }
    }

    public static Map<Attribute, UUID> getAttributes() {
        return attributes;
    }

    @SubscribeEvent
    public void onPlayerAttack(AttackEntityEvent event) {
        if (event.getTarget() instanceof LivingEntity beHurt) {
            if (beHurt.isDeadOrDying()) return;
            if (beHurt instanceof IOwned owned && owned.getTrueOwner() != null && owned.getTrueOwner().getUUID().equals(event.getEntity().getUUID()))
                return;
            if (beHurt instanceof OwnableEntity owned && owned.getOwnerUUID() != null && owned.getOwnerUUID().equals(event.getEntity().getUUID()))
                return;
            if (attributes.isEmpty()) {
                attributes.put(Attributes.MAX_HEALTH, MAX_HEALTH);
                attributes.put(Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE);
                attributes.put(Attributes.ATTACK_SPEED, ATTACK_SPEED);
                attributes.put(Attributes.ARMOR, ARMOR);
                attributes.put(Attributes.ARMOR_TOUGHNESS, ARMOR_TOUGHNESS);
                attributes.put(Attributes.MOVEMENT_SPEED, MOVEMENT_SPEED);
            }
            Player attacker = event.getEntity();
            ItemStack itemStack = attacker.getMainHandItem();
            if (itemStack.getItem() instanceof ModularItem modularItem) {
                int level = modularItem.getEffectLevel(itemStack, itemEffect);
                if (level <= 0) return;
                if (Math.random() * 100D <= level * 3)
                    return;
                ((LivingEntityEC) beHurt).revelationfix$livingECData().tetraFadingTime = 1200;
                for (Attribute attribute : attributes.keySet()) {
                    AttributeInstance instance = beHurt.getAttribute(attribute);
                    if (instance != null) {
                        UUID uuid = attributes.get(attribute);
                        AttributeModifier modifier = instance.getModifier(uuid);
                        if (modifier != null && modifier.getAmount() > -0.9F) {
                            double amount = modifier.getAmount();
                            amount = Math.max(-0.9F, amount - level / 100D);
                            instance.removeModifier(modifier);
                            instance.addTransientModifier(new AttributeModifier(uuid, "Tetra Modifier", amount, AttributeModifier.Operation.MULTIPLY_TOTAL));
                            if (!attribute.isClientSyncable()) {
                                beHurt.getAttributes().getDirtyAttributes().add(instance);
                            }
                        }
                        if (modifier == null) {
                            modifier = new AttributeModifier(uuid, "Tetra Modifier", -level / 100D, AttributeModifier.Operation.MULTIPLY_TOTAL);
                            instance.addTransientModifier(modifier);
                            if (!attribute.isClientSyncable()) {
                                beHurt.getAttributes().getDirtyAttributes().add(instance);
                            }
                        }
                    }
                }
            }
        }
    }
}
