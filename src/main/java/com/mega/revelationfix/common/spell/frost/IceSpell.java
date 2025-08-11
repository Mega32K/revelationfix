package com.mega.revelationfix.common.spell.frost;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.client.particles.FoggyCloudParticleOption;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.entities.projectiles.IceSpear;
import com.Polarice3.Goety.common.entities.projectiles.IceSpike;
import com.Polarice3.Goety.common.magic.EverChargeSpell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.FeastSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.endinglib.util.entity.DamageSourceGenerator;
import com.mega.revelationfix.common.config.GRSpellConfig;
import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.s2c.IceSpellParticlePacket;
import com.mega.revelationfix.common.network.s2c.SpellCircleStatePacket;
import com.mega.revelationfix.safe.mixinpart.goety.IceSpikeEC;
import com.mega.revelationfix.util.entity.EntityFinder;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class IceSpell extends EverChargeSpell {

    public SpellStat defaultStats() {
        return super.defaultStats().setRange(0).setVelocity(1F);
    }

    public int defaultSoulCost() {
        return GRSpellConfig.IceCost.get();
    }

    public int defaultCastUp() {
        return GRSpellConfig.IceChargeUp.get();
    }

    public int defaultSpellCooldown() {
        return GRSpellConfig.IceCoolDown.get();
    }

    public @Nullable SoundEvent CastingSound() {
        return ModSounds.FROST_PREPARE_SPELL.get();
    }

    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.RANGE.get());
        list.add(ModEnchantments.VELOCITY.get());
        return list;
    }

    @Override
    public SpellType getSpellType() {
        return SpellType.FROST;
    }

    @Override
    public void startSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        if (caster instanceof ServerPlayer player) {
            int range = defaultStats().getRange();
            if (WandUtil.enchantedFocus(caster)) {
                range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
            }
            float frostAreaRadius = 12 + range * 2.5F;
            if (this.rightStaff(staff))
                frostAreaRadius += 12.0F;
            PacketHandler.sendToPlayer(player, new SpellCircleStatePacket(this.getSpellType(), SpellCircleStatePacket.STATE_START_SPELL_CIRCLE, player.getMainHandItem().equals(staff), frostAreaRadius));
        }
        super.startSpell(worldIn, caster, staff, spellStat);
    }

    @Override
    public void stopSpell(ServerLevel worldIn, LivingEntity caster, ItemStack staff, ItemStack focus, int castTime, SpellStat spellStat) {
        if (caster instanceof ServerPlayer player) {
            PacketHandler.sendToPlayer(player, new SpellCircleStatePacket(this.getSpellType(), SpellCircleStatePacket.STATE_CANCEL_SPELL_CIRCLE, true, 0));
        }
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {

        //IceSpear
        boolean rightStaff = rightStaff(staff);
        int potency = spellStat.getPotency();
        int range = spellStat.getRange();
        float velocity = spellStat.getVelocity();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            range += WandUtil.getLevels(ModEnchantments.RANGE.get(), caster);
            velocity += (float) WandUtil.getLevels(ModEnchantments.VELOCITY.get(), caster) / 2.0F;
        }
        float frostAreaRadius = 12 + range * 2.5F;
        if (rightStaff)
            frostAreaRadius += 12.0F;
        frostAreaRadius = Math.min(128, frostAreaRadius);
        if (caster.tickCount % 40 == 0) {
            PacketHandler.sendToAll(new IceSpellParticlePacket(caster.getUUID(), frostAreaRadius, IceSpellParticlePacket.CIRCLE_PARTICLES));
        }
        Vec3 location = caster.position();
        BlockPos mutableBlockPos = new BlockPos.MutableBlockPos(location.x, location.y + 1.5, location.z);
        int maxHeight = 15;

        for (int i = 0; mutableBlockPos.getY() < worldIn.getMaxBuildHeight() && worldIn.isEmptyBlock(mutableBlockPos) && i < maxHeight; ++i) {
            mutableBlockPos = mutableBlockPos.above();
        }

        if (caster.tickCount % (rightStaff ? 20 : 40) == 0) {
            DamageSource source = new DamageSourceGenerator(caster).source(DamageTypes.FREEZE, caster);
            PacketHandler.sendToAll(new IceSpellParticlePacket(caster.getUUID(), frostAreaRadius, IceSpellParticlePacket.TARGETS_PARTICLES));
            for (Entity entity : worldIn.getEntities(caster, new AABB(caster.blockPosition()).inflate(frostAreaRadius * (Math.sqrt(2) / 2) + 4), (entity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(entity) && !EntityFinder.isAlliedTo(caster, entity)))) {
                if (entity instanceof LivingEntity living) {
                    living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 2), caster);
                    if (living.hurt(source, 3 + potency)) {
                        living.invulnerableTime = 0;
                        if (living instanceof Apostle apostle)
                            apostle.moddedInvul = 0;
                        EnchantmentHelper.doPostHurtEffects(living, caster);
                        EnchantmentHelper.doPostDamageEffects(caster, living);
                    }
                }
            }
        }
        float radius4 = frostAreaRadius * 4F;
        float radius2 = frostAreaRadius * 2F;
        for (int j = 0; j < Math.min(potency + 1, 8); ++j) {
            IceSpike arrow = new IceSpike(caster, worldIn);
            arrow.setExtraDamage(arrow.getExtraDamage() + 3F + potency);
            if (this.rightStaff(staff)) {
                arrow = new IceSpear(caster, worldIn);
                arrow.setExtraDamage(arrow.getExtraDamage() + 6F);
                ((IceSpikeEC) arrow).revelationfix$setFrostPower(true);
            }

            Vec3 vec3 = mutableBlockPos.getCenter().add(worldIn.random.nextFloat() * radius4 - radius2, worldIn.random.nextFloat() * radius4 - radius2, worldIn.random.nextFloat() * radius4 - radius2);

            for (int clearTries = 0; clearTries < 6 && !worldIn.isEmptyBlock(BlockPos.containing(vec3)) && worldIn.getFluidState(BlockPos.containing(vec3)).isEmpty(); vec3 = mutableBlockPos.getCenter().add(worldIn.random.nextFloat() * radius4 - radius2, worldIn.random.nextFloat() * 4.0F - 2.0F, worldIn.random.nextFloat() * radius4 - radius2)) {
                ++clearTries;
            }

            if (!worldIn.isEmptyBlock(BlockPos.containing(vec3)) && worldIn.getFluidState(BlockPos.containing(vec3)).isEmpty()) {
                vec3 = mutableBlockPos.getCenter();
            }

            worldIn.sendParticles(new FoggyCloudParticleOption(new ColorUtil(MapColor.SNOW), 1.5F, 6), vec3.x(), vec3.y(), vec3.z(), 1, 0.0, 0.0, 0.0, 0.0);
            arrow.setPos(vec3);
            Vec3 vec31 = location.subtract(vec3);
            float randomness = 20.0F + worldIn.random.nextFloat() * 10.0F;
            if (worldIn.random.nextFloat() < 0.25F) {
                randomness = worldIn.random.nextFloat();
            }

            arrow.setRain(true);
            arrow.shoot(vec31.x, vec31.y, vec31.z, velocity + 1.5F * worldIn.random.nextFloat(), randomness);
            if (worldIn.addFreshEntity(arrow)) {
                this.playSound(worldIn, arrow, ModSounds.ICE_SPIKE_CAST.get(), 2.0F, 1.0F / (worldIn.getRandom().nextFloat() * 0.4F + 1.2F) + 0.5F);
            }
        }

    }
}
