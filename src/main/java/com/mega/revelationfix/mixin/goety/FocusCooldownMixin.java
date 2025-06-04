package com.mega.revelationfix.mixin.goety;

import com.Polarice3.Goety.common.capabilities.soulenergy.FocusCooldown;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.init.ModAttributes;
import com.mega.revelationfix.util.ATAHelper2;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import z1gned.goetyrevelation.util.ATAHelper;

import java.util.Iterator;
import java.util.Map;

@Mixin(value = FocusCooldown.class, remap = false)
public abstract class FocusCooldownMixin {
    @Shadow
    @Final
    public Map<Item, FocusCooldown.CooldownInstance> cooldowns;

    @Shadow
    protected abstract void onCooldownStarted(Player player, Item item, int duration);

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lcom/Polarice3/Goety/common/capabilities/soulenergy/FocusCooldown$CooldownInstance;decreaseTime()V"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void tick(Player player, Level level, CallbackInfo ci, Iterator iterator, Map.Entry entry) {
        if (ATAHelper2.hasOdamane(player)) {
            //所有冷却时间缩短到0.1s
            CooldownInstanceAccessor ca = (CooldownInstanceAccessor) entry.getValue();
            ca.setTotal(2);
            if (ca.time() > 2)
                ca.setTime(2);
        }
        if (ATAHelper.hasHalo(player)) {
            if (CommonConfig.haloSpellCooldownReduction > 1.0D) {
                for (int i = 0; i < Mth.floor(CommonConfig.haloSpellCooldownReduction - 1.0D); i++) {
                    ((FocusCooldown.CooldownInstance) entry.getValue()).decreaseTime();
                }
            }
        }

    }

    @Inject(method = "addCooldown", at = @At("HEAD"), cancellable = true)
    private void addCooldown(Player player, Level level, Item item, int coolDown, CallbackInfo ci) {
        ci.cancel();
        coolDown = (int) (coolDown * Math.max(0F, (2F - player.getAttributeValue(ModAttributes.SPELL_COOLDOWN.get()))));
        this.cooldowns.put(item, new FocusCooldown.CooldownInstance(coolDown));
        if (!level.isClientSide) {
            this.onCooldownStarted(player, item, coolDown);
        }
    }
}
