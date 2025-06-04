package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.effects.brew.PotionBrewEffect;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import com.Polarice3.Goety.common.effects.brew.modifiers.CapacityModifier;
import com.Polarice3.Goety.common.items.ModItems;
import com.mega.revelationfix.common.data.brew.BrewData;
import com.mega.revelationfix.safe.mixinpart.goety.BrewEffectsInvoker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(value = BrewEffects.class, remap = false)
public abstract class BrewEffectsMixin implements BrewEffectsInvoker {
    @Shadow(remap = false)
    protected abstract void modifierRegister(BrewModifier modifier, Item ingredient);

    @Shadow protected abstract void register(BrewEffect effect, Item ingredient);

    @Shadow protected abstract void register(BrewEffect effect, EntityType<?> sacrifice);

    @Shadow @Final private Map<String, BrewEffect> effectIDs;

    @Shadow @Final private Map<EntityType<?>, BrewEffect> sacrifice;

    @Shadow @Final private Map<String, EntityType<?>> sacrificeInverted;

    @Shadow @Final private Map<Item, BrewModifier> modifiers;

    @Shadow @Final private Map<Item, BrewEffect> catalyst;

    @Shadow @Final private Map<String, ItemStack> catalystInverted;

    @Override
    public void register_(BrewEffect effect, Item ingredient) {
        this.register(effect, ingredient);
    }

    @Override
    public void register_(BrewEffect effect, EntityType<?> sacrifice) {
        this.register(effect, sacrifice);
    }

    @Override
    public void modifierRegister_(BrewModifier modifier, Item ingredient) {
        this.modifierRegister(modifier, ingredient);
    }

    @Override
    public void forceRegister_(BrewEffect effect, EntityType<?> sacrifice) {
        this.effectIDs.put(effect.getEffectID(), effect);
        this.sacrifice.put(sacrifice, effect);
        this.sacrificeInverted.put(effect.getEffectID(), sacrifice);

    }

    @Override
    public void forceRegister_(BrewEffect effect, Item ingredient) {
        this.effectIDs.put(effect.getEffectID(), effect);
        this.catalyst.put(ingredient, effect);
        this.catalystInverted.put(effect.getEffectID(), new ItemStack(ingredient));

    }

    @Override
    public void forceModifierRegister_(BrewModifier modifier, Item ingredient) {
        this.modifiers.put(ingredient, modifier);
    }
}
