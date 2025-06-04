package com.mega.revelationfix.common.compat.ironspell;

import com.mega.revelationfix.util.LivingEntityEC;
import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class IronSpellbooksEvents {
    @SubscribeEvent
    public static void preSpellCast(SpellPreCastEvent event) {
        if (((LivingEntityEC) event.getEntity()).revelationfix$livingECData().banAnySpelling > 0) {
            event.getEntity().displayClientMessage(Component.translatable("info.goety_revelation.no_spells").withStyle(ChatFormatting.RED), true);
            event.setCanceled(true);
        }
    }
}
