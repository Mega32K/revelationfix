package com.mega.revelationfix.common.network.s2c;

import com.mega.revelationfix.common.init.GRItems;
import com.mega.revelationfix.common.odamane.common.TheEndPuzzleItems;
import com.mega.revelationfix.safe.ClientLevelExpandedContext;
import com.mega.revelationfix.safe.ClientLevelInterface;
import com.mega.revelationfix.safe.TheEndRitualItemContext;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

//服务端把选中puzzle数据传入clientlevel
public class TheEndPuzzleUpdatePacket {
    private final ResourceLocation puzzle1;
    private final ResourceLocation puzzle2;
    private final ResourceLocation puzzle3;
    private final ResourceLocation puzzle4;
    private final ResourceLocation theEndCraft;

    public TheEndPuzzleUpdatePacket(ResourceLocation puzzle1, ResourceLocation puzzle2, ResourceLocation puzzle3, ResourceLocation puzzle4, ResourceLocation theEndCraft) {
        this.puzzle1 = puzzle1;
        this.puzzle2 = puzzle2;
        this.puzzle3 = puzzle3;
        this.puzzle4 = puzzle4;
        this.theEndCraft = theEndCraft;
    }

    public static TheEndPuzzleUpdatePacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new TheEndPuzzleUpdatePacket(friendlyByteBuf.readResourceLocation(), friendlyByteBuf.readResourceLocation(), friendlyByteBuf.readResourceLocation(), friendlyByteBuf.readResourceLocation(), friendlyByteBuf.readResourceLocation());
    }

    public static void encode(TheEndPuzzleUpdatePacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeResourceLocation(packet.puzzle1);
        friendlyByteBuf.writeResourceLocation(packet.puzzle2);
        friendlyByteBuf.writeResourceLocation(packet.puzzle3);
        friendlyByteBuf.writeResourceLocation(packet.puzzle4);
        friendlyByteBuf.writeResourceLocation(packet.theEndCraft);
    }

    public static void handle(TheEndPuzzleUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    static void handle0(TheEndPuzzleUpdatePacket packet, Supplier<NetworkEvent.Context> context) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level != null) {
            TheEndPuzzleItems.bake();
            ClientLevelExpandedContext expandedContext = ((ClientLevelInterface) mc.level).revelationfix$ECData();
            expandedContext.PUZZLE1 = packet.puzzle1;
            expandedContext.PUZZLE2 = packet.puzzle2;
            expandedContext.PUZZLE3 = packet.puzzle3;
            expandedContext.PUZZLE4 = packet.puzzle4;
            expandedContext.TE_END_CRAFT = packet.theEndCraft;
            TheEndRitualItemContext.PUZZLE1 = ForgeRegistries.ITEMS.getValue(packet.puzzle1);
            TheEndRitualItemContext.PUZZLE2 = ForgeRegistries.ITEMS.getValue(packet.puzzle2);
            TheEndRitualItemContext.PUZZLE3 = ForgeRegistries.ITEMS.getValue(packet.puzzle3);
            TheEndRitualItemContext.PUZZLE4 = ForgeRegistries.ITEMS.getValue(packet.puzzle4);
            TheEndRitualItemContext.THE_END_CRAFT = ForgeRegistries.ITEMS.getValue(packet.theEndCraft);
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE1.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES);
                TheEndRitualItemContext.PUZZLE1.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE2.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES2);
                TheEndRitualItemContext.PUZZLE2.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE3.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES3);
                TheEndRitualItemContext.PUZZLE3.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.PUZZLE4.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_PUZZLES4);
                TheEndRitualItemContext.PUZZLE4.builtInRegistryHolder().bindTags(tagKeys);
            }
            {
                List<TagKey<Item>> tagKeys = new ArrayList<>(TheEndRitualItemContext.THE_END_CRAFT.builtInRegistryHolder().tags().toList());
                tagKeys.add(GRItems.THE_END_CRAFTING);
                TheEndRitualItemContext.THE_END_CRAFT.builtInRegistryHolder().bindTags(tagKeys);
            }
        }
    }
}
