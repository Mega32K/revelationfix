package com.mega.revelationfix.client.levelevent;

import com.Polarice3.Goety.client.particles.CircleExplodeParticleOption;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.network.PacketClientProxy;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;

public class LevelEventManager {
    public static Int2ObjectOpenHashMap<ILevelEvent> registries = new Int2ObjectOpenHashMap<>();
    static {
        registries.put(232424314, (pos, rand, i) -> {
            Level level = Minecraft.getInstance().level;
            if (level == null) return;
            switch ((byte) i) {
                case 0 -> {
                    Minecraft.getInstance().level.playLocalSound(pos, SoundEvents.AMETHYST_BLOCK_STEP, SoundSource.BLOCKS, 1.0F, 0.5F + rand.nextFloat() * 1.2F, false);
                    level.playLocalSound(pos, SoundEvents.AMETHYST_BLOCK_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F + rand.nextFloat() * 1.2F, false);
                }
                case 1 -> level.playLocalSound(pos, SoundEvents.BEACON_DEACTIVATE, SoundSource.BLOCKS, 1.0F, 0.5F + rand.nextFloat(), false);
                case 2 -> PacketClientProxy.doFrostParticles(pos.getX() + 0.5D, pos.getY() + 0.4D, pos.getZ() + 0.5D);
                case 3 -> level.addParticle(new CircleExplodeParticleOption(.70F, .45F, .95F, 3F, 1), pos.getX() + RuneReactorBlock.BASE_SHAPE.bounds().getXsize() / 2F, pos.getY() + RuneReactorBlock.BASE_SHAPE.bounds().getYsize() / 2F, pos.getZ() + RuneReactorBlock.BASE_SHAPE.bounds().getZsize() / 2F, 0, 0, 0);
                case 4 -> {
                    double x = pos.getX() + RuneReactorBlock.BASE_SHAPE.bounds().getXsize() / 2F;
                    double z = pos.getZ() + RuneReactorBlock.BASE_SHAPE.bounds().getZsize() / 2F;
                    PacketClientProxy.doFrostParticles(x, pos.getY() + RuneReactorBlock.BASE_SHAPE.bounds().getYsize() / 2F, z);
                    for (int j = 0; j < 16; j++) {
                        level.addParticle(com.Polarice3.Goety.client.particles.ModParticleTypes.FROST_NOVA.get(), x, pos.getY() + j / 2F + RuneReactorBlock.BASE_SHAPE.bounds().getYsize() / 2F, z, 0F, 0F, 0F);
                    }
                }
                case 5 -> level.addParticle(new CircleExplodeParticleOption(.47F, .41F, .32F, 2F, 2), pos.getX() + .5F, pos.getY() + 0.2F, pos.getZ() + .5F, 0, 0.02, 0);
                case 6 -> {
                    pos = pos.above();
                    BlockState state = Blocks.GLASS.defaultBlockState();
                    SoundType soundtype = state.getSoundType(level, pos, null);
                    level.playLocalSound(pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.7F + rand.nextFloat() * 0.2F, false);
                    level.addDestroyBlockEffect(pos, state);
                }
                case 7 -> level.playLocalSound(pos, SoundEvents.WARDEN_ATTACK_IMPACT, SoundSource.PLAYERS, 1.0F, 1.0F, false);
            }
        });
    }
    public static void registerLevelEvent(int id, ILevelEvent clientRun) {
        if (registries.containsKey(id))
            throw new RuntimeException("Duplicate level event: " + id);
        registries.put(id, clientRun);
    }
    public static void onReceive(int id, RandomSource source, BlockPos pos, int iArg) {
        if (registries.containsKey(id))
            registries.get(id).run(pos, source, iArg);
    }
}
