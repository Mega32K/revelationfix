package com.mega.revelationfix.common.entity;

import com.Polarice3.Goety.api.entities.ally.IServant;
import net.minecraft.world.entity.Entity;

public interface IMonsterServant extends IServant {
    Entity getIMSTarget();
}
