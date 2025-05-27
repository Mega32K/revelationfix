package com.mega.revelationfix.safe;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import z1gned.goetyrevelation.data.DefeatApollyonInNetherState;

import java.util.*;

public class GRSavedDataExpandedContext {
    private final ArrayList<ItemStack> bannedCurios = new ArrayList<>();
    private final Set<UUID> ateAscensionHardCandyPlayers = new HashSet<>();
    public DefeatApollyonInNetherState state;
    private boolean summoned_apollyon_overworld;
    //去合成终末环的物品/锚定的物品
    private ResourceLocation THE_END_CRAFT_ITEM;
    //谜题之物/锚定物品合成材料
    private ResourceLocation PUZZLE1;
    private ResourceLocation PUZZLE2;
    private ResourceLocation PUZZLE3;
    private ResourceLocation PUZZLE4;

    public GRSavedDataExpandedContext(DefeatApollyonInNetherState state) {
        this.state = state;
    }

    public static DefeatApollyonInNetherState state(ServerLevel serverLevel) {
        return state(serverLevel.server);
    }

    public static DefeatApollyonInNetherState state(MinecraftServer server) {
        return server.overworld().getDataStorage().computeIfAbsent(DefeatApollyonInNetherState::createNbt, DefeatApollyonInNetherState::new, "killed_apollyon_in_nether");
    }

    /**
     * xxx = nbt.get
     *
     * @param nbt NBT Data
     */
    public void read(CompoundTag nbt) {
        bannedCurios.clear();
        summoned_apollyon_overworld = nbt.getBoolean("SummonedApollyonOverworld");
        {
            ListTag listtag = nbt.getList("BannedCurios", 10);
            for (int i = 0; i < listtag.size(); ++i) {
                ItemStack stack = ItemStack.of(listtag.getCompound(i));
                bannedCurios.add(stack);
            }
        }
        {
            ListTag listtag = nbt.getList("AteAscensionCandies", 10);
            for (int i = 0; i < listtag.size(); ++i) {
                CompoundTag tag = listtag.getCompound(i);
                if (tag.hasUUID("PlayerID")) {
                    ateAscensionHardCandyPlayers.add(tag.getUUID("PlayerID"));
                }
            }
        }
        THE_END_CRAFT_ITEM = new ResourceLocation(nbt.getString("TheEndCraftItem"));
        PUZZLE1 = new ResourceLocation(nbt.getString("Puzzle1"));
        PUZZLE2 = new ResourceLocation(nbt.getString("Puzzle2"));
        PUZZLE3 = new ResourceLocation(nbt.getString("Puzzle3"));
        PUZZLE4 = new ResourceLocation(nbt.getString("Puzzle4"));
    }

    /**
     * nbt.put(xxx)
     *
     * @param nbt NBT Data
     */
    public void save(CompoundTag nbt) {
        {
            ListTag listtag = new ListTag();
            for (int i = 0; i < bannedCurios.size(); ++i) {
                ItemStack itemstack = bannedCurios.get(i);
                if (!itemstack.isEmpty()) {
                    CompoundTag compoundtag = new CompoundTag();
                    itemstack.save(compoundtag);
                    listtag.add(compoundtag);
                }
            }
            nbt.put("BannedCurios", listtag);
        }
        {
            ListTag listtag = new ListTag();
            Iterator<UUID> iterator = ateAscensionHardCandyPlayers.iterator();
            while (iterator.hasNext()) {
                UUID uuid = iterator.next();
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putUUID("PlayerID", uuid);
                listtag.add(compoundtag);
            }
            nbt.put("AteAscensionCandies", listtag);
        }
        nbt.putBoolean("SummonedApollyonOverworld", summoned_apollyon_overworld);
        nbt.putString("TheEndCraftItem", THE_END_CRAFT_ITEM == null ? "" : THE_END_CRAFT_ITEM.toString());
        nbt.putString("Puzzle1", PUZZLE1 == null ? "" : PUZZLE1.toString());
        nbt.putString("Puzzle1", PUZZLE1 == null ? "" : PUZZLE1.toString());
        nbt.putString("Puzzle2", PUZZLE2 == null ? "" : PUZZLE2.toString());
        nbt.putString("Puzzle3", PUZZLE3 == null ? "" : PUZZLE3.toString());
        nbt.putString("Puzzle4", PUZZLE4 == null ? "" : PUZZLE4.toString());
    }

    public boolean isSummonedApollyonOverworld() {
        return summoned_apollyon_overworld;
    }

    public void setSummonedApollyonOverworld(boolean z) {
        this.summoned_apollyon_overworld = z;
        this.state.setDirty();
    }

    public ArrayList<ItemStack> getBannedCurios() {
        return bannedCurios;
    }

    public Set<UUID> getAteAscensions() {
        return ateAscensionHardCandyPlayers;
    }

    public ResourceLocation getTheEndCraftItem() {
        return THE_END_CRAFT_ITEM;
    }

    public void setTheEndCraftItem(Item item) {
        THE_END_CRAFT_ITEM = BuiltInRegistries.ITEM.getKey(item);
        this.state.setDirty();
    }

    public ResourceLocation getPUZZLE1() {
        return PUZZLE1;
    }

    public void setPUZZLE1(Item item) {
        this.PUZZLE1 = BuiltInRegistries.ITEM.getKey(item);
        this.state.setDirty();
    }

    public ResourceLocation getPUZZLE2() {
        return PUZZLE2;
    }

    public void setPUZZLE2(Item item) {
        this.PUZZLE2 = BuiltInRegistries.ITEM.getKey(item);
        this.state.setDirty();
    }

    public ResourceLocation getPUZZLE3() {
        return PUZZLE3;
    }

    public void setPUZZLE3(Item item) {
        this.PUZZLE3 = BuiltInRegistries.ITEM.getKey(item);
        this.state.setDirty();
    }

    public ResourceLocation getPUZZLE4() {
        return PUZZLE4;
    }

    public void setPUZZLE4(Item item) {
        this.PUZZLE4 = BuiltInRegistries.ITEM.getKey(item);
        this.state.setDirty();
    }
}
