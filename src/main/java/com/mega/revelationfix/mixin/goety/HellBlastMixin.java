package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.Polarice3.Goety.client.particles.DustCloudParticleOption;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.projectiles.HellBlast;
import com.Polarice3.Goety.common.entities.projectiles.Hellfire;
import com.Polarice3.Goety.common.entities.projectiles.WaterHurtingProjectile;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.*;
import com.mega.revelationfix.common.entity.TheEndHellfire;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HellBlast.class)
public abstract class HellBlastMixin extends WaterHurtingProjectile {
    HellBlastMixin(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    @Shadow(remap = false)
    public abstract float getRadius();

    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/projectiles/WaterHurtingProjectile;onHit(Lnet/minecraft/world/phys/HitResult;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onHit(HitResult pResult, CallbackInfo ci) {
        Level level = this.level();
        if (!level.isClientSide) {
            if (this.getOwner() instanceof Player player && ATAHelper2.hasOdamane(player)) {
                ci.cancel();
                Vec3 vec3 = Vec3.atCenterOf(this.blockPosition());
                int i;
                if (pResult instanceof BlockHitResult blockHitResult) {
                    BlockPos blockpos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
                    if (BlockFinder.canBeReplaced(level, blockpos)) {
                        vec3 = Vec3.atCenterOf(blockpos);
                    }
                } else if (pResult instanceof EntityHitResult entityHitResult) {
                    Entity entity1 = entityHitResult.getEntity();
                    vec3 = Vec3.atCenterOf(entity1.blockPosition());
                }

                Hellfire hellfire = new TheEndHellfire(level, vec3, player);
                if (level.addFreshEntity(hellfire)) {
                    Direction[] var15 = Direction.values();
                    int var18 = var15.length;

                    for (i = 0; i < var18; ++i) {
                        Direction direction = var15[i];
                        if (direction.getAxis().isHorizontal()) {
                            Hellfire hellfire1 = new TheEndHellfire(level, Vec3.atCenterOf(hellfire.blockPosition().relative(direction)), player);
                            level.addFreshEntity(hellfire1);
                        }
                    }
                }

                SpellExplosion var10001 = new SpellExplosion(level, player, ModDamageSource.hellfire(this, this.getOwner()), vec3.x, vec3.y, vec3.z, getRadius(), 0.0F) {
                    public void explodeHurt(Entity target, DamageSource damageSource, double x, double y, double z, double seen, float actualDamage) {
                        super.explodeHurt(target, damageSource, x, y, z, seen, actualDamage);
                        Entity var13 = damageSource.getDirectEntity();
                        if (var13 instanceof HellBlast hellBlast) {
                            if (hellBlast.getFiery() > 0) {
                                player.setSecondsOnFire(5 * hellBlast.getFiery());
                            }
                        }

                    }
                };
                if (level instanceof ServerLevel serverLevel) {
                    ServerParticleUtil.addParticlesAroundSelf(serverLevel, ModParticleTypes.BIG_FIRE.get(), this);
                    ColorUtil colorUtil = new ColorUtil(14523414);
                    serverLevel.sendParticles(new CircleExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, 4.0F, 1), vec3.x, BlockFinder.moveDownToGround(this), vec3.z, 1, 0.0, 0.0, 0.0, 0.0);
                    serverLevel.sendParticles(new CircleExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, 5.0F, 1), vec3.x, BlockFinder.moveDownToGround(this), vec3.z, 1, 0.0, 0.0, 0.0, 0.0);
                    DustCloudParticleOption cloudParticleOptions = new DustCloudParticleOption(new Vector3f(Vec3.fromRGB24(8021604).toVector3f()), 1.0F);
                    DustCloudParticleOption cloudParticleOptions2 = new DustCloudParticleOption(new Vector3f(Vec3.fromRGB24(15508116).toVector3f()), 1.0F);

                    for (i = 0; i < 2; ++i) {
                        ServerParticleUtil.circularParticles(serverLevel, cloudParticleOptions, vec3.x, this.getY() + 0.25, vec3.z, 0.0, 0.14, 0.0, this.getRadius() * 2.0F);
                    }

                    ServerParticleUtil.circularParticles(serverLevel, cloudParticleOptions2, vec3.x, this.getY() + 0.25, vec3.z, 0.0, 0.14, 0.0, this.getRadius() * 2.0F);
                }

                this.playSound(SoundEvents.GENERIC_EXPLODE, 4.0F, 1.0F);
                this.playSound(ModSounds.HELL_BLAST_IMPACT.get(), 4.0F, 1.0F);
                this.discard();
            }
        }
    }
}
