package com.mega.revelationfix.common.compat.kjs;

import com.mega.revelationfix.api.event.register.CustomBrewRegisterEvent;
import com.mega.revelationfix.common.compat.kjs.events.KjsEvents;
import com.mega.revelationfix.common.compat.kjs.events.RitualRegisterKjsEvent;
import com.mega.revelationfix.common.compat.kjs.events.RunestoneRitualExecutorRegisterKjsEvent;
import com.mega.revelationfix.common.compat.kjs.events.WitchBrewRegisterKjsEvent;

public class KjsSafeClass {
    public static void postBrewEvent_0() {
        KjsEvents.CUSTOM_BREW_REGISTER.post(new WitchBrewRegisterKjsEvent(CustomBrewRegisterEvent.Phase.CHECK));
    }

    public static void postRunestoneEvent_0() {
        KjsEvents.RUNESTONE_RITUAL_EXECUTOR_REGISTER.post(new RunestoneRitualExecutorRegisterKjsEvent());
    }

    public static void postRitualEvent_0() {
        KjsEvents.CUSTOM_RITUAL_REGISTER.post(new RitualRegisterKjsEvent());
    }
}
