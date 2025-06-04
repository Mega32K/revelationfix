package com.mega.revelationfix.common.compat.kjs;

import com.mega.revelationfix.common.compat.kjs.events.KjsEvents;

public class KubeJSPlugin extends dev.latvian.mods.kubejs.KubeJSPlugin {
    @Override
    public void registerEvents() {
        KjsEvents.GROUP.register();
    }
}

