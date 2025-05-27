package com.mega.revelationfix.common.apollyon.common;

public class MotionSimulator {
    /*
    public LivingEntity entity;
    private Vec3 deltaMovement;
    private double x;
    private double y;
    private double z;
    private double xOld;
    private double yOld;
    private double zOld;
    public MotionSimulator(LivingEntity entity) {
        this.entity = entity;
        this.deltaMovement = entity.getDeltaMovement();
        this.x = entity.getX();
        this.y = entity.getY();
        this.z = entity.getZ();
        this.xOld = entity.xOld;
        this.yOld = entity.yOld;
        this.zOld = entity.zOld;
    }
    public void simulate(int ticks) {
        for (int i=0;i<ticks;i++)
            this.tick();
    }
    private void tick() {
        this.setDeltaMovement(this.getDeltaMovement().scale(0.98F));
        this.travel(this.getDeltaMovement());
    }
    public void travel(Vec3 p_21280_) {
        if (entity.isControlledByLocalInstance()) {
            double d0 = 0.08D;
            AttributeInstance gravity = entity.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
            boolean flag = this.getDeltaMovement().y <= 0.0D;
            if (flag && entity.hasEffect(MobEffects.SLOW_FALLING)) {
                if (!gravity.hasModifier(LivingEntity.SLOW_FALLING)) gravity.addTransientModifier(LivingEntity.SLOW_FALLING);
            } else if (gravity.hasModifier(LivingEntity.SLOW_FALLING)) {
                gravity.removeModifier(LivingEntity.SLOW_FALLING);
            }
            d0 = gravity.getValue();

            FluidState fluidstate = entity.level().getFluidState(entity.blockPosition());
            if (entity.isFallFlying()) {
                Vec3 vec3 = this.getDeltaMovement();
                Vec3 vec31 = entity.getLookAngle();
                float f = entity.getXRot() * ((float)Math.PI / 180F);
                double d1 = Math.sqrt(vec31.x * vec31.x + vec31.z * vec31.z);
                double d3 = vec3.horizontalDistance();
                double d4 = vec31.length();
                double d5 = Math.cos((double)f);
                d5 = d5 * d5 * Math.min(1.0D, d4 / 0.4D);
                vec3 = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + d5 * 0.75D), 0.0D);
                if (vec3.y < 0.0D && d1 > 0.0D) {
                    double d6 = vec3.y * -0.1D * d5;
                    vec3 = vec3.add(vec31.x * d6 / d1, d6, vec31.z * d6 / d1);
                }

                if (f < 0.0F && d1 > 0.0D) {
                    double d10 = d3 * (double)(-Mth.sin(f)) * 0.04D;
                    vec3 = vec3.add(-vec31.x * d10 / d1, d10 * 3.2D, -vec31.z * d10 / d1);
                }

                if (d1 > 0.0D) {
                    vec3 = vec3.add((vec31.x / d1 * d3 - vec3.x) * 0.1D, 0.0D, (vec31.z / d1 * d3 - vec3.z) * 0.1D);
                }

                this.setDeltaMovement(vec3.multiply((double)0.99F, (double)0.98F, (double)0.99F));
                this.move(MoverType.SELF, this.getDeltaMovement());
            } else {
                BlockPos blockpos = entity.getBlockPosBelowThatAffectsMyMovement();
                float f2 = entity.level().getBlockState(entity.getBlockPosBelowThatAffectsMyMovement()).getFriction(entity.level(), entity.getBlockPosBelowThatAffectsMyMovement(), this);
                float f3 = entity.onGround() ? f2 * 0.91F : 0.91F;
                Vec3 vec35 = this.handleRelativeFrictionAndCalculateMovement(p_21280_, f2);
                double d2 = vec35.y;
                if (entity.hasEffect(MobEffects.LEVITATION)) {
                    d2 += (0.05D * (double)(entity.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec35.y) * 0.2D;
                } else if (entity.level().isClientSide && !entity.level().hasChunkAt(blockpos)) {
                    if (this.getY() > (double)entity.level().getMinBuildHeight()) {
                        d2 = -0.1D;
                    } else {
                        d2 = 0.0D;
                    }
                } else if (!entity.isNoGravity()) {
                    d2 -= d0;
                }

                if (entity.shouldDiscardFriction()) {
                    this.setDeltaMovement(vec35.x, d2, vec35.z);
                } else {
                    this.setDeltaMovement(vec35.x * (double)f3, d2 * (double)0.98F, vec35.z * (double)f3);
                }
            }
        }

    }
    public Vec3 handleRelativeFrictionAndCalculateMovement(Vec3 p_21075_, float p_21076_) {
        this.moveRelative(entity.getFrictionInfluencedSpeed(p_21076_), p_21075_);
        this.setDeltaMovement(entity.handleOnClimbable(this.getDeltaMovement()));
        this.move(MoverType.SELF, this.getDeltaMovement());
        Vec3 vec3 = this.getDeltaMovement();
        if ((entity.horizontalCollision || entity.jumping) && (entity.onClimbable() || entity.getFeetBlockState().is(Blocks.POWDER_SNOW) && PowderSnowBlock.canEntityWalkOnPowderSnow(entity))) {
            vec3 = new Vec3(vec3.x, 0.2D, vec3.z);
        }

        return vec3;
    }
    public void moveRelative(float p_19921_, Vec3 p_19922_) {
        Vec3 vec3 = getInputVector(p_19922_, p_19921_, entity.getYRot());
        this.setDeltaMovement(this.getDeltaMovement().add(vec3));
    }
    public void move(MoverType p_19973_, Vec3 p_19974_) {
        if (entity.noPhysics) {
            this.setPos(this.getX() + p_19974_.x, this.getY() + p_19974_.y, this.getZ() + p_19974_.z);
        } else {
            if (p_19973_ == MoverType.PISTON) {
                p_19974_ = entity.limitPistonMovement(p_19974_);
                if (p_19974_.equals(Vec3.ZERO)) {
                    return;
                }
            }
            if (entity.stuckSpeedMultiplier.lengthSqr() > 1.0E-7D) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            p_19974_ = entity.maybeBackOffFromEdge(p_19974_, p_19973_);
            Vec3 vec3 = entity.collide(p_19974_);
            double d0 = vec3.lengthSqr();
            if (d0 > 1.0E-7D) {
                this.setPos(this.getX() + vec3.x, this.getY() + vec3.y, this.getZ() + vec3.z);
            }

            boolean flag4 = !Mth.equal(p_19974_.x, vec3.x);
            boolean flag = !Mth.equal(p_19974_.z, vec3.z);
            this.horizontalCollision = flag4 || flag;
            this.verticalCollision = p_19974_.y != vec3.y;
            this.verticalCollisionBelow = this.verticalCollision && p_19974_.y < 0.0D;
            if (this.horizontalCollision) {
                this.minorHorizontalCollision = this.isHorizontalCollisionMinor(vec3);
            } else {
                this.minorHorizontalCollision = false;
            }

            BlockPos blockpos = this.getOnPosLegacy();
            BlockState blockstate = this.level().getBlockState(blockpos);
            this.checkFallDamage(vec3.y, this.onGround(), blockstate, blockpos);
            if (this.isRemoved()) {
                this.level().getProfiler().pop();
            } else {
                if (this.horizontalCollision) {
                    Vec3 vec31 = this.getDeltaMovement();
                    this.setDeltaMovement(flag4 ? 0.0D : vec31.x, vec31.y, flag ? 0.0D : vec31.z);
                }

                Block block = blockstate.getBlock();
                if (p_19974_.y != vec3.y) {
                    block.updateEntityAfterFallOn(this.level(), this);
                }

                if (this.onGround()) {
                    block.stepOn(this.level(), blockpos, blockstate, this);
                }

                Entity.MovementEmission entity$movementemission = this.getMovementEmission();
                if (entity$movementemission.emitsAnything() && !this.isPassenger()) {
                    double d1 = vec3.x;
                    double d2 = vec3.y;
                    double d3 = vec3.z;
                    this.flyDist = (float)((double)this.flyDist + vec3.length() * 0.6D);
                    BlockPos blockpos1 = this.getOnPos();
                    BlockState blockstate1 = this.level().getBlockState(blockpos1);
                    boolean flag1 = this.isStateClimbable(blockstate1);
                    if (!flag1) {
                        d2 = 0.0D;
                    }

                    this.walkDist += (float)vec3.horizontalDistance() * 0.6F;
                    this.moveDist += (float)Math.sqrt(d1 * d1 + d2 * d2 + d3 * d3) * 0.6F;
                    if (this.moveDist > this.nextStep && !blockstate1.isAir()) {
                        boolean flag2 = blockpos1.equals(blockpos);
                        boolean flag3 = this.vibrationAndSoundEffectsFromBlock(blockpos, blockstate, entity$movementemission.emitsSounds(), flag2, p_19974_);
                        if (!flag2) {
                            flag3 |= this.vibrationAndSoundEffectsFromBlock(blockpos1, blockstate1, false, entity$movementemission.emitsEvents(), p_19974_);
                        }

                        if (flag3) {
                            this.nextStep = this.nextStep();
                        } else if (this.isInWater()) {
                            this.nextStep = this.nextStep();
                            if (entity$movementemission.emitsSounds()) {
                                this.waterSwimSound();
                            }

                            if (entity$movementemission.emitsEvents()) {
                                this.gameEvent(GameEvent.SWIM);
                            }
                        }
                    } else if (blockstate1.isAir()) {
                        this.processFlappingMovement();
                    }
                }

                this.tryCheckInsideBlocks();
                float f = this.getBlockSpeedFactor();
                this.setDeltaMovement(this.getDeltaMovement().multiply((double)f, 1.0D, (double)f));
                if (this.level().getBlockStatesIfLoaded(this.getBoundingBox().deflate(1.0E-6D)).noneMatch((p_20127_) -> {
                    return p_20127_.is(BlockTags.FIRE) || p_20127_.is(Blocks.LAVA);
                })) {
                    if (this.remainingFireTicks <= 0) {
                        this.setRemainingFireTicks(-this.getFireImmuneTicks());
                    }

                    if (this.wasOnFire && (this.isInPowderSnow || this.isInWaterRainOrBubble() || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType)))) {
                        this.playEntityOnFireExtinguishedSound();
                    }
                }

                if (this.isOnFire() && (this.isInPowderSnow || this.isInWaterRainOrBubble() || this.isInFluidType((fluidType, height) -> this.canFluidExtinguish(fluidType)))) {
                    this.setRemainingFireTicks(-this.getFireImmuneTicks());
                }

                this.level.getProfiler().pop();
            }
        }
    }
    public void setPos(double p_20210_, double p_20211_, double p_20212_) {
        this.setPosRaw(p_20210_, p_20211_, p_20212_);
    }
    public final void setPosRaw(double p0, double p1, double p2) {
        if (x != p0 || y != p1 || z != p2) {
            this.x = p0;
            this.y = p1;
            this.z = p2;
        }
    }

    private static Vec3 getInputVector(Vec3 p_20016_, float p_20017_, float p_20018_) {
        double d0 = p_20016_.lengthSqr();
        if (d0 < 1.0E-7D) {
            return Vec3.ZERO;
        } else {
            Vec3 vec3 = (d0 > 1.0D ? p_20016_.normalize() : p_20016_).scale((double)p_20017_);
            float f = Mth.sin(p_20018_ * ((float)Math.PI / 180F));
            float f1 = Mth.cos(p_20018_ * ((float)Math.PI / 180F));
            return new Vec3(vec3.x * (double)f1 - vec3.z * (double)f, vec3.y, vec3.z * (double)f1 + vec3.x * (double)f);
        }
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }
    public Vec3 getDeltaMovement() {
        return this.deltaMovement;
    }

    public void setDeltaMovement(Vec3 p_20257_) {
        this.deltaMovement = p_20257_;
    }

    public void addDeltaMovement(Vec3 p_250128_) {
        this.setDeltaMovement(this.getDeltaMovement().add(p_250128_));
    }

    public void setDeltaMovement(double p_20335_, double p_20336_, double p_20337_) {
        this.setDeltaMovement(new Vec3(p_20335_, p_20336_, p_20337_));
    }
    private Vec3 position() {
        return new Vec3(x, y, z);
    }

     */
}
