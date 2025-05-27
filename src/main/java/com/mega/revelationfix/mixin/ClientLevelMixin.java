package com.mega.revelationfix.mixin;

import com.mega.revelationfix.safe.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.ClientLevelInterface;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.storage.WritableLevelData;
import net.minecraftforge.event.TickEvent;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level implements ClientLevelInterface {
    @Unique
    private ClientLevelExpandedContext ecContext;

    ClientLevelMixin(WritableLevelData p_270739_, ResourceKey<Level> p_270683_, RegistryAccess p_270200_, Holder<DimensionType> p_270240_, Supplier<ProfilerFiller> p_270692_, boolean p_270904_, boolean p_270470_, long p_270248_, int p_270466_) {
        super(p_270739_, p_270683_, p_270200_, p_270240_, p_270692_, p_270904_, p_270470_, p_270248_, p_270466_);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void initData(ClientPacketListener p_205505_, ClientLevel.ClientLevelData p_205506_, ResourceKey p_205507_, Holder p_205508_, int p_205509_, int p_205510_, Supplier p_205511_, LevelRenderer p_205512_, boolean p_205513_, long p_205514_, CallbackInfo ci) {
        this.revelationfix$setECData(new ClientLevelExpandedContext((ClientLevel) (Object) this));
    }

    @Override
    public @NotNull ClientLevelExpandedContext revelationfix$ECData() {
        return this.ecContext;
    }

    @Override
    public void revelationfix$setECData(ClientLevelExpandedContext data) {
        this.ecContext = data;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickHead(BooleanSupplier p_104727_, CallbackInfo ci) {
        this.ecContext.tick(TickEvent.Phase.START);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void tickTail(BooleanSupplier p_104727_, CallbackInfo ci) {
        this.ecContext.tick(TickEvent.Phase.END);
    }
}
