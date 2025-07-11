package com.mega.revelationfix.mixin;

import com.mega.endinglib.util.annotation.DeprecatedMixin;
import com.mega.revelationfix.common.structures.church.ChurchPlacement;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ColumnFeatureConfiguration;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(Feature.class)
public class FeatureFixMixin {

    @Shadow @Final public static Feature<ColumnFeatureConfiguration> BASALT_COLUMNS;

    @Redirect(method = "place(Lnet/minecraft/world/level/levelgen/feature/configurations/FeatureConfiguration;Lnet/minecraft/world/level/WorldGenLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/WorldGenLevel;ensureCanWrite(Lnet/minecraft/core/BlockPos;)Z"))
    private boolean fixNetherBugFeatureSpawning(WorldGenLevel instance, BlockPos p_181157_) {
        if (instance.getLevel().dimension() == Level.NETHER) {
           /*
            int i = SectionPos.blockToSectionCoord(p_181157_.getX());
            int j = SectionPos.blockToSectionCoord(p_181157_.getZ());
            AtomicBoolean should = new AtomicBoolean(false);
            ChurchPlacement.map.forEach((i1, i2) -> {
                if (Math.abs(i-i1) < 5 && (Math.abs(j-i2.x) < 5 || Math.abs(j-i2.y) < 5))
                    should.set(true);
            });
            if (should.get())
                return false;
            */
            if (p_181157_.getY() >= 117 && ChurchPlacement.mayInChurch(p_181157_))
                return false;
        }
        return instance.ensureCanWrite(p_181157_);
    }
}
