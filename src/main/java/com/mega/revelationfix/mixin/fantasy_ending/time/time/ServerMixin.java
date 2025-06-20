package com.mega.revelationfix.mixin.fantasy_ending.time.time;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mega.revelationfix.common.data.MinecraftServerReloadTracker;
import com.mega.revelationfix.safe.mixinpart.NoModDependsMixin;
import com.mega.revelationfix.safe.level.ServerEC;
import com.mega.revelationfix.safe.level.ServerExpandedContext;
import com.mojang.datafixers.DataFixer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.net.Proxy;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BooleanSupplier;
@Mixin(MinecraftServer.class)
@NoModDependsMixin("fantasy_ending")
public abstract class ServerMixin implements ServerEC {
    @Unique
    private ServerExpandedContext serverEC;
    @Shadow
    public abstract int getPlayerCount();
    @Shadow
    public abstract Iterable<ServerLevel> getAllLevels();
    @Inject(method = "<init>", at = @At("RETURN"))
    private void init(Thread p_236723_, LevelStorageSource.LevelStorageAccess p_236724_, PackRepository p_236725_, WorldStem p_236726_, Proxy p_236727_, DataFixer p_236728_, Services p_236729_, ChunkProgressListenerFactory p_236730_, CallbackInfo ci) {
        this.uom$setECData(new ServerExpandedContext((MinecraftServer) (Object) this));
    }
    @Inject(method = "tickServer", at = @At("HEAD"))
    private void tickServer(BooleanSupplier p_129871_, CallbackInfo ci) {
        this.uom$serverECData().update();
    }
    @Inject(method = "reloadResources", at = @At("HEAD"))
    private void startReloadTrack(Collection<String> selectedIds, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        MinecraftServerReloadTracker.ACTIVE_RELOADS++;
    }
    @ModifyExpressionValue(method = "reloadResources", at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;thenAcceptAsync(Ljava/util/function/Consumer;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;", ordinal = 0))
    private CompletableFuture<Void> mfix$endReloadTrack(CompletableFuture<Void> original) {
        return original.thenAcceptAsync(val -> {
            MinecraftServerReloadTracker.ACTIVE_RELOADS--;
        }, (Executor) this);
    }
    @Override
    public ServerExpandedContext uom$serverECData() {
        return this.serverEC;
    }
    @Override
    public void uom$setECData(ServerExpandedContext data) {
        this.serverEC = data;
    }
}
