package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.Polarice3.Goety.common.network.ModServerBossInfo;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Predicate;

@Mixin(Apostle.class)
public interface ApostleAccessor {
    @Accessor(value = "SPEED_MODIFIER_CASTING", remap = false)
    static AttributeModifier SPEED_MODIFIER_CASTING() {
        return null;
    }

    @Accessor(value = "SPEED_MODIFIER_MONOLITH", remap = false)
    static AttributeModifier SPEED_MODIFIER_MONOLITH() {
        return null;
    }

    @Accessor(value = "bossInfo", remap = false)
    ModServerBossInfo bossInfo();

    @Accessor(value = "bossInfo", remap = false)
    @Mutable
    void setBossInfo(ModServerBossInfo bossInfo);

    @Accessor(value = "ALIVE", remap = false)
    Predicate<Entity> ALIVE();

    @Accessor(value = "tornadoCoolDown", remap = false)
    void tornadoCoolDown(int t);

    @Accessor(value = "infernoCoolDown", remap = false)
    void infernoCoolDown(int t);

    @Accessor(value = "monolithCoolDown", remap = false)
    void monolithCoolDown(int t);

    @Accessor(value = "damnedCoolDown", remap = false)
    void damnedCoolDown(int t);

    @Accessor(value = "tornadoCoolDown", remap = false)
    int tornadoCoolDown();

    @Accessor(value = "infernoCoolDown", remap = false)
    int infernoCoolDown();

    @Accessor(value = "monolithCoolDown", remap = false)
    int monolithCoolDown();

    @Accessor(value = "damnedCoolDown", remap = false)
    int damnedCoolDown();

    @Accessor(value = "coolDown", remap = false)
    void coolDown(int t);

    @Accessor(value = "coolDown", remap = false)
    int coolDown();
}
