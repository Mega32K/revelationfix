package com.mega.revelationfix.common.apollyon.common;

import com.Polarice3.Goety.common.entities.boss.Apostle;
import com.mega.endinglib.mixin.accessor.AccessorAttributeInstance;
import com.mega.revelationfix.common.compat.SafeClass;
import com.mega.revelationfix.common.config.CommonConfig;
import com.mega.revelationfix.common.entity.FakeItemEntity;
import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.item.other.CuriosStorageCrystal;
import com.mega.revelationfix.mixin.AttributeMapAccessor;
import com.mega.revelationfix.safe.GRSavedDataEC;
import com.mega.revelationfix.safe.GRSavedDataExpandedContext;
import com.mega.revelationfix.safe.entity.AttributeInstanceInterface;
import com.mega.revelationfix.safe.entity.PlayerInterface;
import com.mega.revelationfix.util.entity.ATAHelper2;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.CuriosApi;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;
import z1gned.goetyrevelation.util.ApollyonAbilityHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class PlayerTickrateExecutor {
    public static Predicate<Entity> doomApollyon = (e) -> {
        Apostle apostle = (Apostle) e;
        ApollyonAbilityHelper helper = (ApollyonAbilityHelper) e;
        return helper.allTitlesApostle_1_20_1$isApollyon() && SafeClass.isDoom(apostle);
    };

    public static void playerTick(Player player) {
        PlayerInterface playerInterface = (PlayerInterface) player;
        //上一次更新时的doom阶段值(字段)
        boolean lastMode = playerInterface.revelationfix$temp_isBaseAttributeMode();
        //当前doom阶段值(data)
        if (playerInterface.revelationfix$isBaseAttributeMode() && !ATAHelper2.hasOdamane(player)) {
            if (player.isUsingItem())
                player.stopUsingItem();
            //仅服务端时
            if (!player.level().isClientSide) {
                if (player.level().dimension() == Level.NETHER) {
                    if (player.tickCount % 20 == 0) {
                        if (player.level().getEntities(player, player.getBoundingBox().inflate(128), (e) -> e instanceof Apostle).stream()
                                .noneMatch(doomApollyon)) {
                            playerInterface.revelationfix$setBaseAttributeMode(false);
                        }
                        if (player.isCreative() || player.isSpectator())
                            playerInterface.revelationfix$setBaseAttributeMode(false);
                    }
                } else {
                    playerInterface.revelationfix$setBaseAttributeMode(false);
                }
            }
        } else {
            playerInterface.revelationfix$temp_setBaseAttributeMode(false);
        }


        playerInterface.revelationfix$temp_setBaseAttributeMode(playerInterface.revelationfix$isBaseAttributeMode());
        if (player.tickCount % 20 == 0 && playerInterface.revelationfix$temp_isBaseAttributeMode()) {
            boolean not = player.isCreative() || player.isSpectator();
            AttributeMapAccessor accessor = (AttributeMapAccessor) player.getAttributes();
            accessor.attributes().values().forEach(ins -> {
                AttributeInstanceInterface i = (AttributeInstanceInterface) ins;
                i.revelationfix$setAlwaysBase(!not);
                ((AccessorAttributeInstance) ins).invokeSetDirty();
                ins.getValue();
            });
        }
        AttributeInstance attributeInstance = player.getAttribute(Attributes.MAX_HEALTH);
        if ((lastMode && !playerInterface.revelationfix$temp_isBaseAttributeMode()) || (player.tickCount % 20 == 0 && attributeInstance != null && ((AttributeInstanceInterface) attributeInstance).revelationfix$alwaysBase() && !playerInterface.revelationfix$temp_isBaseAttributeMode())) {
            AttributeMapAccessor accessor = (AttributeMapAccessor) player.getAttributes();
            accessor.attributes().values().forEach(ins -> {
                AttributeInstanceInterface i = (AttributeInstanceInterface) ins;
                i.revelationfix$setAlwaysBase(false);
                ((AccessorAttributeInstance) ins).invokeSetDirty();
                ins.getValue();
            });
        }
        if (isInDoom(player) && !player.level().isClientSide && player.tickCount % 5 == 0) {
            Inventory inventory = player.getInventory();
            ItemCooldowns itemCooldowns = player.getCooldowns();
            ServerLevel serverLevel = (ServerLevel) player.level();
            DefeatApollyonInNetherState state = GRSavedDataExpandedContext.state(serverLevel);
            List<ItemStack> bannedStorageCrystals = ((GRSavedDataEC) state).revelationfix$dataEC().getBannedCurios();
            List<ItemStack> stacks = new ArrayList<>();
            AtomicBoolean changed = new AtomicBoolean(false);
            synchronized (stacks) {
                AtomicInteger totalCount = new AtomicInteger(0);
                List<ItemStack> toStealArmors = new ObjectArrayList<>();
                for (ItemStack stack : inventory.armor) {
                    if (!CommonConfig.inWhitelist(stack.getItem()) && !stack.isEmpty()) {
                        CooldownsManager.setItemCooldowns(player, stack.getItem(), 70);
                        changed.set(true);
                        stacks.add(stack);
                        toStealArmors.add(stack);
                        if (totalCount.addAndGet(1) < 6) {
                            FakeItemEntity itemEntity = new FakeItemEntity(player.level(), player.getX(), player.getY(0.5), player.getZ(), stack);
                            itemEntity.setGlowingTag(true);
                            player.level().addFreshEntity(itemEntity);
                            itemEntity.push(player.getRandom().triangle(0D, 0.6D), 0D, player.getRandom().triangle(0D, 0.6D));
                        }
                    }
                }
                for (ItemStack toClear : toStealArmors)
                    inventory.removeItem(toClear);
                CuriosApi.getCuriosInventory(player).ifPresent(iCuriosItemHandler -> iCuriosItemHandler.getCurios().values().forEach(iCurioStacksHandler -> {
                    for (int i = iCurioStacksHandler.getSlots() - 1; i >= 0; i--) {
                        ItemStack stack = iCurioStacksHandler.getStacks().getStackInSlot(i);
                        if (!stack.isEmpty() && !CommonConfig.inWhitelist(stack.getItem())) {
                            if (!CommonConfig.inWhitelist(stack.getItem())) {
                                CooldownsManager.setItemCooldowns(player, stack.getItem(), 70);
                                changed.set(true);
                                stacks.add(stack);
                                if (totalCount.addAndGet(1) < 6) {
                                    FakeItemEntity itemEntity = new FakeItemEntity(player.level(), player.getX(), player.getY(0.5), player.getZ(), stack);
                                    itemEntity.setGlowingTag(true);
                                    player.level().addFreshEntity(itemEntity);
                                    itemEntity.push(player.getRandom().triangle(0D, 0.6D), 0D, player.getRandom().triangle(0D, 0.6D));
                                }
                                iCurioStacksHandler.getStacks().setStackInSlot(i, ItemStack.EMPTY);
                            }
                        }
                    }
                }));

            }
            ItemStack findCrystal;
            if (!(findCrystal = findExistStorageCrystal(bannedStorageCrystals, player)).isEmpty()) {
                CuriosStorageCrystal.store(findCrystal, stacks, player.getName().getString());
            } else {
                ItemStack itemStack = GRItems.CURIOS_STORAGE_CRYSTAL.get().getDefaultInstance();
                CuriosStorageCrystal.store(itemStack, stacks, player.getName().getString());
                bannedStorageCrystals.add(itemStack);
            }
            if (changed.get()) {
                if (player instanceof ServerPlayer serverPlayer)
                    serverPlayer.playNotifySound(SoundEvents.AMETHYST_BLOCK_BREAK, SoundSource.PLAYERS, 10F, 1F);
                state.setDirty();
            }

            inventory.items.forEach(stack -> {
                if (!CommonConfig.inWhitelist(stack.getItem()))
                    CooldownsManager.setItemCooldowns(player, stack.getItem(), 70);
            });
            inventory.offhand.forEach(stack -> {
                if (!CommonConfig.inWhitelist(stack.getItem()))
                    CooldownsManager.setItemCooldowns(player, stack.getItem(), 70);
            });
        }
    }

    static ItemStack findExistStorageCrystal(List<ItemStack> bannedCrystals, Player player) {
        ItemStack stack = ItemStack.EMPTY;
        for (ItemStack is : bannedCrystals) {
            if (is.is(GRItems.CURIOS_STORAGE_CRYSTAL.get()) && is.hasTag()) {
                if (is.getTag().contains("OwnerName", 8) && is.getTag().getString("OwnerName").equals(player.getName().getString()))
                    return is;
            }

        }
        return stack;
    }

    public static boolean isInDoom(Player player) {
        return ((PlayerInterface) player).revelationfix$temp_isBaseAttributeMode();
    }

    public static void disableBaseValueMode(Player player) {
        AttributeMapAccessor accessor = (AttributeMapAccessor) player.getAttributes();
        accessor.attributes().values().forEach(ins -> {
            AttributeInstanceInterface i = (AttributeInstanceInterface) ins;
            i.revelationfix$setAlwaysBase(false);
            ((AccessorAttributeInstance) ins).invokeSetDirty();
            ins.getValue();
        });
    }
}
