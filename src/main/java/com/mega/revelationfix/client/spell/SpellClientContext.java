package com.mega.revelationfix.client.spell;

import com.Polarice3.Goety.api.magic.IChargingSpell;
import com.Polarice3.Goety.api.magic.ISpell;
import com.Polarice3.Goety.api.magic.SpellType;
import com.Polarice3.Goety.utils.WandUtil;
import com.mega.revelationfix.client.renderer.VFRBuilders;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.network.s2c.SpellCircleStatePacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.util.HashMap;

public class SpellClientContext {
    public static WandUsingCircle circle = new WandUsingCircle(SpellType.FROST, 0);
    public static void replaceAndStartCircle(WandUsingCircle newC) {
        circle = newC;
        circle.usingRenderer = true;
    }
    public static void stopCircle() {
        if (circle != null) {
            circle.usingRenderer = false;
            if (!Integer.valueOf(circle.maxLife).equals(circle.aliveTicks))
                circle.maxLife = Math.min(circle.maxLife, circle.aliveTicks + 5);
        }
    }
    public static class WandUsingCircle {
        public static HashMap<SpellType, Vector3f> spellColor = new HashMap<>();
        static {
            for (SpellType origin : SpellType.values())
                spellColor.put(origin, new Vector3f(0x58 / 255F));
            spellColor.put(SpellType.FROST, new Vector3f(0x8e / 255F, 0xc5 / 255F, 0xfc / 255F));
        }
        public SpellType spellType;
        public int maxLife;
        public int aliveTicks;
        private int aliveTicksO;
        public float radius;
        public boolean usingRenderer = false;
        public WandUsingCircle(SpellType spellType, int age) {
            this.spellType = spellType;
            this.maxLife = age;
        }

        public WandUsingCircle setRadius(float radius) {
            this.radius = radius;
            return this;
        }
        public void tick() {
            if (spellType == null) return;
            aliveTicksO = aliveTicks;
            this.aliveTicks++;
            if (aliveTicks >= maxLife) {
                this.usingRenderer = false;
                this.aliveTicks = maxLife;
            }
            Player player = Wrapped.clientPlayer();
            if (player == null) return;
            if (!player.isUsingItem())
                SpellClientContext.stopCircle();
        }
        @OnlyIn(Dist.CLIENT)
        public void render(float partialTicks, PoseStack renderEventStack) {
            if (spellType == null) return;
            if (!usingRenderer && Integer.valueOf(aliveTicksO).equals(maxLife)) return;
            Player player = Wrapped.clientPlayer();
            if (player == null) return;
            renderEventStack.pushPose();

            Vector3f color = spellColor.get(spellType);
            float red = color.x;
            float green = color.y;
            float blue = color.z;
            float alpha = Mth.clamp(partialTicks, aliveTicksO, aliveTicks);

            if (!usingRenderer) {
                alpha = (maxLife - alpha);
            }
            alpha /= 5F;
            if (alpha > 1.0F) alpha = 1.0F;
            alpha *= 0.6F;
            int endColor = 0x00000000;
            int accuracy = 22;
            float eyeHeight = player.getEyeHeight(player.getPose());
            float step = (Mth.TWO_PI / accuracy);
            MultiBufferSource.BufferSource source = Minecraft.getInstance().renderBuffers().bufferSource();
            VertexConsumer consumer = source.getBuffer(RenderType.entityTranslucent(VFRBuilders.beam, true));

            for (float f0=step;f0 <= Mth.TWO_PI;f0+=step) {
                float x = Mth.cos(f0) * radius;
                float z = Mth.sin(f0) * radius;
                float x2 = Mth.cos(f0 + step) * radius;
                float z2 = Mth.sin(f0 + step) * radius;
                drawQuad(x, 1.F, z, x2, 0.3F, z2, renderEventStack.last(), consumer, red, green, blue, alpha, 0F, 1F);
            }
            renderEventStack.popPose();
            source.endBatch();
        }
        private static void drawQuad(float x1, float y1, float z1, float x2, float y2, float z2, PoseStack.Pose pose, VertexConsumer consumer, float rT, float gT, float bT, float alpha, float uvMin, float uvMax) {
            Matrix4f poseMatrix = pose.pose();
            Matrix3f normalMatrix = pose.normal();
            consumer.vertex(poseMatrix, x1, y1, z1).color(rT, gT, bT, alpha).uv(0.0F, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, x2, y1, z2).color(rT, gT, bT, alpha).uv(1.0F, uvMin).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, x2, y2, z2).color(0.721F, 0.741F, 0.725F, 0.2F*alpha).uv(1.0F, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix,  x1, y2, z1).color(0.721F, 0.741F, 0.725F, 0.2F*alpha).uv(0.0F, uvMax).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        }
    }
    public static void receiveSpellStatePacket(SpellCircleStatePacket packet) {
        SpellType spellType = packet.getSpell();
        byte state = packet.getState();
        boolean isMainHand = packet.isMainHand();
        float radius  = packet.getRadius();

        if (state == SpellCircleStatePacket.STATE_START_SPELL_CIRCLE) {
            Player player = Wrapped.clientPlayer();
            if (player != null) {
                InteractionHand hand = isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;
                ISpell spell = WandUtil.getSpellOnHand(player, hand);
                if (spell != null) {
                    int age = spell.defaultCastDuration();
                    if (spell instanceof IChargingSpell chargingSpell)
                        age += chargingSpell.castUp(player, player.getItemInHand(hand));
                    replaceAndStartCircle(new WandUsingCircle(spellType, age+5).setRadius(radius));
                }
            }
        } else if (state == SpellCircleStatePacket.STATE_CANCEL_SPELL_CIRCLE) {
            stopCircle();
        }
    }
}
