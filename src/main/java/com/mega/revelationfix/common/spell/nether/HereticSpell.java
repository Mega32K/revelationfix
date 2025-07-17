package com.mega.revelationfix.common.spell.nether;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.enchantments.ModEnchantments;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.SummonSpell;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.MobUtil;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.common.config.GRSpellConfig;
import com.mega.revelationfix.common.entity.cultists.HereticServant;
import com.mega.revelationfix.common.entity.cultists.MaverickServant;
import com.mega.revelationfix.common.init.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import z1gned.goetyrevelation.util.ATAHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class HereticSpell extends SummonSpell {

    public int defaultSoulCost() {
        return GRSpellConfig.HereticCost.get();
    }

    public int defaultCastDuration() {
        return GRSpellConfig.HereticDuration.get();
    }

    public int SummonDownDuration() {
        return GRSpellConfig.HereticSummonDown.get();
    }

    public int defaultSpellCooldown() {
        return GRSpellConfig.HereticCoolDown.get();
    }

    public SoundEvent CastingSound() {
        return ModSounds.PREPARE_SUMMON.get();
    }

    public SpellType getSpellType() {
        return SpellType.NETHER;
    }

    public Predicate<LivingEntity> summonPredicate() {
        return (livingEntity) -> livingEntity instanceof MaverickServant;
    }

    public int summonLimit() {
        return GRSpellConfig.HereticLimit.get();
    }

    public List<Enchantment> acceptedEnchantments() {
        List<Enchantment> list = new ArrayList<>();
        list.add(ModEnchantments.POTENCY.get());
        list.add(ModEnchantments.DURATION.get());
        return list;
    }

    public void commonResult(ServerLevel worldIn, LivingEntity caster) {
    }

    public void SpellResult(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat) {
        this.commonResult(worldIn, caster);
        int potency = spellStat.getPotency();
        int duration = spellStat.getDuration();
        if (WandUtil.enchantedFocus(caster)) {
            potency += WandUtil.getLevels(ModEnchantments.POTENCY.get(), caster);
            duration += WandUtil.getLevels(ModEnchantments.DURATION.get(), caster) + 1;
        }

        {
            potency = Mth.clamp(potency + 1, 1, 3);
            //独行者的数量
            int mavericks = potency;
            //异教徒比独行者多一
            int heretics = mavericks + 1;
            boolean netherStaff = rightStaff(staff);
            if (netherStaff) {
                mavericks = heretics = 4;
            }
            //生成独行者
            for (int i1 = 0; i1 < mavericks; ++i1) {
                MaverickServant summonedentity = new MaverickServant(ModEntities.MAVERICK_SERVANT.get(), worldIn);
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn), 0.0F, 0.0F);
                MobUtil.moveDownToGround(summonedentity);
                summonedentity.setPersistenceRequired();
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                {
                    summonedentity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 999999, netherStaff ? 1 : (ATAHelper.hasHalo(caster) ? 4 : 0)));
                    summonedentity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 999999, netherStaff ? 1 : 0));
                    summonedentity.setNetherStaffSummoned(netherStaff);
                }

                summonedentity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                this.SummonSap(caster, summonedentity);
                this.setTarget(caster, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                this.summonAdvancement(caster, summonedentity);
            }
            //生成异教徒
            for (int i1 = 0; i1 < heretics; ++i1) {
                HereticServant summonedentity = new HereticServant(ModEntities.HERETIC_SERVANT.get(), worldIn);
                summonedentity.setTrueOwner(caster);
                summonedentity.moveTo(BlockFinder.SummonRadius(caster.blockPosition(), summonedentity, worldIn), 0.0F, 0.0F);
                MobUtil.moveDownToGround(summonedentity);
                summonedentity.setPersistenceRequired();
                summonedentity.setLimitedLife(MobUtil.getSummonLifespan(worldIn) * duration);
                {
                    summonedentity.setNetherStaffSummoned(netherStaff);
                }
                summonedentity.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(caster.blockPosition()), MobSpawnType.MOB_SUMMONED, null, null);
                this.SummonSap(caster, summonedentity);
                this.setTarget(caster, summonedentity);
                worldIn.addFreshEntity(summonedentity);
                this.summonAdvancement(caster, summonedentity);
            }
            this.SummonDown(caster);
            worldIn.playSound(null, caster.getX(), caster.getY(), caster.getZ(), ModSounds.SUMMON_SPELL.get(), this.getSoundSource(), 1.0F, 1.0F);
        }

    }

    /**
     * 禁用shift无视召唤上限
     */
    @Override
    public boolean isShifting(LivingEntity caster) {
        return false;
    }
}
