package com.mega.revelationfix.mixin.ironspellbooks.goety_revelation;

import com.google.common.collect.ImmutableMultimap;
import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.api.item.combat.IWeaponExtraAttributesInjector;
import com.mega.revelationfix.common.item.tool.combat.sword.ValetteinItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.IPresetSpellContainer;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;

import java.util.UUID;

@Mixin(ValetteinItem.class)
@ModDependsMixin("irons_spellbooks")
public abstract class ValetteinItemMixin extends SwordItem implements IPresetSpellContainer, IWeaponExtraAttributesInjector {
    ValetteinItemMixin(Tier p_43269_, int p_43270_, float p_43271_, Properties p_43272_) {
        super(p_43269_, p_43270_, p_43271_, p_43272_);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        if (itemStack != null) {
            if (!ISpellContainer.isSpellContainer(itemStack)) {
                ISpellContainer spellContainer = ISpellContainer.create(3, true, false);
                spellContainer.addSpell(SpellRegistry.HEALING_CIRCLE_SPELL.get(), 10, true, null);
                spellContainer.addSpell(SpellRegistry.WALL_OF_FIRE_SPELL.get(), 5, true, null);
                spellContainer.addSpell(SpellRegistry.ANGEL_WINGS_SPELL.get(), 5, true, null);
                spellContainer.save(itemStack);
            }

        }
    }

    @Override
    public void onCraftedBy(@NotNull ItemStack p_41447_, @NotNull Level p_41448_, @NotNull Player p_41449_) {
        initializeSpellContainer(p_41447_);
    }

    @Override
    public void injectExtraAttributes(ImmutableMultimap.Builder<Attribute, AttributeModifier> builder) {
        builder.put(AttributeRegistry.MAX_MANA.get(), new AttributeModifier(UUID.fromString("dc14aa9f-eb1d-4eeb-988c-49e82d9196e4"), "Max Mana", 180.0, AttributeModifier.Operation.ADDITION));
    }
}
