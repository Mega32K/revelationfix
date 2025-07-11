package com.mega.revelationfix.common.compat;

import com.mega.revelationfix.common.init.GRItems;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ToBeInject {
    public static boolean inWhitelist(Item item) {
        return false;
    }
    public static boolean inWhitelist(Entity entity) {
        return false;
    }
}
