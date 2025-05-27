package com.mega.revelationfix.common.client.key;

import com.mega.revelationfix.common.network.PacketHandler;
import com.mega.revelationfix.common.network.c2s.TryTimeStopSkill;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CuriosSkillKeyMapping {
    private static final Minecraft mc = Minecraft.getInstance();
    public static KeyMapping ACTIVE_SKILL = new KeyMapping("key.revelationfix.curios_skill", GLFW.GLFW_KEY_K, "key.revelationfix.category");

    @SubscribeEvent
    public static void onKeyboard(InputEvent.Key event) {
        if (mc.screen == null && event.getAction() == GLFW.GLFW_PRESS && event.getKey() == ACTIVE_SKILL.getKey().getValue()) {
            if (ATAHelper2.hasOdamane(mc.player) || ATAHelper2.hasEternalWatch(mc.player)) {
                PacketHandler.sendToServer(new TryTimeStopSkill(mc.player.getId()));
            }
        }
    }
}
