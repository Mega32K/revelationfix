package com.mega.revelationfix.proxy;

import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.compat.ironspell.IronSpellbooksEvents;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy implements ModProxy {
    public CommonProxy() { 
    }
    public void onEventsRegister() {
        if (SafeClass.isIronSpellbookslLoaded()) {
            MinecraftForge.EVENT_BUS.register(IronSpellbooksEvents.class);
        }
    }
}
