package com.mega.revelationfix.common.odamane.common;

import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class HaloEvents {
    /**
     * setHealth前最低优先级处进行减伤
     */
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onLivingDamage(LivingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (ATAHelper2.hasOdamane(player)) {
                if (ATAHelper2.getOdamaneEC(player).isInvulnerable())
                    event.setAmount(0);
                float reduce;
                ResourceKey<Level> dimension = player.level.dimension();
                if (dimension == Level.NETHER)
                    reduce = 0.75F;
                else if (dimension == Level.END)
                    reduce = 0.99F;
                else reduce = 0.5F;
                event.setAmount(OdamanePlayerExpandedContext.damageScale(event.getAmount() * (1.0F - reduce), player));
            }
        }
    }
}
