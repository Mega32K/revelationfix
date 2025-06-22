package com.mega.revelationfix.client.task;

import com.Polarice3.Goety.common.entities.hostile.WebSpider;
import com.mega.endinglib.api.client.ClientTaskInstance;
import com.mega.revelationfix.client.screen.post.custom.BarrelDistortionCoordinatesPostEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.event.TickEvent;

public class TeleportCameraTask extends ClientTaskInstance {
    @Override
    public void tick(Level level) {
        if (tickCount == 0)
            BarrelDistortionCoordinatesPostEffect.centerOffset = new Vec2((float) Math.random() - .5F, (float) Math.random() - .5F).scale(0.2F);
        BarrelDistortionCoordinatesPostEffect.tickCount = tickCount;
        if (this.tickCount >= 10) {
            this.setRemoved(true);
            BarrelDistortionCoordinatesPostEffect.tickCount = 0;
        }
    }

    @Override
    public void renderTick(TickEvent.RenderTickEvent event) {

    }
}
