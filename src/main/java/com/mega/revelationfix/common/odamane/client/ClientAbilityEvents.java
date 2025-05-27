package com.mega.revelationfix.common.odamane.client;

import com.mega.revelationfix.util.ATAHelper2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderBlockScreenEffectEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 终末玩家客户端效果事件处理
 */
@Mod.EventBusSubscriber(Dist.CLIENT)
public class ClientAbilityEvents {
    /**
     * 免疫火焰水下效果动画
     */
    @SubscribeEvent
    public static void disableFireScreenEffect(RenderBlockScreenEffectEvent event) {
        if (event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.FIRE || event.getOverlayType() == RenderBlockScreenEffectEvent.OverlayType.WATER)
            if (ATAHelper2.hasOdamane(event.getPlayer()))
                event.setCanceled(true);
    }
}
