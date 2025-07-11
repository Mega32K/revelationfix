package com.mega.revelationfix.mixin.gr;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.revelationfix.common.apollyon.common.AttackDamageChangeHandler;
import com.mega.revelationfix.common.apollyon.common.CommonEventHandler;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import z1gned.goetyrevelation.event.LivingEntityHurtEvent;
import z1gned.goetyrevelation.util.ATAHelper;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

@Mixin(value = LivingEntityHurtEvent.class, remap = false)
public class LivingEntityHurtEventMixin {
    /**
     * 免疫部分:{@link CommonEventHandler#haloInvulnerableTo(LivingAttackEvent)}
     * 此为减伤部分
     */
    @Inject(method = "onLivingHurt", at = @At("HEAD"), cancellable = true)
    private static void onLivingHurt(LivingHurtEvent event, CallbackInfo ci) {
        ci.cancel();
        DamageSource source = event.getSource();
        if (event.getEntity() instanceof Player player) {
            //破碎光环减伤下界30%其它维度15%
            if (ATAHelper.hasBrokenHalo(player)) {
                if (!source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                    event.setAmount(event.getAmount() * (player.level().dimension() == Level.NETHER ? 0.7F : 0.85F));
                }
            }

            //晋升光环减伤下界50%其它维度25%
            if (ATAHelper.hasHalo(player)) {
                if (!source.is(DamageTypes.FELL_OUT_OF_WORLD)) {
                    event.setAmount(event.getAmount() * (player.level().dimension() == Level.NETHER ? 0.5F : 0.75F));
                }
            }
        }

        if (event.getEntity() instanceof Apostle apostle && ((ApollyonAbilityHelper) apostle).allTitlesApostle_1_20_1$isApollyon())
            ((ApollyonAbilityHelper) apostle).setApollyonTime(AttackDamageChangeHandler.vanillaLimitTime);

    }
}
