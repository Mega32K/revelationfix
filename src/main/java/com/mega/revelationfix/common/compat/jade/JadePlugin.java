package com.mega.revelationfix.common.compat.jade;

import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.block.RunestoneEngravedTableBlock;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    public static IWailaClientRegistration CLIENT_REGISTRATION;
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(RunestoneCoreProvider.INSTANCE, RunestoneEngravedTableBlock.class);
        registration.registerBlockComponent(RuneReactorCoreProvider.INSTANCE, RuneReactorBlock.class);
    }
}
