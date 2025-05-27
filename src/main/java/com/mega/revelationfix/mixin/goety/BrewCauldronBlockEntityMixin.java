package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.blocks.entities.BrewCauldronBlockEntity;
import com.Polarice3.Goety.common.crafting.BrewingRecipe;
import com.Polarice3.Goety.common.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import com.Polarice3.Goety.common.effects.brew.modifiers.CapacityModifier;
import com.Polarice3.Goety.utils.BrewUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.lang.reflect.Method;

@Mixin(BrewCauldronBlockEntity.class)
public abstract class BrewCauldronBlockEntityMixin extends BlockEntity {
    @Shadow(remap = false)
    public BrewCauldronBlockEntity.Mode mode;
    @Shadow(remap = false)
    public int capacity;
    @Shadow(remap = false)
    public int capacityUsed;
    @Shadow(remap = false)
    public int duration;
    @Shadow(remap = false)
    public int amplifier;
    @Shadow(remap = false)
    public int aoe;
    @Shadow(remap = false)
    public float lingering;
    @Shadow(remap = false)
    public int quaff;
    @Shadow(remap = false)
    public float velocity;
    @Shadow(remap = false)
    public boolean isAquatic;
    @Shadow(remap = false)
    public boolean isFireProof;

    public BrewCauldronBlockEntityMixin(BlockEntityType<?> p_155228_, BlockPos p_155229_, BlockState p_155230_) {
        super(p_155228_, p_155229_, p_155230_);
    }

    @Shadow(remap = false)
    protected abstract int getFirstEmptySlot();

    @Shadow
    public abstract void setItem(int pIndex, @NotNull ItemStack pStack);

    @Shadow(remap = false)
    public abstract int getCapacity();

    @Shadow(remap = false)
    public abstract boolean hasNoAugmentation();

    @Shadow(remap = false)
    public abstract int getCapacityUsed();

    @Shadow(remap = false)
    public abstract void addCost(float cost);

    @Shadow(remap = false)
    public abstract void setColor(int color);

    @Shadow(remap = false)
    public abstract ItemStack getBrew();

    @Shadow(remap = false)
    public abstract int getDuration();

    @Shadow(remap = false)
    public abstract void multiplyCost(float cost);

    @Shadow(remap = false)
    public abstract int getAmplifier();

    @Shadow(remap = false)
    public abstract int getAoE();

    @Shadow(remap = false)
    public abstract float getLingering();

    @Shadow(remap = false)
    public abstract int getQuaff();

    @Shadow(remap = false)
    public abstract float getVelocity();

    @Shadow(remap = false)
    public abstract boolean isAquatic();

    @Shadow(remap = false)
    public abstract boolean isFireProof();

    @Shadow(remap = false)
    public abstract void markUpdated();

    @Shadow(remap = false)
    public abstract BrewCauldronBlockEntity.Mode fail();

    @Unique
    private void fuckingClearContent() {
        try {
            Method method = this.getClass().getDeclaredMethod("clearContent");
            method.setAccessible(true);
            method.invoke(this);
        } catch (Throwable throwable) {
            try {
                Method method = this.getClass().getDeclaredMethod("m_6211_");
                method.setAccessible(true);
                method.invoke(this);
            } catch (Throwable throwable2) {
                System.exit(-1);
            }
        }
    }

    /**
     * @author Mega
     * @reason in test version
     */
    @Overwrite(remap = false)
    public BrewCauldronBlockEntity.Mode insertItem(ItemStack itemStack) {
        if (this.level != null && !this.level.isClientSide) {
            Item ingredient = itemStack.getItem();
            BrewModifier brewModifier = (new BrewEffects()).getModifier(ingredient);
            int modLevel = brewModifier != null ? brewModifier.getLevel() : -1;
            boolean activate = brewModifier instanceof CapacityModifier && brewModifier.getLevel() == 0;
            int firstEmpty = this.getFirstEmptySlot();
            ServerLevel serverLevel;
            Level var8;
            float f2;
            float f1;
            double d1;
            double d2;
            double d3;
            int k;
            if (firstEmpty != -1) {
                this.setItem(firstEmpty, itemStack);
                if (this.mode == BrewCauldronBlockEntity.Mode.IDLE && this.getCapacity() < 4 && activate) {
                    this.fuckingClearContent();
                    this.capacity = 4;
                    var8 = this.level;
                    if (var8 instanceof ServerLevel) {
                        serverLevel = (ServerLevel) var8;

                        for (k = 0; k < 20; ++k) {
                            f2 = serverLevel.random.nextFloat() * 4.0F;
                            f1 = serverLevel.random.nextFloat() * 6.2831855F;
                            d1 = Mth.cos(f1) * f2;
                            d2 = 0.01 + serverLevel.random.nextDouble() * 0.5;
                            d3 = Mth.sin(f1) * f2;
                            serverLevel.sendParticles(ParticleTypes.WITCH, (double) this.getBlockPos().getX() + 0.5 + d1 * 0.1, (double) this.getBlockPos().getY() + 0.5 + 0.3, (double) this.getBlockPos().getZ() + 0.5 + d3 * 0.1, 0, d1, d2, d3, 0.25);
                        }
                    }

                    return BrewCauldronBlockEntity.Mode.BREWING;
                }

                if (this.mode == BrewCauldronBlockEntity.Mode.BREWING) {
                    BrewingRecipe brewingRecipe = this.level.getRecipeManager().getAllRecipesFor(ModRecipeSerializer.BREWING_TYPE.get()).stream().filter(recipe -> recipe.input.test(itemStack)).findFirst().orElse(null);
                    BrewEffect brewEffect = (new BrewEffects()).getEffectFromCatalyst(ingredient);
                    if (this.hasNoAugmentation() && (brewingRecipe != null || brewEffect != null)) {
                        if (brewingRecipe != null && brewingRecipe.getCapacityExtra() + this.getCapacityUsed() <= this.getCapacity()) {
                            this.capacityUsed += brewingRecipe.getCapacityExtra();
                            this.addCost((float) brewingRecipe.soulCost);
                            this.setColor(BrewUtils.getColor(this.getBrew()));
                            return BrewCauldronBlockEntity.Mode.BREWING;
                        }

                        if (brewEffect != null && brewEffect.getCapacityExtra() + this.getCapacityUsed() <= this.getCapacity()) {
                            this.capacityUsed += brewEffect.getCapacityExtra();
                            this.addCost((float) brewEffect.getSoulCost());
                            this.setColor(BrewUtils.getColor(this.getBrew()));
                            return BrewCauldronBlockEntity.Mode.BREWING;
                        }
                    }

                    if (brewModifier != null) {
                        if (BrewUtils.hasEffect(this.getBrew())) {
                            if (brewModifier.getId().equals(BrewModifier.HIDDEN) || brewModifier.getId().equals(BrewModifier.SPLASH) || brewModifier.getId().equals(BrewModifier.LINGERING) || brewModifier.getId().equals(BrewModifier.GAS)) {
                                if (brewModifier.getId().equals(BrewModifier.HIDDEN)) {
                                    this.addCost(10.0F);
                                }

                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }

                            if (brewModifier.getId().equals(BrewModifier.DURATION)) {
                                if (this.getDuration() == 0 && modLevel == 0) {
                                    ++this.duration;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getDuration() == 1 && modLevel == 1) {
                                    ++this.duration;
                                    this.multiplyCost(1.5F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getDuration() == 2 && modLevel == 2) {
                                    ++this.duration;
                                    this.multiplyCost(2.0F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                                if (this.getDuration() == 3 && modLevel == 3) {
                                    ++this.duration;
                                    this.multiplyCost(2.0F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                            }

                            if (brewModifier.getId().equals(BrewModifier.AMPLIFIER)) {
                                if (this.getAmplifier() == 0 && modLevel == 0) {
                                    ++this.amplifier;
                                    this.multiplyCost(2.0F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getAmplifier() == 1 && modLevel == 1) {
                                    ++this.amplifier;
                                    this.multiplyCost(2.5F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getAmplifier() == 2 && modLevel == 2) {
                                    ++this.amplifier;
                                    this.multiplyCost(3.0F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                                if (this.getAmplifier() == 3 && modLevel == 3) {
                                    ++this.amplifier;
                                    this.multiplyCost(3.5F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                            }

                            if (brewModifier.getId().equals(BrewModifier.AOE)) {
                                if (this.getAoE() == 0 && modLevel == 0) {
                                    ++this.aoe;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getAoE() == 1 && modLevel == 1) {
                                    ++this.aoe;
                                    this.multiplyCost(1.5F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getAoE() == 2 && modLevel == 2) {
                                    ++this.aoe;
                                    this.multiplyCost(2.0F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                                if (this.getAoE() == 3 && modLevel == 3) {
                                    ++this.aoe;
                                    this.multiplyCost(2.0F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                            }

                            if (brewModifier.getId().equals(BrewModifier.LINGER)) {
                                if (this.getLingering() == 0.0F && modLevel == 0) {
                                    ++this.lingering;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getLingering() == 1.0F && modLevel == 1) {
                                    ++this.lingering;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getLingering() == 2.0F && modLevel == 2) {
                                    ++this.lingering;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getLingering() == 3.0F && modLevel == 3) {
                                    ++this.lingering;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                            }

                            if (brewModifier.getId().equals(BrewModifier.QUAFF)) {
                                if (this.getQuaff() == 0 && modLevel == 0) {
                                    this.quaff += 8;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getQuaff() == 8 && modLevel == 1) {
                                    this.quaff += 8;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getQuaff() == 16 && modLevel == 2) {
                                    this.quaff += 8;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                                if (this.getQuaff() == 24 && modLevel == 3) {
                                    this.quaff += 8;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                            }

                            if (brewModifier.getId().equals(BrewModifier.VELOCITY)) {
                                if (this.getVelocity() == 0.0F && modLevel == 0) {
                                    this.velocity += 0.1F;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getVelocity() == 0.1F && modLevel == 1) {
                                    this.velocity += 0.2F;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }

                                if (this.getVelocity() == 0.3F && modLevel == 2) {
                                    this.velocity += 0.2F;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                                if (this.getVelocity() == 0.5F && modLevel == 3) {
                                    this.velocity += 0.2F;
                                    this.multiplyCost(1.25F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                            }

                            if (brewModifier.getId().equals(BrewModifier.AQUATIC) && !this.isAquatic() && modLevel == 0) {
                                this.isAquatic = true;
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }

                            if (brewModifier.getId().equals(BrewModifier.FIRE_PROOF) && !this.isFireProof() && modLevel == 0) {
                                this.isFireProof = true;
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }
                        } else if (brewModifier instanceof CapacityModifier capacityModifier) {
                            if (this.getCapacity() == 4 && capacityModifier.getLevel() == 1) {
                                this.capacity += 2;
                                this.fuckingClearContent();
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }

                            if (this.getCapacity() == 6 && capacityModifier.getLevel() == 2) {
                                this.capacity += 2;
                                this.fuckingClearContent();
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }

                            if (this.getCapacity() == 8 && capacityModifier.getLevel() == 3) {
                                this.capacity += 2;
                                this.fuckingClearContent();
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }

                            if (this.getCapacity() == 10 && capacityModifier.getLevel() == 4) {
                                this.capacity += 2;
                                this.fuckingClearContent();
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }

                            if (this.getCapacity() == 12 && capacityModifier.getLevel() == 5) {
                                this.capacity += 4;
                                this.fuckingClearContent();
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }

                            if (this.getCapacity() == 16 && capacityModifier.getLevel() == 6) {
                                this.capacity += 8;
                                this.fuckingClearContent();
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }
                            if (this.getCapacity() == 24 && capacityModifier.getLevel() == 7) {
                                this.capacity += 8;
                                this.fuckingClearContent();
                                return BrewCauldronBlockEntity.Mode.BREWING;
                            }
                        }
                    }
                }
            } else if (this.mode == BrewCauldronBlockEntity.Mode.IDLE && this.getCapacity() < 4 && activate) {
                this.fuckingClearContent();
                this.capacity = 4;
                var8 = this.level;
                if (var8 instanceof ServerLevel) {
                    serverLevel = (ServerLevel) var8;

                    for (k = 0; k < 20; ++k) {
                        f2 = serverLevel.random.nextFloat() * 4.0F;
                        f1 = serverLevel.random.nextFloat() * 6.2831855F;
                        d1 = Mth.cos(f1) * f2;
                        d2 = 0.01 + serverLevel.random.nextDouble() * 0.5;
                        d3 = Mth.sin(f1) * f2;
                        serverLevel.sendParticles(ParticleTypes.WITCH, (double) this.getBlockPos().getX() + 0.5 + d1 * 0.1, (double) this.getBlockPos().getY() + 0.5 + 0.3, (double) this.getBlockPos().getZ() + 0.5 + d3 * 0.1, 0, d1, d2, d3, 0.25);
                    }
                }

                return BrewCauldronBlockEntity.Mode.BREWING;
            }

            this.markUpdated();
        }

        return this.fail();
    }
}
