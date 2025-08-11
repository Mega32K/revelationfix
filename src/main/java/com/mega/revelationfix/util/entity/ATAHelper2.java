package com.mega.revelationfix.util.entity;

import com.Polarice3.Goety.utils.CuriosFinder;
import com.mega.revelationfix.common.entity.binding.FakeSpellerEntity;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.curios.enigmtic_legacy.EnigmaticLegacyItemInit;
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
        return CuriosFinder.hasCurio(living, GRItems.THE_NEEDLE.get());
    }

    public static boolean hasDimensionalWill(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        return CuriosFinder.hasCurio(living, GRItems.DIMENSIONAL_WILL.get());
    }

    public static boolean hasEternalWatch(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        return CuriosFinder.hasCurio(living, GRItems.ETERNAL_WATCH.get());
    }
    public static boolean hasBlessingScroll(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        if (!(living instanceof Player)) return false;
        return CuriosFinder.hasCurio(living, EnigmaticLegacyItemInit.BLESSING_SCROLL_ITEM);
    }
    public static boolean hasSoulOfObsidian(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        return CuriosFinder.hasCurio(living, GRItems.SOUL_OF_OBSIDIAN.get());
    }
    public static boolean hasAmuletOfSlime(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        return CuriosFinder.hasCurio(living, GRItems.AMULET_OF_SLIME.get());
    }
    public static boolean hasGoldFeather(LivingEntity living) {
        if (living instanceof FakeSpellerEntity spellerEntity)
            living = spellerEntity.getOwner();
        return CuriosFinder.hasCurio(living, GRItems.GOLD_FEATHER.get());
    }
    public static OdamanePlayerExpandedContext getOdamaneEC(LivingEntity player) {
        if (player instanceof FakeSpellerEntity spellerEntity)
            player = spellerEntity.getOwner();
        if (!(player instanceof Player)) throw new AssertionError("NONE");
        return ((PlayerInterface) player).revelationfix$odamaneHaloExpandedContext();
    }
}
