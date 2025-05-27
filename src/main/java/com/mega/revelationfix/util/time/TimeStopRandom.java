package com.mega.revelationfix.util.time;

import com.google.common.annotations.VisibleForTesting;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.ThreadingDetector;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.MarsagliaPolarGaussian;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

import java.util.concurrent.atomic.AtomicLong;

public class TimeStopRandom extends LegacyRandomSource {
    private final AtomicLong seed = new AtomicLong();
    private final MarsagliaPolarGaussian gaussianSource = new MarsagliaPolarGaussian(this);

    public TimeStopRandom(long p_188578_) {
        super(p_188578_);
    }

    public RandomSource fork() {
        return new LegacyRandomSource(this.nextLong());
    }

    public PositionalRandomFactory forkPositional() {
        return new LegacyRandomSource.LegacyPositionalRandomFactory(this.nextLong());
    }

    public int next(int p_188581_) {
        long i = this.seed.get();
        long j = i * 25214903917L + 11L & 281474976710655L;
        if (TimeStopUtils.isTimeStop) {
            return (int) (j >> 48 - p_188581_);
        } else {
            if (!this.seed.compareAndSet(i, j)) {
                throw ThreadingDetector.makeThreadingException("LegacyRandomSource", null);
            } else {
                return (int) (j >> 48 - p_188581_);
            }
        }
    }

    public double nextGaussian() {
        return this.gaussianSource.nextGaussian();
    }

    public static class LegacyPositionalRandomFactory implements PositionalRandomFactory {
        private final long seed;

        public LegacyPositionalRandomFactory(long p_188588_) {
            this.seed = p_188588_;
        }

        public RandomSource at(int p_224198_, int p_224199_, int p_224200_) {
            long i = Mth.getSeed(p_224198_, p_224199_, p_224200_);
            long j = i ^ this.seed;
            return new LegacyRandomSource(j);
        }

        public RandomSource fromHashOf(String p_224202_) {
            int i = p_224202_.hashCode();
            return new LegacyRandomSource((long) i ^ this.seed);
        }

        @VisibleForTesting
        public void parityConfigString(StringBuilder p_188596_) {
            p_188596_.append("LegacyPositionalRandomFactory{").append(this.seed).append("}");
        }
    }
}
