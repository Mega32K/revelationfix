package com.mega.revelationfix.common.data.loot;

import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;

public class ModEntitiesLoot extends EntityLootSubProvider {
    public ModEntitiesLoot() {
        super(FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    public void generate() {
        //this.add(ModEntityType.BROOD_MOTHER.get(), );
    }
}
