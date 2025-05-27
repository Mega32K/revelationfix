package com.mega.revelationfix.common.item;

import com.Polarice3.Goety.api.entities.IOwned;
import com.Polarice3.Goety.common.items.ServantSpawnEggItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

public class AllRightsServantEgg extends ServantSpawnEggItem {
    public AllRightsServantEgg(RegistryObject<? extends EntityType<? extends Mob>> entityTypeSupplier, int primaryColorIn, int secondaryColorIn, Properties builder) {
        super(entityTypeSupplier, primaryColorIn, secondaryColorIn, builder);
    }

    @Override
    public InteractionResult useOn(UseOnContext p_43223_) {

        Level level = p_43223_.getLevel();
        Player player = p_43223_.getPlayer();
        if (!(level instanceof ServerLevel serverLevel)) {
            return InteractionResult.SUCCESS;
        } else {
            ItemStack itemstack = p_43223_.getItemInHand();
            BlockPos blockpos = p_43223_.getClickedPos();
            Direction direction = p_43223_.getClickedFace();
            BlockState blockstate = level.getBlockState(blockpos);
            if (blockstate.is(Blocks.SPAWNER)) {
                BlockEntity blockentity = level.getBlockEntity(blockpos);
                if (blockentity instanceof SpawnerBlockEntity spawnerblockentity) {
                    EntityType<?> entitytype1 = this.getType(itemstack.getTag());
                    spawnerblockentity.setEntityId(entitytype1, level.getRandom());
                    blockentity.setChanged();
                    level.sendBlockUpdated(blockpos, blockstate, blockstate, 3);
                    level.gameEvent(p_43223_.getPlayer(), GameEvent.BLOCK_CHANGE, blockpos);
                    itemstack.shrink(1);
                    return InteractionResult.CONSUME;
                }
            }

            BlockPos blockpos1;
            if (blockstate.getCollisionShape(level, blockpos).isEmpty()) {
                blockpos1 = blockpos;
            } else {
                blockpos1 = blockpos.relative(direction);
            }

            EntityType<?> entitytype = this.getType(itemstack.getTag());
            Entity entity = entitytype.spawn(serverLevel, itemstack, p_43223_.getPlayer(), blockpos1, MobSpawnType.SPAWN_EGG, true, !Objects.equals(blockpos, blockpos1) && direction == Direction.UP);
            if (entity != null) {
                if (player != null && entity instanceof IOwned owned) {
                    if (player.isCrouching()) {
                        owned.setTrueOwner(player);
                        if (owned instanceof Mob mob) {
                            ForgeEventFactory.onFinalizeSpawn(mob, serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.SPAWN_EGG, null, itemstack.getTag());
                        }
                    }
                }

                itemstack.shrink(1);
                level.gameEvent(p_43223_.getPlayer(), GameEvent.ENTITY_PLACE, blockpos);
            }

            return InteractionResult.CONSUME;
        }
    }
}
