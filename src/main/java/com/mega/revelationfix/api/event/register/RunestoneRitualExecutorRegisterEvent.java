package com.mega.revelationfix.api.event.register;

import com.mega.revelationfix.common.init.RunestoneRitualExe;
import com.mega.revelationfix.common.init.RunestoneRitualInit;
import net.minecraftforge.eventbus.api.Event;

import java.util.function.Consumer;

public class RunestoneRitualExecutorRegisterEvent extends Event {
    public RunestoneRitualExecutorRegisterEvent() {
    }

    public void register(int simpleCode, RunestoneRitualExe exe, Consumer<Throwable> caughtException) {
        try {
            RunestoneRitualInit.register(simpleCode, exe);
        } catch (Throwable throwable) {
            caughtException.accept(throwable);
        }
    }

    public void replaceExe(int simpleCode, RunestoneRitualExe exe) {
        if (RunestoneRitualInit.registries.containsKey(simpleCode))
            RunestoneRitualInit.registries.put(simpleCode, exe);
    }

    public void removeExe(int simpleCode) {
        if (RunestoneRitualInit.registries.containsKey(simpleCode))
            RunestoneRitualInit.registries.remove(simpleCode);
    }
}
