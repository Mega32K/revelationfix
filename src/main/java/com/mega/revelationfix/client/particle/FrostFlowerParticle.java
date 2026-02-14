package com.mega.revelationfix.client.particle;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class FrostFlowerParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    static final float r0 = 0x8e / 255F;
    static final float g0 = 0xc5 / 255F;
    static final float b0 = 0xfc / 255F;
    static final float r1 = 0xb0 / 255F;
    static final float g1 = 0xe0 / 255F;
    static final float b1 = 0xe6 / 255F;
    int rand = (int) (Math.random() * 20) + 40;

    protected FrostFlowerParticle(ClientLevel p_172292_, double p_172293_, double p_172294_, double p_172295_, double p_172296_, double p_172297_, double p_172298_, SpriteSet p_172299_) {
        super(p_172292_, p_172293_, p_172294_, p_172295_);
        this.gravity = 0.225F;
        this.friction = 1.0F;
        this.sprites = p_172299_;
        this.xd = p_172296_ + (Math.random() * 2.0D - 1.0D) * (double) 0.05F;
        this.yd = p_172297_ + (Math.random() * 2.0D - 1.0D) * (double) 0.05F;
        this.zd = p_172298_ + (Math.random() * 2.0D - 1.0D) * (double) 0.05F;
        this.quadSize = 0.1F * (this.random.nextFloat() * this.random.nextFloat() * 1.0F + 1.0F);
        this.lifetime = (int) (16.0D / ((double) this.random.nextFloat() * 0.8D + 0.2D)) + 2;
        this.setSpriteFromAge(p_172299_);
    }

    public void tick() {
        super.tick();
        this.setSpriteFromAge(this.sprites);
        this.xd *= 0.95F;
        this.yd *= 0.9F;
        this.zd *= 0.95F;
    }

    @Override
    public void render(@NotNull VertexConsumer p_107678_, @NotNull Camera p_107679_, float partialTicks) {
        float partial = Mth.clamp((age + partialTicks) / 16F - 0.3F, 0F, 1.0F);
        float alphaMultiplier = Math.min(1F, (partialTicks + age) / 8F);
        rCol = Mth.clamp(partial, 0.69F, 0.55F);
        gCol = Mth.clamp(partial, 0.87F, 0.77F);
        bCol = Mth.clamp(partial, 0.90F, 0.98F);
        float[] offsets = new float[]{(float) Math.cos(age + partialTicks + rand) / 32F, (float) Math.sin(age + partialTicks + rand) / 32F, 0};

        {
            Vec3 vec3 = p_107679_.getPosition();
            float f = (float) (Mth.lerp(partialTicks, this.xo, this.x) - vec3.x()) + offsets[0];
            float f1 = (float) (Mth.lerp(partialTicks, this.yo, this.y) - vec3.y()) + offsets[1];
            float f2 = (float) (Mth.lerp(partialTicks, this.zo, this.z) - vec3.z()) + offsets[2];
            Quaternionf quaternionf;
            if (this.roll == 0.0F) {
                quaternionf = p_107679_.rotation();
            } else {
                quaternionf = new Quaternionf(p_107679_.rotation());
                quaternionf.rotateZ(Mth.lerp(partialTicks, this.oRoll, this.roll));
            }

            Vector3f[] avector3f = new Vector3f[]{new Vector3f(-1.0F, -1.0F, 0.0F), new Vector3f(-1.0F, 1.0F, 0.0F), new Vector3f(1.0F, 1.0F, 0.0F), new Vector3f(1.0F, -1.0F, 0.0F)};
            float f3 = this.getQuadSize(partialTicks);

            for (int i = 0; i < 4; ++i) {
                Vector3f vector3f = avector3f[i];
                vector3f.rotate(quaternionf);
                vector3f.mul(f3);
                vector3f.add(f, f1, f2);
            }

            float f6 = this.getU0();
            float f7 = this.getU1();
            float f4 = this.getV0();
            float f5 = this.getV1();
            int j = this.getLightColor(partialTicks);
            p_107678_.vertex(avector3f[0].x(), avector3f[0].y(), avector3f[0].z()).uv(f7, f5).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaMultiplier).uv2(j).endVertex();
            p_107678_.vertex(avector3f[1].x(), avector3f[1].y(), avector3f[1].z()).uv(f7, f4).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaMultiplier).uv2(j).endVertex();
            p_107678_.vertex(avector3f[2].x(), avector3f[2].y(), avector3f[2].z()).uv(f6, f4).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaMultiplier).uv2(j).endVertex();
            p_107678_.vertex(avector3f[3].x(), avector3f[3].y(), avector3f[3].z()).uv(f6, f5).color(this.rCol, this.gCol, this.bCol, this.alpha * alphaMultiplier).uv2(j).endVertex();

        }
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @OnlyIn(Dist.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet p_172304_) {
            this.sprites = p_172304_;
        }

        public Particle createParticle(@NotNull SimpleParticleType p_172315_, @NotNull ClientLevel p_172316_, double p_172317_, double p_172318_, double p_172319_, double p_172320_, double p_172321_, double p_172322_) {
            return new FrostFlowerParticle(p_172316_, p_172317_, p_172318_, p_172319_, p_172320_, p_172321_, p_172322_, this.sprites);
        }
    }
}
