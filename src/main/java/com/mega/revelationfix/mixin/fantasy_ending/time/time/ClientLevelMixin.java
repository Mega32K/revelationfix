package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.client.RendererUtils;
import com.mega.revelationfix.safe.level.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.level.LevelEC;
import com.mega.revelationfix.safe.level.LevelExpandedContext;
import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.TransientEntitySectionManager;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
@NoModDependsMixin("fantasy_ending")
public abstract class ClientLevelMixin extends Level implements LevelEC {
    @Shadow
    public EntityTickList tickingEntities;
    @Shadow
    public TransientEntitySectionManager<Entity> entityStorage;
    @Unique
    private ClientLevelExpandedContext uom$clientEC;

    ClientLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Shadow
    public abstract void tickNonPassenger(Entity p_104640_);

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(ClientPacketListener p_205505_, ClientLevel.ClientLevelData p_205506_, ResourceKey p_205507_, Holder p_205508_, int p_205509_, int p_205510_, Supplier p_205511_, LevelRenderer p_205512_, boolean p_205513_, long p_205514_, CallbackInfo ci) {
        this.uom$setECData(new ClientLevelExpandedContext((ClientLevel) (Object) this));
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(BooleanSupplier p_104727_, CallbackInfo ci) {
        this.uom$levelECData().tickHead(p_104727_, ci);
    }

    @Inject(method = "animateTick", at = @At("HEAD"), cancellable = true)
    private void animateTick(int p_104785_, int p_104786_, int p_104787_, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop
                && RendererUtils.isTimeStop_andSameDimension) ci.cancel();
    }

    @Inject(method = "tickTime", at = @At("HEAD"), cancellable = true)
    private void tickTime(CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop
                && RendererUtils.isTimeStop_andSameDimension) ci.cancel();
    }

    @Inject(method = "setGameTime", at = @At("HEAD"), cancellable = true)
    private void setGameTime(long p_104638_, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop
                && RendererUtils.isTimeStop_andSameDimension) {
            if (p_104638_ - levelData.getGameTime() > 0) {
                ci.cancel();
            }
        }
    }

    @Override
    public LevelExpandedContext uom$levelECData() {
        return uom$clientEC;
    }

    @Override
    public void uom$setECData(LevelExpandedContext data) {
        this.uom$clientEC = (ClientLevelExpandedContext) data;
    }
}
