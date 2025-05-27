package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.mega.revelationfix.safe.LevelEC;
import com.mega.revelationfix.safe.LevelExpandedContext;
import com.mega.revelationfix.safe.NoModDependsMixin;
import com.mega.revelationfix.safe.ServerLevelExpandedContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.RandomSequences;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.entity.EntityTickList;
import net.minecraft.world.level.entity.LevelEntityGetter;
import net.minecraft.world.level.entity.PersistentEntitySectionManager;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraft.world.level.storage.WritableLevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ServerLevel.class)
@NoModDependsMixin("fantasy_ending")
public abstract class ServerLevelMixin extends Level implements LevelEC {
    @Shadow
    public
    EntityTickList entityTickList;
    @Shadow
    public ServerChunkCache chunkSource;
    @Shadow
    public PersistentEntitySectionManager<Entity> entityManager;
    @Unique
    private ServerLevelExpandedContext uom$serverEC;

    ServerLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);

    }

    @Shadow
    public abstract boolean shouldDiscardEntity(Entity p_143343_);

    @Shadow
    public abstract void tickNonPassenger(Entity p_8648_);

    @Shadow
    @Nullable
    public abstract Entity getEntity(UUID p_8792_);

    @Shadow
    public abstract LevelEntityGetter<Entity> getEntities();

    @Shadow
    @Nullable
    public abstract Entity getEntity(int p_8597_);

    @Shadow
    @Nullable
    public abstract ServerPlayer getRandomPlayer();

    @Shadow
    public abstract List<ServerPlayer> players();

    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(MinecraftServer p_214999_, Executor p_215000_, LevelStorageSource.LevelStorageAccess p_215001_, ServerLevelData p_215002_, ResourceKey p_215003_, LevelStem p_215004_, ChunkProgressListener p_215005_, boolean p_215006_, long p_215007_, List p_215008_, boolean p_215009_, RandomSequences p_288977_, CallbackInfo ci) {
        this.uom$setECData(new ServerLevelExpandedContext(this));
    }

    @Override
    public LevelExpandedContext uom$levelECData() {
        return this.uom$serverEC;
    }

    @Override
    public void uom$setECData(LevelExpandedContext data) {
        this.uom$serverEC = (ServerLevelExpandedContext) data;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void tick(BooleanSupplier p_8794_, CallbackInfo ci) {
        this.uom$levelECData().tickHead(p_8794_, ci);
    }

    @Inject(method = "tickCustomSpawners", at = @At("HEAD"), cancellable = true)
    private void tickCustomSpawners(boolean p_8800_, boolean p_8801_, CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(this)) ci.cancel();
    }

    @Inject(method = "tickTime", at = @At("HEAD"), cancellable = true)
    private void tickTime(CallbackInfo ci) {
        if (TimeStopUtils.isTimeStop && TimeStopUtils.andSameDimension(this)) ci.cancel();
    }
}
