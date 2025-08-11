package com.mega.revelationfix.common.data.ritual;

import com.Polarice3.Goety.common.blocks.entities.RitualBlockEntity;
import com.Polarice3.Goety.common.effects.brew.BrewEffects;
import com.Polarice3.Goety.common.ritual.RitualRequirements;
import com.mega.revelationfix.common.data.ritual.requirement.DimensionTypeRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.PositionRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.Requirement;
import com.mega.revelationfix.common.data.ritual.requirement.TimeRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.block.BlockRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.entity.EntityRequirement;
import com.mega.revelationfix.safe.mixinpart.goety.BrewEffectsInvoker;
import com.mega.revelationfix.util.RevelationFixMixinPlugin;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RitualDataManager {
    public static final Lock LOCK = new ReentrantLock();
    public static final FriendlyByteBuf.Writer<String> PLUGIN_NAME_WRITER = FriendlyByteBuf::writeUtf;
    public static final FriendlyByteBuf.Reader<String> PLUGIN_NAME_READER = FriendlyByteBuf::readUtf;
    public static final FriendlyByteBuf.Writer<RitualData> SIMPLE_RITUAL_DATA_WRITER = RitualDataWriter.getInstance();
    public static final FriendlyByteBuf.Reader<RitualData> SIMPLE_RITUAL_DATA_READER = RitualDataReader.getInstance();
    public static final Map<String, RitualData> REGISTRIES = new HashMap<>();
    public static Map<String, RitualData> getRegistries() {
        return REGISTRIES;
    }

    public static Collection<RitualData> getRituals() {
        return REGISTRIES.values();
    }
    public static void register(String plugin, RitualData data) {
        REGISTRIES.put(plugin, data);
        {
            Logger logger = RevelationFixMixinPlugin.LOGGER;
            BrewEffectsInvoker invoker = (BrewEffectsInvoker) BrewEffects.INSTANCE;
            logger.debug("Goety custom rituals registering Plugin: {}", plugin);
            logger.debug(" -Ritual:{}", plugin);
            logger.debug(" -IconItem:{}", data.getIconItem());
            logger.debug(" -Range:{}", data.getRange());
            logger.debug(" -Requirements: ");
            logger.debug("   -Blocks: size->{} ", data.requirements.get(RitualData.BLOCKS).size());
            logger.debug("   -Entities: size->{} ", data.requirements.get(RitualData.ENTITIES).size());
        }
    }
    public static @Nullable RitualData getRitualByPlugin(String plugin) {
        return REGISTRIES.get(plugin);
    }
    public static void clearData()  {
        REGISTRIES.clear();
    }
    public static boolean isCustomRitual(String craftType) {
        return REGISTRIES.containsKey(craftType);
    }
    public static boolean getProperStructure(String craftType, RitualBlockEntity pTileEntity, BlockPos pPos, Level pLevel) {
        RitualData ritualData = REGISTRIES.get(craftType);
        {
            Set<Requirement> requirements = ritualData.requirements.get(RitualData.DIMENSION);
            if (!requirements.isEmpty()) {
                for (Requirement requirement : requirements) {
                    if (requirement instanceof DimensionTypeRequirement dimensionTypeRequirement && !dimensionTypeRequirement.canUse(pLevel)) {
                        return false;
                    }
                }
            }
        }
        {
            Set<Requirement> requirements = ritualData.requirements.get(RitualData.TIME);
            if (!requirements.isEmpty()) {
                for (Requirement requirement : requirements) {
                    if (requirement instanceof TimeRequirement timeRequirement && !timeRequirement.canUse(pLevel)) {
                        return false;
                    }
                }
            }
        }
        {
            Set<Requirement> requirements = ritualData.requirements.get(RitualData.POSITION);
            if (!requirements.isEmpty()) {
                for (Requirement requirement : requirements) {
                    if (requirement instanceof PositionRequirement positionRequirement && !positionRequirement.canUse(pLevel, pPos)) {
                        return false;
                    }
                }
            }
        }
        Vec3i range = ritualData.getRange();
        int xRange = range.getX();
        int yRange = range.getY();
        int zRange = range.getZ();
        if (xRange < 0 || yRange < 0 || zRange < 0)
            xRange = yRange = zRange = RitualRequirements.RANGE;
        Set<Requirement> blockRequirements = ritualData.requirements.get(RitualData.BLOCKS);
        Set<Requirement> entityRequirements = ritualData.requirements.get(RitualData.ENTITIES);
        int blockLength = blockRequirements.size();
        int entityLength = entityRequirements.size();
        if (blockLength > 0) {
            Map<BlockRequirement, Integer> map = new HashMap<>();
            for (Requirement requirement : blockRequirements) {
                if (requirement instanceof BlockRequirement br)
                    map.put(br, 0);
            }
            for (int i = -xRange; i <= xRange; ++i) {
                for (int j = -yRange; j <= yRange; ++j) {
                    for (int k = -zRange; k <= zRange; ++k) {
                        BlockPos blockpos1 = pPos.offset(i, j, k);
                        BlockState blockstate = pLevel.getBlockState(blockpos1);
                        for (BlockRequirement requirement : map.keySet()) {
                            if (requirement.canUse(pLevel, blockpos1, blockstate)) {
                                map.put(requirement, map.get(requirement)+1);
                            }
                        }
                    }
                }
            }
            for (var entryToCheck : map.entrySet()) {
                if (entryToCheck.getKey().getRequiredCount() > entryToCheck.getValue())
                    return false;
            }
        }
        if (entityLength > 0) {
            Map<EntityRequirement, Integer> map = new HashMap<>();
            for (Requirement requirement : entityRequirements) {
                if (requirement instanceof EntityRequirement er)
                    map.put(er, 0);
            }
            for (Entity entity : pLevel.getEntitiesOfClass(Entity.class, new AABB(pPos).inflate(xRange, yRange, zRange))) {
                for (EntityRequirement requirement : map.keySet()) {
                    if (requirement.canUseRequirement(pLevel, entity))
                        map.put(requirement, map.get(requirement)+1);
                }
            }

            for (var entryToCheck : map.entrySet()) {
                if (entryToCheck.getKey().getRequiredCount() > entryToCheck.getValue()) {
                    return false;
                }
            }
        }
        return true;
    }
}
