package com.mega.revelationfix.common.item.tool.combat.sword;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.effects.GoetyEffects;
import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.youkai.YoukaiKiller;
import com.mega.revelationfix.api.item.IInvulnerableItem;
import com.mega.revelationfix.common.item.ModItemTiers;
import com.mega.revelationfix.api.item.combat.ICustomHurtWeapon;
import com.mega.revelationfix.safe.DamageSourceInterface;
import com.mega.revelationfix.safe.entity.LivingEventEC;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.OwnableEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.UUID;

public class ValetteinItem extends SwordItem implements IInvulnerableItem, ICustomHurtWeapon {
    private static final UUID MAX_HEALTH_ID = UUID.fromString("ae3f67b9-08e3-4866-8644-53770179117a");

    public ValetteinItem() {
        super(ModItemTiers.APOCALYPTIUM, 3, -2.6F, new Properties().fireResistant().rarity(Rarity.UNCOMMON));
    }

    static boolean isOwned(Entity entity, Player player) {
        return entity instanceof IOwned o && o.getTrueOwner() == player || entity instanceof OwnableEntity oe && oe.getOwner() == player;
    }

    @Override
    public boolean canBeHurtBy(DamageSource p_41387_) {
        if (!p_41387_.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) return false;
        return super.canBeHurtBy(p_41387_);
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (selectedIndex == slotIndex) {
            if (player.getCooldowns().isOnCooldown(this))
                player.fallDistance = 0;
            if (player.tickCount % 20 == 0) {
                for (Entity iterator : level.getEntities(player, new AABB(player.position(), player.position()).inflate(7F), e -> isOwned(e, player))) {
                    if (iterator instanceof LivingEntity living)
                        living.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60, 1));
                }
            }
        }
    }

    @Override
    public boolean onLeftClickEntity(ItemStack stack, Player player, Entity entity) {
        if (entity instanceof ObsidianMonolith monolith) {
            if (monolith.getTrueOwner() instanceof Player ownerPlayer) {
                if (ownerPlayer != player || !player.isAlliedTo(ownerPlayer)) {
                    monolith.die(player.damageSources().playerAttack(player));
                }
            } else monolith.die(player.damageSources().playerAttack(player));
        }
        if (SafeClass.isYoukaiLoaded()) {
            YoukaiKiller.killYoukai(entity);
        }
        return super.onLeftClickEntity(stack, player, entity);
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity) {
        if (!entity.level.isClientSide && entity instanceof Player player) {
            if (player.isShiftKeyDown() && !player.getCooldowns().isOnCooldown(this)) {
                float f7 = player.getYRot();
                float f = player.getXRot();
                float f1 = -Mth.sin(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f2 = -Mth.sin(f * ((float)Math.PI / 180F));
                float f3 = Mth.cos(f7 * ((float)Math.PI / 180F)) * Mth.cos(f * ((float)Math.PI / 180F));
                float f4 = Mth.sqrt(f1 * f1 + f2 * f2 + f3 * f3);
                float f5 = 3.0F * 1.2F;
                f1 *= f5 / f4;
                f2 *= f5 / f4;
                f3 *= f5 / f4;
                player.push(f1, f2, f3);
                player.startAutoSpinAttack(15);
                player.getCooldowns().addCooldown(this, 10);
                player.level.playSound((Player)null, player, SoundEvents.TRIDENT_RIPTIDE_1, SoundSource.PLAYERS, 1.0F, 1.3F);
                player.hurtMarked = true;
            }
        }
        return super.onEntitySwing(stack, entity);
    }

    @Override
    public void onAttack(ItemStack itemStack, LivingAttackEvent event) {
        if (!event.getEntity().level.isClientSide) {
            DamageSourceInterface dsItf = (DamageSourceInterface) event.getSource();
            if (!dsItf.hasTag((byte) 2)) {
                int rand = event.getEntity().random.nextInt(0, 3);
                switch (rand) {
                    case 0: {
                        if (!dsItf.hasTag((byte) 2) && event.getSource().getEntity() instanceof LivingEntity living) {
                            event.getEntity().hurt(living.damageSources().source(DamageTypes.ON_FIRE, living), event.getAmount() * 0.5F);
                            event.getEntity().invulnerableTime = 0;
                            dsItf.giveSpecialTag((byte) 2);
                        }
                    }
                    case 1: {
                        if (event.getSource().getEntity() instanceof Player player) {
                            float perHealth = 0.03F;
                            float perMax = 0.02F;
                            if (!player.getCooldowns().isOnCooldown(itemStack.getItem())) {
                                LivingEntity entity = event.getEntity();
                                entity.setHealth(Math.max(0.001F, entity.getHealth() * (1F - perHealth)));
                                Attribute attribute = Attributes.MAX_HEALTH;
                                AttributeInstance attributeInstance = entity.getAttribute(attribute);
                                if (attributeInstance != null) {
                                    float multi = 0F;
                                    AttributeModifier srcModifier = attributeInstance.getModifier(MAX_HEALTH_ID);
                                    if (srcModifier == null)
                                        attributeInstance.addModifier(new AttributeModifier(MAX_HEALTH_ID, "Weapon Modifier", -perMax, AttributeModifier.Operation.MULTIPLY_TOTAL));
                                    else if (srcModifier.getAmount() > -perMax * 5F) {
                                        double value = srcModifier.getAmount();
                                        attributeInstance.removeModifier(srcModifier);
                                        attributeInstance.addModifier(new AttributeModifier(MAX_HEALTH_ID, "Weapon Modifier", value - perMax, AttributeModifier.Operation.MULTIPLY_TOTAL));
                                    }
                                }
                            }
                            player.getCooldowns().addCooldown(itemStack.getItem(), 5);
                        }
                    }
                    case 2: {
                        event.getEntity().addEffect(new MobEffectInstance(GoetyEffects.CURSED.get(), 40, 1));
                    }
                }
            }
        }
        LivingEventEC ec = (LivingEventEC) event;
        ((DamageSourceInterface) event.getSource()).revelationfix$setBypassAll(true);
        ec.revelationfix$hackedUnCancelable(true);
        ec.revelationfix$hackedOnlyAmountUp(true);
    }

    @Override
    public void onHurt(ItemStack itemStack, LivingHurtEvent event) {
        LivingEventEC ec = (LivingEventEC) event;
        ((DamageSourceInterface) event.getSource()).revelationfix$setBypassAll(true);
        ec.revelationfix$hackedUnCancelable(true);
        ec.revelationfix$hackedOnlyAmountUp(true);
    }

    @Override
    public void onDamage(ItemStack itemStack, LivingDamageEvent event) {
        LivingEventEC ec = (LivingEventEC) event;
        ec.revelationfix$hackedUnCancelable(true);
        ec.revelationfix$hackedOnlyAmountUp(true);
    }

    @Override
    public void onDeath(ItemStack itemStack, LivingDeathEvent event, EventPriority priority) {
        LivingEventEC ec = (LivingEventEC) event;
        ec.revelationfix$hackedUnCancelable(true);
        event.getEntity().setHealth(0F);
    }
}
