package com.mega.revelationfix.api.event.register;

import com.mega.revelationfix.common.data.brew.BrewData;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.EventPriority;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class CustomBrewRegisterEvent extends Event {
    private final Phase phase;
    public enum Phase {
        DATA_LOAD,CHECK
    }
    public CustomBrewRegisterEvent(Phase phase) {
        this.phase = phase;
    }

    @Nullable
    public CustomBrewRegisterEvent.Phase getRegisterPhase() {
        return phase;
    }

    public void register(String pluginID, BrewData mainData) {
        BrewData.register(pluginID, mainData);
    }
    public BrewData getData(String id) {
        return BrewData.getValue(id);
    }
    public Collection<BrewData> getExistData() {
        return BrewData.allData();
    }
}
