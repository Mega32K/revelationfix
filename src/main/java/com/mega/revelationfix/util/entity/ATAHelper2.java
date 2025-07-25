package com.mega.revelationfix.util.entity;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.item.curios.DimensionalWillItem;
import com.mega.revelationfix.common.item.curios.EternalWatchItem;
import com.mega.revelationfix.common.item.curios.TheNeedleItem;
import com.mega.revelationfix.common.item.curios.el.BlessingScroll;
import com.mega.revelationfix.safe.OdamanePlayerExpandedContext;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class ATAHelper2 {
    public static boolean hasOdamane(Entity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        if (!(living instanceof Player)) return false;
        return ((PlayerInterface) living).revelationfix$odamaneHaloExpandedContext().hasOdamane();
    }

    public static boolean hasNeedle(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        if (!(living instanceof Player)) return false;
        return CuriosFinder.hasCurio(living, (stack) -> stack.getItem() instanceof TheNeedleItem);
    }

    public static boolean hasDimensionalWill(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        return CuriosFinder.hasCurio(living, (stack) -> stack.getItem() instanceof DimensionalWillItem);
    }

    public static boolean hasEternalWatch(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        return CuriosFinder.hasCurio(living, (stack) -> stack.getItem() instanceof EternalWatchItem);
    }

    public static boolean hasBlessingScroll(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        if (!(living instanceof Player)) return false;
        return CuriosFinder.hasCurio(living, (stack) -> stack.getItem() instanceof BlessingScroll);
    }

    public static OdamanePlayerExpandedContext getOdamaneEC(LivingEntity player) {
        if (player instanceof FakeSpellerEntity spellerEntity)
            player = spellerEntity.getOwner();
        if (!(player instanceof Player)) throw new AssertionError("NONE");
        return ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext();
    }
}
