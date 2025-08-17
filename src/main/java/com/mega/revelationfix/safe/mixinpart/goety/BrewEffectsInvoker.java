package com.mega.revelationfix.safe.mixinpart.goety;

import com.Polarice3.Goety.common.effects.brew.BrewEffect;
import com.Polarice3.Goety.common.effects.brew.modifiers.BrewModifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public interface BrewEffectsInvoker {
    void modifierRegister_(BrewModifier modifier, Item ingredient);

    void register_(BrewEffect effect, Item ingredient);

    void register_(BrewEffect effect, EntityType<?> sacrifice);

    void forceModifierRegister_(BrewModifier modifier, Item ingredient);

    void forceRegister_(BrewEffect effect, Item ingredient);

    void forceRegister_(BrewEffect effect, EntityType<?> sacrifice);
}
