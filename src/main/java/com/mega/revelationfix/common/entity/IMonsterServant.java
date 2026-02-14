package com.mega.revelationfix.common.entity;

import com.Polarice3.Goety.api.entities.ally.IServant;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface IMonsterServant extends IServant {
    Entity getIMSTarget();
}
