package com.mega.revelationfix.util.entity;

import com.Polarice3.Goety.api.entities.IOwned;
import com.mega.revelationfix.common.entity.IMonsterServant;

import java.util.UUID;

public class GRServantUtil {
    public static boolean isOwnerNotOnline(IMonsterServant owned) {
        UUID uuid = owned.getOwnerId();
        return uuid == null || owned.getTrueOwner() == null || (!owned.getTrueOwner().isAlive() && (owned.getIMSTarget() == null || !owned.getIMSTarget().isAlive()));
    }
}
