package com.mega.revelationfix.common.compat.kjs.events;

import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import com.mega.revelationfix.common.data.ritual.requirement.DimensionTypeRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.PositionRequirement;
import com.mega.revelationfix.common.data.ritual.requirement.Requirement;
import com.mega.revelationfix.common.data.ritual.requirement.TimeRequirement;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.Collection;

public class RitualRegisterKjsEvent extends EventJS {
    public void register(String pluginID, RitualData ritualData) {
        RitualDataManager.register(pluginID, ritualData);
    }

    public RitualData getData(String id) {
        return RitualDataManager.getRitualByPlugin(id);
    }

    public Collection<RitualData> getExistData() {
        return RitualDataManager.getRituals();
    }

    public RitualData create(String plugin, String iconItem, Collection<Requirement> blockRequirements, Collection<Requirement> entityRequirements, DimensionTypeRequirement dimensionTypeRequirement, TimeRequirement timeRequirement, PositionRequirement positionRequirement) {
        RitualData ritualData = new RitualData(plugin);
        ritualData.setIconItemKey(iconItem);
        ritualData.setRequirements(RitualData.BLOCKS, blockRequirements);
        ritualData.setRequirements(RitualData.ENTITIES, entityRequirements);
        ritualData.setRequirements(RitualData.DIMENSION, dimensionTypeRequirement);
        ritualData.setRequirements(RitualData.TIME, timeRequirement);
        ritualData.setRequirements(RitualData.POSITION, positionRequirement);
        return ritualData;
    }
}
