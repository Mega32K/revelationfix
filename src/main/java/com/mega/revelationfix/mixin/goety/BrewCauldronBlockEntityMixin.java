package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.blocks.entities.BrewCauldronBlockEntity;
import com.Polarice3.Goety.common.crafting.BrewingRecipe;
import com.Polarice3.Goety.common.crafting.ModRecipeSerializer;
import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffectInstance;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.effects.brew.PotionBrewEffect;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import com.Polarice3.Goety.common.effects.brew.modifiers.CapacityModifier;
import com.Polarice3.Goety.common.items.ModItems;
import com.Polarice3.Goety.utils.BrewUtils;
import com.mega.revelationfix.common.config.BrewConfig;
import com.mega.revelationfix.common.data.brew.BrewData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
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
import java.util.ArrayList;
import java.util.List;

@Mixin(BrewCauldronBlockEntity.class)
public abstract class BrewCauldronBlockEntityMixin extends BlockEntity implements WorldlyContainer {
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

    @Shadow
    public abstract @NotNull ItemStack getItem(int pIndex);

    @Shadow(remap = false)
    public abstract EntityType<?> getSacrificed(int pIndex);

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
            if (BrewEffects.INSTANCE == null) {
                BrewEffects.INSTANCE = new BrewEffects();
                BrewData.reRegister();
            }
            BrewModifier brewModifier = BrewEffects.INSTANCE.getModifier(ingredient);
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
                    BrewEffect brewEffect = BrewEffects.INSTANCE.getEffectFromCatalyst(ingredient);
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
                                if (this.getDuration() == 4 && modLevel == 4) {
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
                                    this.multiplyCost(3.0F);
                                    return BrewCauldronBlockEntity.Mode.BREWING;
                                }
                                if (this.getAmplifier() == 4 && modLevel == 4) {
                                    ++this.amplifier;
                                    this.multiplyCost(3.0F);
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
                                if (this.getAoE() == 4 && modLevel == 4) {
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

                                if (this.getLingering() == 4.0F && modLevel == 4) {
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
                                if (this.getQuaff() == 32 && modLevel == 4) {
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
                                if (this.getVelocity() == 0.7F && modLevel == 4) {
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

    /**
     * @author Mega
     * @reason reason in text versions
     */
    @Overwrite(remap = false)
    public ItemStack getBrew() {
        if (BrewEffects.INSTANCE == null) {
            BrewEffects.INSTANCE = new BrewEffects();
            BrewData.reRegister();
        }
        ItemStack brew = new ItemStack(ModItems.BREW.get());
        if (this.level != null && !this.level.isClientSide) {
            List<MobEffectInstance> effects = new ArrayList<>();
            List<BrewEffectInstance> blockEffects = new ArrayList<>();
            int hidden = 0;
            for (int i = 0; i < this.getCapacity(); i++) {
                ItemStack itemStack = this.getItem(i);
                Item item = itemStack.getItem();
                BrewModifier brewModifier = BrewEffects.INSTANCE.getModifier(item);
                BrewEffect brewEffect = BrewEffects.INSTANCE.getEffectFromCatalyst(item);
                BrewingRecipe brewingRecipe = this.level.getRecipeManager().getAllRecipesFor(ModRecipeSerializer.BREWING_TYPE.get()).stream().filter(recipe -> recipe.input.test(itemStack)).findFirst().orElse(null);
                EntityType<?> entityType = this.getSacrificed(i);
                if (entityType != null) {
                    brewingRecipe = this.level.getRecipeManager().getAllRecipesFor(ModRecipeSerializer.BREWING_TYPE.get()).stream()
                            .filter(recipe -> {
                                if (recipe.getEntityTypeTag() != null) {
                                    return entityType.is(recipe.getEntityTypeTag());
                                } else if (recipe.getEntityType() != null) {
                                    return entityType == recipe.getEntityType();
                                }
                                return false;
                            }).findFirst().orElse(null);
                    brewEffect = BrewEffects.INSTANCE.getEffectFromSacrifice(entityType);
                }
                BrewingRecipe finalBrewingRecipe = brewingRecipe;
                if (brewingRecipe != null && effects.stream().noneMatch(effect -> effect.getEffect() == finalBrewingRecipe.output)) {
                    effects.add(new MobEffectInstance(brewingRecipe.output, brewingRecipe.duration));
                } else if (brewEffect != null) {
                    if (brewEffect instanceof PotionBrewEffect potionBrewEffect) {
                        effects.add(new MobEffectInstance(potionBrewEffect.mobEffect, potionBrewEffect.duration));
                    } else {
                        blockEffects.add(new BrewEffectInstance(brewEffect, brewEffect.duration));
                    }
                } else if (brewModifier != null) {
                    if (brewModifier.getId().equals(BrewModifier.HIDDEN)) {
                        hidden++;
                    } else if (brewModifier.getId().equals(BrewModifier.SPLASH) && brew.is(ModItems.BREW.get())) {
                        brew = new ItemStack(ModItems.SPLASH_BREW.get());
                    } else if (brewModifier.getId().equals(BrewModifier.LINGERING) && brew.is(ModItems.SPLASH_BREW.get())) {
                        brew = new ItemStack(ModItems.LINGERING_BREW.get());
                    } else if (brewModifier.getId().equals(BrewModifier.GAS) && brew.is(ModItems.LINGERING_BREW.get())) {
                        brew = new ItemStack(ModItems.GAS_BREW.get());
                    }
                }
            }
            for (int i = 0; i < effects.size(); i++) {
                for (int j = 0; j < this.getDuration(); j++) {
                    MobEffect type = effects.get(i).getEffect();
                    int duration = effects.get(i).getDuration();
                    effects.set(i, new MobEffectInstance(type, type.isInstantenous() ? duration : duration * 2));
                }
                for (int j = 0; j < this.getAmplifier(); j++) {
                    MobEffect type = effects.get(i).getEffect();
                    int duration = effects.get(i).getDuration();
                    effects.set(i, new MobEffectInstance(type, type.isInstantenous() ? duration : duration / 2, Math.min(effects.get(i).getAmplifier() + 1, BrewConfig.maxAmplier(type))));
                }
                for (int j = 0; j < hidden; j++) {
                    MobEffect type = effects.get(i).getEffect();
                    int duration = effects.get(i).getDuration();
                    int amplifier = effects.get(i).getAmplifier();
                    effects.set(i, new MobEffectInstance(type, duration, amplifier, false, false, false));
                }
            }
            for (int i = 0; i < blockEffects.size(); i++) {
                for (int j = 0; j < this.getDuration(); j++) {
                    BrewEffect type = blockEffects.get(i).getEffect();
                    int duration = blockEffects.get(i).getDuration();
                    blockEffects.set(i, new BrewEffectInstance(type, type.isInstantenous() ? duration : duration * 2));
                }
                for (int j = 0; j < this.getAmplifier(); j++) {
                    BrewEffect type = blockEffects.get(i).getEffect();
                    int duration = blockEffects.get(i).getDuration();
                    blockEffects.set(i, new BrewEffectInstance(type, duration, blockEffects.get(i).getAmplifier() + 1));
                }
            }
            BrewUtils.setCustomEffects(brew, effects, blockEffects);
            BrewUtils.setAreaOfEffect(brew, this.getAoE());
            BrewUtils.setLingering(brew, this.getLingering());
            BrewUtils.setQuaff(brew, this.getQuaff());
            BrewUtils.setVelocity(brew, this.getVelocity());
            BrewUtils.setAquatic(brew, this.isAquatic());
            BrewUtils.setFireProof(brew, this.isFireProof());
            brew.getOrCreateTag().putInt("CustomPotionColor", BrewUtils.getColor(effects, blockEffects));
            brew.getOrCreateTag().putBoolean("CustomBrew", true);
            this.markUpdated();
        }
        return brew;
    }

}
