package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.crafting.RitualRecipe;
import com.Polarice3.Goety.utils.SEHelper;
import com.mega.revelationfix.safe.GRSavedDataEC;
import com.mega.revelationfix.safe.GRSavedDataExpandedContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;

@Mixin(DarkAltarBlockEntity.class)
public class DarkAltarBlockEntityMixin {
    @Inject(remap = false, method = "startRitual", at = @At("HEAD"), cancellable = true)
    private void startRitual(Player player, ItemStack activationItem, RitualRecipe ritualRecipe, CallbackInfo ci) {
        if (player.level.dimension() == Level.NETHER && ritualRecipe != null && (ritualRecipe.getId().equals(new ResourceLocation(ModMain.MODID, "summon_apollyon")) || ritualRecipe.getId().equals(new ResourceLocation(ModMain.MODID, "summon_apollyon2")))) {
            if (player.level instanceof ServerLevel serverLevel) {
                DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(serverLevel);
                GRSavedDataEC savedDataEC = (GRSavedDataEC) state;
                GRSavedDataExpandedContext context = savedDataEC.revelationfix$dataEC();
                if (!context.isSummonedApollyonOverworld() && serverLevel.dimension() == Level.NETHER) {
                    player.displayClientMessage(Component.translatable("info.goety.ritual.apollyonDisable"), true);
                    ci.cancel();
                }
            }
            if (!SEHelper.apostleWarned(player)) {
                SEHelper.setApostleWarned(player, true);
                player.displayClientMessage(Component.translatable("info.goety.ritual.apollyonWarn"), true);
                ci.cancel();
                return;
            }
            SEHelper.setApostleWarned(player, false);
        }
    }
}
