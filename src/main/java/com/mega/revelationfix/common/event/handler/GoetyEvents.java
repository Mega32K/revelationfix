package com.mega.revelationfix.common.event.handler;

import com.Polarice3.Goety.common.blocks.ModBlocks;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.mega.revelationfix.api.event.block.DarkAltarEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class GoetyEvents {
    public static final ResourceLocation ENDER_KEEPER_RECIPE = new ResourceLocation("goety_revelation:ender_keeper_sword");
    @SubscribeEvent
    public static void onEnderKeeperCrafting(DarkAltarEvent.TickEvent event) {
        if (event.getTickPhase() == DarkAltarEvent.TickEvent.TickPhase.CRAFTING_SECOND) {
            if (event.getLevel().isClientSide()) return;
            RitualRecipe recipe = event.getAltarBlockEntity().getCurrentRitualRecipe();
            if (recipe != null && recipe.getId().equals(ENDER_KEEPER_RECIPE)) {
                //尝试破坏周围虚空块
                if (event.getAltarBlockEntity().currentTime % 2 == 0) {
                    int range = RitualRequirements.RANGE;
                    for (int i = -range; i <= range; ++i) {
                        for (int j = range; j >= -range; --j) {
                            for (int k = -range; k <= range; ++k) {
                                BlockPos blockpos1 = event.getAltarBlockEntity().getBlockPos().offset(i, j, k);
                                BlockState blockstate = event.getLevel().getBlockState(blockpos1);
                                if (blockstate.is(ModBlocks.VOID_BLOCK.get())) {
                                    if (event.getLevel().destroyBlock(blockpos1, false)) {
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
