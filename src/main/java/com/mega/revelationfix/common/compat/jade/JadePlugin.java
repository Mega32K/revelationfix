package com.mega.revelationfix.common.compat.jade;

import com.Polarice3.Goety.compat.jade.ServantModeProvider;
import com.Polarice3.Goety.compat.jade.SummonLifespanProvider;
import com.Polarice3.Goety.compat.jade.SummonOwnerProvider;
import com.mega.revelationfix.common.block.RuneReactorBlock;
import com.mega.revelationfix.common.block.RunestoneEngravedTableBlock;
import com.mega.revelationfix.common.block.blockentity.RuneReactorBlockEntity;
import com.mega.revelationfix.common.entity.boss.ApostleServant;
import com.mega.revelationfix.common.entity.cultists.HereticServant;
import com.mega.revelationfix.common.entity.cultists.MaverickServant;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {
    public static IWailaClientRegistration CLIENT_REGISTRATION;

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, ApostleServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, HereticServant.class);
        registration.registerEntityComponent(SummonLifespanProvider.INSTANCE, MaverickServant.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, ApostleServant.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, HereticServant.class);
        registration.registerEntityComponent(ServantModeProvider.INSTANCE, MaverickServant.class);
        registration.registerBlockComponent(RunestoneCoreProvider.INSTANCE, RunestoneEngravedTableBlock.class);
        registration.registerBlockComponent(RuneReactorCoreProvider.INSTANCE, RuneReactorBlock.class);
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, ApostleServant.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, HereticServant.class);
        registration.registerEntityDataProvider(SummonOwnerProvider.INSTANCE, MaverickServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, ApostleServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, HereticServant.class);
        registration.registerEntityDataProvider(SummonLifespanProvider.INSTANCE, MaverickServant.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, ApostleServant.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, HereticServant.class);
        registration.registerEntityDataProvider(ServantModeProvider.INSTANCE, MaverickServant.class);
        registration.registerBlockDataProvider(RuneReactorCoreProvider.INSTANCE, RuneReactorBlockEntity.class);
    }
}
