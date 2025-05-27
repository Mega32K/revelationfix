package com.mega.revelationfix.mixin.fantasy_ending.time.ironspellbook;

import com.mega.revelationfix.safe.LevelEC;
import com.mega.revelationfix.safe.LevelExpandedContext;
import com.mega.revelationfix.util.time.TimeStopUtils;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.CastResult;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = AbstractSpell.class, remap = false)
//RevelationMixinPlugin检查
//@NoModDependsMixin("fantasy_ending")
//@ModDependsMixin("irons_spellbooks")
public abstract class AbstractSpellMixin {
    @Unique
    private boolean uom$clientOriginRarity;
    @Mutable
    @Shadow
    @Final
    private int maxRarity;

    @Shadow
    public abstract ResourceLocation getSpellResource();

    @Shadow
    public abstract int getMaxLevel();

    @Inject(method = "canBeCastedBy", at = @At("HEAD"), cancellable = true)
    private void canBeCastedBy(int spellLevel, CastSource castSource, MagicData playerMagicData, Player player, CallbackInfoReturnable<CastResult> cir) {
        LevelExpandedContext levelEC = ((LevelEC) player.level).uom$levelECData();
        if (TimeStopUtils.isTimeStop)
            if (TimeStopUtils.andSameDimension(player.level()) && !TimeStopUtils.canMove(player))
                cir.setReturnValue(new CastResult(CastResult.Type.FAILURE));
    }
}
