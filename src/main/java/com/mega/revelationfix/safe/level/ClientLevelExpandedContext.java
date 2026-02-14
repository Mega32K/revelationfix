package com.mega.revelationfix.safe.level;

import com.Polarice3.Goety.common.blocks.DarkAltarBlock;
import com.Polarice3.Goety.common.blocks.ModBlocks;
import com.Polarice3.Goety.common.blocks.entities.DarkAltarBlockEntity;
import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.compat.Wrapped;
import com.mega.revelationfix.common.config.ClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.ModMain;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.function.BooleanSupplier;

public class ClientLevelExpandedContext extends LevelExpandedContext {
    public final ClientLevel clientLevel;
    public float oRainLevel;
    public float rainLevel;
    public Apostle currentNetherApollyon;
    public int tickCount = 0;
    public ResourceLocation PUZZLE1;
    public ResourceLocation PUZZLE2;
    public ResourceLocation PUZZLE3;
    public ResourceLocation PUZZLE4;
    public ResourceLocation TE_END_CRAFT;
    public BlockPos teEndRitualBE;
    public boolean teEndRitualRunning;

    public ClientLevelExpandedContext(ClientLevel clientLevel) {
        super(clientLevel);
        this.clientLevel = clientLevel;
    }

    public static ClientLevelExpandedContext get() {
        ClientLevel level = Minecraft.getInstance().level;
        if (level != null) {
            return ((ClientLevelInterface) level).revelationfix$ECData();
        } else throw new RuntimeException(new NullPointerException("ClientLevel is Null"));
    }

    public void tick(TickEvent.Phase phase) {
        boolean shouldRain = false;
        if (clientLevel.dimension() == Level.NETHER) {
            if (clientLevel.getDifficulty() != Difficulty.PEACEFUL) {
                this.oRainLevel = this.rainLevel;
                if (this.isNetherScarletRaining()) {
                    shouldRain = true;
                }
            }
        }
        if (clientLevel.dimension() == Level.END) {
            if (teEndRitualRunning && teEndRitualBE != null) {
                BlockState state;

                if ((state = clientLevel.getBlockState(teEndRitualBE)).getBlock() instanceof DarkAltarBlock) {
                    BlockEntity blockEntity = clientLevel.getBlockEntity(teEndRitualBE);
                    if (!(blockEntity instanceof DarkAltarBlockEntity darkAltar && darkAltar.getCurrentRitualRecipe() != null && darkAltar.getCurrentRitualRecipe().getId().equals(new ResourceLocation(ModMain.MODID, "the_end_ritual")))) {
                        teEndRitualRunning = false;
                        teEndRitualBE = null;
                    }
                } else {
                    BlockState s = clientLevel.getBlockState(teEndRitualBE);
                    teEndRitualRunning = false;
                    teEndRitualBE = null;

                }
            }
        } else {
            teEndRitualRunning = false;
            teEndRitualBE = null;
        }
        if (shouldRain) {
            this.rainLevel += 0.01F;
        } else {
            this.rainLevel -= 0.01F;
        }
        this.rainLevel = Mth.clamp(this.rainLevel, 0.0F, 1.0F);
        tickCount++;
        if (tickCount > 100) {
            tickCount = 0;
            Entity entity;
            //清除临时用于检测猩红雨的下界亚实体
            if (currentNetherApollyon != null && ((entity = Wrapped.getEntityByUUID(currentNetherApollyon.getUUID())) == null || entity.isRemoved())) {
                currentNetherApollyon = null;
            }
        }
    }

    @Override
    public void tickHead(BooleanSupplier booleanSupplier, CallbackInfo ci) {
    }

    public boolean isNetherScarletRaining() {
        return currentNetherApollyon != null && ((ApollyonAbilityHelper) currentNetherApollyon).allTitlesApostle_1_20_1$isApollyon() && currentNetherApollyon.isAddedToWorld() && currentNetherApollyon.isSecondPhase();
    }

    public boolean isNetherApollyon() {
        return currentNetherApollyon != null && ((ApollyonAbilityHelper) currentNetherApollyon).allTitlesApostle_1_20_1$isApollyon() && currentNetherApollyon.isAddedToWorld();
    }

    public boolean isScarletRaining() {
        if (!ClientConfig.enableScarletRain) return false;
        else return (double) this.getRainLevel(1.0F) > 0F;
    }

    public float getRainLevel(float partialTicks) {
        return Mth.lerp(partialTicks, this.oRainLevel, this.rainLevel);
    }
}
