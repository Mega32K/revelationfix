package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.ModEntityType;
import com.Polarice3.Goety.common.entities.hostile.servants.ObsidianMonolith;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.common.magic.Spell;
import com.Polarice3.Goety.common.magic.SpellStat;
import com.Polarice3.Goety.common.magic.spells.geomancy.BarricadeSpell;
import com.Polarice3.Goety.config.AttributesConfig;
import com.Polarice3.Goety.utils.MobUtil;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.PlayerAbilityHelper;

@Mixin(BarricadeSpell.class)
public abstract class BarricadeSpellMixin extends Spell {

    @Inject(at = @At("HEAD"), method = "SpellResult", cancellable = true, remap = false)
    private void createObsidianMonolith(ServerLevel worldIn, LivingEntity caster, ItemStack staff, SpellStat spellStat, CallbackInfo ci) {
        if (ATAHelper2.hasOdamane(caster)) {
            ci.cancel();
            HitResult rayTraceResult = this.rayTrace(worldIn, caster, 24, 3.0);
            if (rayTraceResult instanceof BlockHitResult) {
                BlockPos blockPos = ((BlockHitResult) rayTraceResult).getBlockPos();
                blockPos = blockPos.offset(0, 1, 0);
                Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
                ObsidianMonolith monolith = ModEntityType.OBSIDIAN_MONOLITH.get().create(worldIn);
                if (monolith != null) {
                    EntityType<?> entityType = monolith.getVariant(worldIn, blockPos);
                    if (entityType != null) {
                        monolith = ModEntityType.OBSIDIAN_MONOLITH.get().create(worldIn);
                    }

                    if (monolith != null) {
                        monolith.setTrueOwner(caster);
                        monolith.setPos(vec3.x(), vec3.y(), vec3.z());
                        if (worldIn instanceof ServerLevel) {
                            monolith.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
                        }
                        worldIn.addFreshEntity(monolith);
                        ((PlayerAbilityHelper) caster).setMonolith(monolith);
                        MobUtil.setBaseAttributes(monolith.getAttribute(Attributes.MAX_HEALTH), Math.max(200.0F, AttributesConfig.ObsidianMonolithHealth.get()));
                        monolith.setHealth(monolith.getMaxHealth());
                    }
                }
            }
        } else if (ATAHelper.hasHalo(caster) && staff.is(ModItems.NETHER_STAFF.get())) {
            ci.cancel();
            HitResult rayTraceResult = this.rayTrace(worldIn, caster, 16, 3.0);
            if (rayTraceResult instanceof BlockHitResult) {
                BlockPos blockPos = ((BlockHitResult) rayTraceResult).getBlockPos();
                blockPos = blockPos.offset(0, 1, 0);
                Vec3 vec3 = Vec3.atBottomCenterOf(blockPos);
                ObsidianMonolith monolith = ModEntityType.OBSIDIAN_MONOLITH.get().create(worldIn);
                if (monolith != null) {
                    EntityType<?> entityType = monolith.getVariant(worldIn, blockPos);
                    if (entityType != null) {
                        monolith = ModEntityType.OBSIDIAN_MONOLITH.get().create(worldIn);
                    }

                    if (monolith != null) {
                        monolith.setTrueOwner(caster);
                        monolith.setPos(vec3.x(), vec3.y(), vec3.z());
                        if (worldIn instanceof ServerLevel) {
                            monolith.finalizeSpawn(worldIn, worldIn.getCurrentDifficultyAt(blockPos), MobSpawnType.MOB_SUMMONED, null, null);
                        }

                        worldIn.addFreshEntity(monolith);

                        if (((PlayerAbilityHelper) caster).getMonolith() == null) {
                            ((PlayerAbilityHelper) caster).setMonolith(monolith);
                        } else {
                            ((PlayerAbilityHelper) caster).getMonolith().silentDie(caster.damageSources().starve());
                            ((PlayerAbilityHelper) caster).setMonolith(monolith);
                        }
                    }
                }
            }
        }
    }

}
