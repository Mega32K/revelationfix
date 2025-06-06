package com.mega.revelationfix.api.event.register;

import com.mega.revelationfix.common.data.ritual.RitualData;
import com.mega.revelationfix.common.data.ritual.RitualDataManager;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collection;

public class CustomRitualRegisterEvent extends Event {
    public CustomRitualRegisterEvent() {
    }


    public void register(String pluginID, RitualData ritualData) {
        RitualDataManager.register(pluginID, ritualData);
    }

    public RitualData getData(String id) {
        return RitualDataManager.getRitualByPlugin(id);
    }

    public Collection<RitualData> getExistData() {
        return RitualDataManager.getRituals();
    }
}