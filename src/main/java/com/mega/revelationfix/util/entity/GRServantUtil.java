package com.mega.revelationfix.util.entity;

import com.mega.revelationfix.common.entity.IMonsterServant;

import java.util.UUID;

public class GRServantUtil {
    public static boolean isOwnerNotOnline(IMonsterServant owned) {
        UUID uuid = owned.getOwnerId();
        if (uuid == null)
            return false;
        else if (owned.getTrueOwner() == null)
            return false;
        return (!owned.getTrueOwner().isAlive() && (owned.getIMSTarget() == null || !owned.getIMSTarget().isAlive()));
    }
}
