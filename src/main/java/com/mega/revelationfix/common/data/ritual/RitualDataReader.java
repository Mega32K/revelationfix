package com.mega.revelationfix.common.data.ritual;

import net.minecraft.network.FriendlyByteBuf;

public class RitualDataReader implements FriendlyByteBuf.Reader<RitualData> {
    public static RitualDataReader INSTANCE;

    @Override
    public RitualData apply(FriendlyByteBuf friendlyByteBuf) {
        RitualData ritualData = new RitualData(friendlyByteBuf.readUtf());
        ritualData.setIconItemKey(friendlyByteBuf.readUtf());
        return ritualData;
    }

    public static RitualDataReader getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RitualDataReader();
        return INSTANCE;
    }
}
