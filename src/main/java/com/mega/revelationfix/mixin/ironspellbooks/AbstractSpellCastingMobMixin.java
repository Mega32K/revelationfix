package com.mega.revelationfix.mixin.ironspellbooks;

import com.mega.endinglib.util.annotation.ModDependsMixin;
import com.mega.revelationfix.util.LivingEntityEC;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.entity.mobs.abstract_spell_casting_mob.AbstractSpellCastingMob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(AbstractSpellCastingMob.class)
@ModDependsMixin("irons_spellbooks")
public class AbstractSpellCastingMobMixin {
    @Shadow(remap = false) @Nullable private SpellData castingSpell;

    @Shadow(remap = false) private boolean recreateSpell;

    @Inject(method = "initiateCastSpell", at = @At("HEAD"), cancellable = true, remap = false)
    private void initiateCastSpell(AbstractSpell spell, int spellLevel, CallbackInfo ci) {
        if (((LivingEntityEC) this).revelationfix$livingECData().banAnySpelling > 0) {
            ci.cancel();
            castingSpell = null;
        }
    }
    @Inject(method = "customServerAiStep", at = @At("HEAD"))
    private void customServerAiStep(CallbackInfo ci) {
        if (((LivingEntityEC) this).revelationfix$livingECData().banAnySpelling > 0) {
            if (castingSpell != null) {
                this.recreateSpell = true;
            }
        }
    }
}
