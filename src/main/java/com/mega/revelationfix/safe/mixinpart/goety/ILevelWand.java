package com.mega.revelationfix.safe.mixinpart.goety;

import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.common.magic.Spell;
import net.minecraft.world.item.ItemStack;

public interface ILevelWand {
    int getStaffLevel();
    boolean expandedRightStaffLogic(Spell spell, ItemStack stack);
    boolean expandedTypeStaffLogic(SpellType spellType, ItemStack stack);
}
