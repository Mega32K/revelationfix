package com.mega.revelationfix.client.screen.post.custom;

import com.mega.endinglib.api.client.Easing;
import com.mega.endinglib.mixin.accessor.AccessorPostChain;
import com.mega.endinglib.util.mc.client.ClientUtils;
import com.mega.revelationfix.Revelationfix;
import com.mega.revelationfix.client.screen.CustomScreenEffect;
import com.mega.revelationfix.client.screen.post.PostEffectHandler;
import com.mega.revelationfix.safe.level.ClientLevelInterface;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector4f;
import z1gned.goetyrevelation.ModMain;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@OnlyIn(Dist.CLIENT)
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class TheEndEffect implements CustomScreenEffect {
    public static ResourceLocation LICHEN = new ResourceLocation(ModMain.MODID, "textures/ui/lichen.png");
    public static ResourceLocation FADE = new ResourceLocation(ModMain.MODID, "textures/ui/fade.png");
    static Minecraft mc = Minecraft.getInstance();
    static int tickCount = 0;
    static int tickCountO = 0;
    static float rotationStar;
    static GuiGraphics guiGraphics;
    static TheEndEffect INSTANCE;
    float saturation = 1.0F;
    private float lastStamp;
    private float time;

    public TheEndEffect() {
        TheEndEffect.INSTANCE = this;
    }

    @SubscribeEvent
    public static void clientProgramTick(TickEvent.ClientTickEvent event) {
        LocalPlayer player = mc.player;
        if (player == null || event.phase == TickEvent.Phase.END)
            return;
        tickCountO = tickCount;
        if (AberrationDistortionPostEffect.INSTANCE.canUse()) {
            if (tickCount < 25)
                tickCount++;
        } else if (tickCount > 0) tickCount--;
    }

    @Override
    public String getName() {
        return "the_end";
    }

    @Override
    public ResourceLocation getShaderLocation() {
        return new ResourceLocation(Revelationfix.MODID, "shaders/post/movie.json");
    }

    @Override
    public void onRenderTick(float partialTicks) {
        if (partialTicks < this.lastStamp) {
            this.time += 1.0F - this.lastStamp;
            this.time += partialTicks;
        } else {
            this.time += partialTicks - this.lastStamp;
        }
        lastStamp = partialTicks;
        float percent = Mth.lerp(partialTicks, TheEndEffect.tickCountO, TheEndEffect.tickCount) / 25F;
        PostEffectHandler.updateUniform_post(this, "SeriousTotalTime", time * 0.05F);
        PostEffectHandler.updateUniform_post(this, "Percent", Easing.OUT_CUBIC.calculate(percent));
        ((AccessorPostChain) this.current()).getPasses().get(0).getEffect().setSampler("LichenSampler", () -> mc.textureManager.getTexture(LICHEN).getId());
        ((AccessorPostChain) this.current()).getPasses().get(0).getEffect().setSampler("FadeSampler", () -> mc.textureManager.getTexture(FADE).getId());

        if (mc.level != null) {
            BlockPos pos = ((ClientLevelInterface) mc.level).revelationfix$ECData().teEndRitualBE;
            if (pos != null) {
                Vec3 cameraPos = mc.gameRenderer.getMainCamera().getPosition();
                Matrix4f transformMat = new Matrix4f(ClientUtils.LEVEL_PROJ_MAT).mul(ClientUtils.LEVEL_MODEL_VIEW_MAT);
                Set<Vec3> toTrans = new ObjectOpenHashSet<>();
                float step = 0.4F;
                toTrans.add(pos.getCenter().add(0F, step + 0.25F, 0F).add(cameraPos.scale(-1)));
                toTrans.add(pos.getCenter().add(0F, -step + 0.25F, 0F).add(cameraPos.scale(-1)));
                toTrans.add(pos.getCenter().add(step, 0F + 0.25F, 0F).add(cameraPos.scale(-1)));
                toTrans.add(pos.getCenter().add(-step, 0F + 0.25F, 0F).add(cameraPos.scale(-1)));
                toTrans.add(pos.getCenter().add(0F, 0F + 0.25F, step).add(cameraPos.scale(-1)));
                toTrans.add(pos.getCenter().add(0F, 0F + 0.25F, -step).add(cameraPos.scale(-1)));
                List<Vec2> transformed = new ObjectArrayList<>(toTrans.stream().map(p -> {
                    Vector4f vector4f = transformMat.transform(new Vector4f(p.toVector3f(), 0F));
                    return new Vec2((vector4f.x / vector4f.z + 1F) / 2F, (vector4f.y / vector4f.z + 1F) / 2F);
                }).toList());
                float distance = 0F;
                double[] x = limitX(transformed);
                double[] y = limitY(transformed);
                distance = new Vector2f((float) x[0], (float) y[0]).distance(new Vector2f((float) x[1], (float) y[1]));
                Vector4f center = transformMat.transform(new Vector4f(pos.getCenter().add(0F,  0.25F, 0F).add(cameraPos.scale(-1)).toVector3f(), 0F));

                PostEffectHandler.updateUniform_post(this, "Center", new float[]{(center.x / center.z + 1F) / 2F, (center.y / center.z + 1F) / 2F, center.z});
                PostEffectHandler.updateUniform_post(this, "Distance", distance);
            }
        }
    }

    double[] limitX(Collection<Vec2> vec2s) {
        double min = Double.NaN;
        double max = Double.NaN;
        for (Vec2 vec2 : vec2s) {
            if (Double.isNaN(min) || Double.isNaN(max)) {
                min = max = vec2.x;
            } else {
                if (min >= vec2.x) {
                    min = vec2.x;
                }
                if (max <= vec2.x) {
                    max = vec2.x;
                }
            }
        }
        return new double[]{min, max};
    }

    double[] limitY(Collection<Vec2> vec2s) {
        double min = Double.NaN;
        double max = Double.NaN;
        for (Vec2 vec2 : vec2s) {
            if (Double.isNaN(min) || Double.isNaN(max)) {
                min = max = vec2.y;
            } else {
                if (min >= vec2.y) {
                    min = vec2.y;
                }
                if (max <= vec2.y) {
                    max = vec2.y;
                }
            }
        }
        return new double[]{min, max};
    }

    @Override
    public boolean canUse() {
        return tickCount > 0 || tickCountO > 0;
    }
}
