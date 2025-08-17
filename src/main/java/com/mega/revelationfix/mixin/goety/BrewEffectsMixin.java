package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import com.Polarice3.Goety.common.effects.brew.modifiers.CapacityModifier;
import com.mega.revelationfix.common.data.brew.BrewData;
import com.mega.revelationfix.safe.mixinpart.goety.BrewEffectsInvoker;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(value = BrewEffects.class, remap = false)
public abstract class BrewEffectsMixin implements BrewEffectsInvoker {
    @Shadow
    @Final
    private Map<String, BrewEffect> effectIDs;
    @Shadow
    @Final
    private Map<EntityType<?>, BrewEffect> sacrifice;
    @Shadow
    @Final
    private Map<String, EntityType<?>> sacrificeInverted;
    @Shadow
    @Final
    private Map<Item, BrewModifier> modifiers;
    @Shadow
    @Final
    private Map<Item, BrewEffect> catalyst;
    @Shadow
    @Final
    private Map<String, ItemStack> catalystInverted;

    @Shadow(remap = false)
    protected abstract void modifierRegister(BrewModifier modifier, Item ingredient);

    @Shadow
    protected abstract void register(BrewEffect effect, Item ingredient);

    @Shadow
    protected abstract void register(BrewEffect effect, EntityType<?> sacrifice);

    @Override
    public void register_(BrewEffect effect, Item ingredient) {
        this.register(effect, ingredient);
        List<String> list = BrewData.TEMP_ALL_ITEM_CATALYSTS;
        list.remove(effect.getEffectID());
        list.add(effect.getEffectID());
    }

    @Override
    public void register_(BrewEffect effect, EntityType<?> sacrifice) {
        this.register(effect, sacrifice);
        List<String> list = BrewData.TEMP_ALL_ENTITY_CATALYSTS;
        list.remove(effect.getEffectID());
        list.add(effect.getEffectID());
    }

    @Override
    public void modifierRegister_(BrewModifier modifier, Item ingredient) {
        this.modifierRegister(modifier, ingredient);
        if (modifier instanceof CapacityModifier) {
            List<Item> list = BrewData.LEVEL_TO_CAPACITY_MAP.get(modifier.level);
            list.remove(ingredient);
            list.add(ingredient);
        } else {
            Int2ObjectOpenHashMap<ObjectArrayList<Item>> correspondingMap = BrewData.TYPE_TO_LEVEL_AUGMENTATIONS.get(modifier.id);
            if (correspondingMap != null) {
                List<Item> list = correspondingMap.get(modifier.level);
                list.remove(ingredient);
                list.add(ingredient);
            }
        }
    }

    @Override
    public void forceRegister_(BrewEffect effect, EntityType<?> sacrifice) {
        this.effectIDs.put(effect.getEffectID(), effect);
        this.sacrifice.put(sacrifice, effect);
        this.sacrificeInverted.put(effect.getEffectID(), sacrifice);
        List<String> list = BrewData.TEMP_ALL_ENTITY_CATALYSTS;
        list.remove(effect.getEffectID());
        list.add(effect.getEffectID());
    }

    @Override
    public void forceRegister_(BrewEffect effect, Item ingredient) {
        this.effectIDs.put(effect.getEffectID(), effect);
        this.catalyst.put(ingredient, effect);
        this.catalystInverted.put(effect.getEffectID(), new ItemStack(ingredient));
        List<String> list = BrewData.TEMP_ALL_ITEM_CATALYSTS;
        list.remove(effect.getEffectID());
        list.add(effect.getEffectID());
    }

    @Override
    public void forceModifierRegister_(BrewModifier modifier, Item ingredient) {
        this.modifiers.put(ingredient, modifier);
        if (modifier instanceof CapacityModifier) {
            List<Item> list = BrewData.LEVEL_TO_CAPACITY_MAP.get(modifier.level);
            list.remove(ingredient);
            list.add(ingredient);
        } else {
            Int2ObjectOpenHashMap<ObjectArrayList<Item>> correspondingMap = BrewData.TYPE_TO_LEVEL_AUGMENTATIONS.get(modifier.id);
            if (correspondingMap != null) {
                List<Item> list = correspondingMap.get(modifier.level);
                list.remove(ingredient);
                list.add(ingredient);
            }
        }
    }

    @Inject(method = "modifierRegister", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", shift = At.Shift.AFTER))
    private void modifierRegister0(BrewModifier modifier, Item ingredient, CallbackInfo ci) {
        if (modifier instanceof CapacityModifier) {
            List<Item> list = BrewData.LEVEL_TO_CAPACITY_MAP.get(modifier.level);
            list.remove(ingredient);
            list.add(ingredient);
        } else {
            Int2ObjectOpenHashMap<ObjectArrayList<Item>> correspondingMap = BrewData.TYPE_TO_LEVEL_AUGMENTATIONS.get(modifier.id);
            if (correspondingMap != null) {
                List<Item> list = correspondingMap.get(modifier.level);
                list.remove(ingredient);
                list.add(ingredient);
            }
        }
    }
    /*
    @Inject(method = "register(Lcom/Polarice3/Goety/common/effects/brew/BrewEffect;Lnet/minecraft/world/entity/EntityType;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, shift = At.Shift.AFTER))
    private void registerEntityCatalyst(BrewEffect effect, EntityType<?> sacrifice, CallbackInfo ci) {
        List<String> list = BrewData.TEMP_ALL_ENTITY_CATALYSTS;
        list.remove(effect.getEffectID());
        list.add(effect.getEffectID());
    }
    @Inject(method = "register(Lcom/Polarice3/Goety/common/effects/brew/BrewEffect;Lnet/minecraft/world/item/Item;)V", at = @At(value = "INVOKE", target = "Ljava/util/Map;put(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;", ordinal = 0, shift = At.Shift.AFTER))
    private void registerItemCatalyst(BrewEffect effect, Item ingredient, CallbackInfo ci) {
        List<String> list = BrewData.TEMP_ALL_ITEM_CATALYSTS;
        list.remove(effect.getEffectID());
        list.add(effect.getEffectID());
    }
     */
}
