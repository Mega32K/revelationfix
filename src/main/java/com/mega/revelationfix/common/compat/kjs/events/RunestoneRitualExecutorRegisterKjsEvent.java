package com.mega.revelationfix.common.compat.kjs.events;

import com.mega.revelationfix.common.init.RunestoneRitualExe;
import com.mega.revelationfix.common.init.RunestoneRitualInit;
import dev.latvian.mods.kubejs.event.EventJS;

import java.util.function.Consumer;

public class RunestoneRitualExecutorRegisterKjsEvent extends EventJS {

    public RunestoneRitualExecutorRegisterKjsEvent() {
    }
    public void register(int simpleCode, RunestoneRitualExe exe, Consumer<Throwable> caughtException) {
        try {
            RunestoneRitualInit.register(simpleCode, exe);
        } catch (Throwable throwable) {
            caughtException.accept(throwable);
        }
    }
    public void register(int simpleCode, RunestoneRitualExe exe) {
        RunestoneRitualInit.register(simpleCode, exe);
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
