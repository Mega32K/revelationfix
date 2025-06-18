package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.Polarice3.Goety.client.particles.DustCloudParticleOption;
import com.Polarice3.Goety.client.particles.ModParticleTypes;
import com.Polarice3.Goety.common.entities.projectiles.HellBolt;
import com.Polarice3.Goety.common.entities.projectiles.Hellfire;
import com.Polarice3.Goety.common.entities.projectiles.WaterHurtingProjectile;
import com.Polarice3.Goety.init.ModSounds;
import com.Polarice3.Goety.utils.BlockFinder;
import com.Polarice3.Goety.utils.ColorUtil;
import com.Polarice3.Goety.utils.ServerParticleUtil;
import com.mega.revelationfix.common.entity.TheEndHellfire;
import com.mega.revelationfix.util.entity.ATAHelper2;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
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

@Mixin(HellBolt.class)
public abstract class HellBoltMixin extends WaterHurtingProjectile {
    HellBoltMixin(EntityType<? extends AbstractHurtingProjectile> p_36833_, Level p_36834_) {
        super(p_36833_, p_36834_);
    }

    @Shadow(remap = false)
    public abstract boolean isRain();

    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/entities/projectiles/WaterHurtingProjectile;onHit(Lnet/minecraft/world/phys/HitResult;)V", shift = At.Shift.AFTER), cancellable = true)
    private void onHit(HitResult pResult, CallbackInfo ci) {
        if (!this.level.isClientSide) {
            if (this.getOwner() instanceof Player player && ATAHelper2.hasOdamane(player)) {
                ci.cancel();
                if (!this.isRain()) {
                    Vec3 vec3 = Vec3.atCenterOf(this.blockPosition());
                    Hellfire hellfire;
                    if (pResult instanceof BlockHitResult blockHitResult) {
                        BlockPos blockpos = blockHitResult.getBlockPos().relative(blockHitResult.getDirection());
                        if (BlockFinder.canBeReplaced(this.level, blockpos)) {
                            hellfire = new TheEndHellfire(this.level, Vec3.atCenterOf(blockpos), player);
                            vec3 = Vec3.atCenterOf(blockpos);
                            this.level.addFreshEntity(hellfire);
                        }
                    } else if (pResult instanceof EntityHitResult entityHitResult) {
                        Entity entity1 = entityHitResult.getEntity();
                        hellfire = new TheEndHellfire(this.level, Vec3.atCenterOf(entity1.blockPosition()), player);
                        vec3 = Vec3.atCenterOf(entity1.blockPosition());
                        this.level.addFreshEntity(hellfire);
                    }

                    Level var10 = this.level;
                    if (var10 instanceof ServerLevel serverLevel) {
                        ServerParticleUtil.addParticlesAroundSelf(serverLevel, ModParticleTypes.BIG_FIRE.get(), this);
                        ColorUtil colorUtil = new ColorUtil(14523414);
                        serverLevel.sendParticles(new CircleExplodeParticleOption(colorUtil.red, colorUtil.green, colorUtil.blue, 2.0F, 1), vec3.x, BlockFinder.moveDownToGround(this), vec3.z, 1, 0.0, 0.0, 0.0, 0.0);
                        DustCloudParticleOption cloudParticleOptions = new DustCloudParticleOption(new Vector3f(Vec3.fromRGB24(8021604).toVector3f()), 1.0F);
                        DustCloudParticleOption cloudParticleOptions2 = new DustCloudParticleOption(new Vector3f(Vec3.fromRGB24(15508116).toVector3f()), 1.0F);

                        for (int i = 0; i < 2; ++i) {
                            ServerParticleUtil.circularParticles(serverLevel, cloudParticleOptions, vec3.x, this.getY() + 0.25, vec3.z, 0.0, 0.14, 0.0, 1.0F);
                        }

                        ServerParticleUtil.circularParticles(serverLevel, cloudParticleOptions2, vec3.x, this.getY() + 0.25, vec3.z, 0.0, 0.14, 0.0, 1.0F);
                    }

                    this.playSound(ModSounds.HELL_BOLT_IMPACT.get(), 1.0F, 1.0F);
                }

                this.discard();
            }
        }
    }
}
