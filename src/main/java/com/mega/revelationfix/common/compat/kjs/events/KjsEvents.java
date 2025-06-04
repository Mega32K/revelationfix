package com.mega.revelationfix.common.compat.kjs.events;

import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface KjsEvents {
    EventGroup GROUP = EventGroup.of("GoetyRevelationKJSEvents");
    EventHandler CUSTOM_BREW_REGISTER = GROUP.server("witch_brew_register", ()-> WitchBrewRegisterKjsEvent.class);
    EventHandler RUNESTONE_RITUAL_EXECUTOR_REGISTER = GROUP.server("runestone_ritual_executor_register", ()-> RunestoneRitualExecutorRegisterKjsEvent.class);
}
