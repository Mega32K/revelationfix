package com.mega.revelationfix.mixin;

import com.Polarice3.Goety.client.events.BossBarEvent;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.common.config.ClientConfig;
import com.mega.revelationfix.safe.entity.Apollyon2Interface;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.client.render.ui.NetherStarBar;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(value = BossBarEvent.class, priority = 0)
public class BossBarEventMixin {
    @Unique
    private static final ResourceLocation revelationfix$BAR = new ResourceLocation(Revelationfix.MODID, "textures/ui/boss_bar.png");
    @Unique
    private static final ResourceLocation revelationfix$BAR_S = new ResourceLocation("goety_revelation", "textures/ui/boss_bar.png");

    @Inject(
            at = {@At("HEAD")},
            method = {"drawBar"},
            cancellable = true,
            remap = false
    )
    private static void renderApollyonBar(GuiGraphics guiGraphics, int pX, int pY, float partialTicks, Mob pEntity, CallbackInfo ci) {
        if (pEntity instanceof Apostle apostle) {
            if (((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon()) {
                ci.cancel();
                float health = revelationfix$getPercent(apostle, partialTicks);
                int pX2 = pX + 9;
                int pY2 = pY + 4;
                ResourceLocation ui = apostle.isInNether() && ClientConfig.enableNewNetherApollyonBossbar ? revelationfix$BAR : revelationfix$BAR_S;
                if (health > 0) {
                    guiGraphics.blit(ui, pX2, pY2, 0.0F, 32.0F, (int) health, 8, 256, 256);
                    NetherStarBar.blitCosmicBar(guiGraphics.pose(), ui, (float) pX2, (float) pY2, 0.0F, 32.0F, (int) health, 8, 256, 256, true, ((ApollyonAbilityHelper) apostle).getApollyonTime());
                }

                guiGraphics.blit(ui, pX, pY, 0.0F, apostle.isSecondPhase() ? 16.0F : 0.0F, 200, 16, 256, 256);
            }
        }

    }

    @Unique
    private static float revelationfix$getPercent(Apostle apostle, float partialTicks) {
        return ((Apollyon2Interface) apostle).revelaionfix$apollyonEC().getBossBarPercent(partialTicks);
    }
}
