package com.mega.revelationfix.common.network.s2c;

import com.Polarice3.Goety.api.magic.SpellType;
import com.mega.revelationfix.client.spell.SpellClientContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class SpellCircleStatePacket {
    public static final byte STATE_START_SPELL_CIRCLE = 0;
    public static final byte STATE_CANCEL_SPELL_CIRCLE = 1;
    private final SpellType spell;
    private final byte state;
    private final boolean isMainHand;
    private final float radius;
    public SpellCircleStatePacket(SpellType spellType, byte state, boolean isMainHand, float radius) {
        this.spell = spellType;
        this.state = state;
        this.isMainHand = isMainHand;
        this.radius = radius;
    }


    public static SpellCircleStatePacket decode(FriendlyByteBuf friendlyByteBuf) {
        return new SpellCircleStatePacket(friendlyByteBuf.readEnum(SpellType.class), friendlyByteBuf.readByte(), friendlyByteBuf.readBoolean(), friendlyByteBuf.readFloat());
    }

    public static void encode(SpellCircleStatePacket packet, FriendlyByteBuf friendlyByteBuf) {
        friendlyByteBuf.writeEnum(packet.spell);
        friendlyByteBuf.writeByte(packet.state);
        friendlyByteBuf.writeBoolean(packet.isMainHand);
        friendlyByteBuf.writeFloat(packet.radius);
    }

    public static void handle(SpellCircleStatePacket packet, Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            if (packet != null)
                handle0(packet, context);
        });
        context.get().setPacketHandled(true);
    }

    public byte getState() {
        return state;
    }

    public SpellType getSpell() {
        return spell;
    }

    public float getRadius() {
        return radius;
    }

    public boolean isMainHand() {
        return isMainHand;
    }

    static void handle0(SpellCircleStatePacket packet, Supplier<NetworkEvent.Context> context) {
        SpellClientContext.receiveSpellStatePacket(packet);
    }
}
