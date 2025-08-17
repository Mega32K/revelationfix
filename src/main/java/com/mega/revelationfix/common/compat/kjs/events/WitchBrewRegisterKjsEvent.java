package com.mega.revelationfix.common.compat.kjs.events;

import com.mega.revelationfix.api.event.register.CustomBrewRegisterEvent;
import com.mega.revelationfix.common.data.brew.BrewData;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.Collection;
import java.util.List;

public class WitchBrewRegisterKjsEvent extends EventJS {
    private final CustomBrewRegisterEvent.Phase phase;

    public WitchBrewRegisterKjsEvent(CustomBrewRegisterEvent.Phase phase) {
        this.phase = phase;
    }

    public CustomBrewRegisterEvent.Phase getRegisterPhase() {
        return phase;
    }

    public void register(String pluginID, BrewData mainData) {
        BrewData.register(pluginID, mainData);
    }

    public BrewData getData(String id) {
        return BrewData.getValue(id);
    }

    public BrewData create(String plugin, List<BrewData.Capacity> capacities, List<BrewData.Catalysts<?>> catalysts, List<BrewData.Augmentation> augmentations) {
        BrewData data = new BrewData();
        data.pluginName = plugin;
        data.catalysts = catalysts;
        data.capacities = capacities;
        data.augmentations = augmentations;
        return data;
    }

    public Collection<BrewData> getExistData() {
        return BrewData.allData();
    }

    public enum Phase {
        DATA_LOAD, CHECK
    }
}
