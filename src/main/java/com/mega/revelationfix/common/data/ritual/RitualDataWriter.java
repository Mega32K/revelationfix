package com.mega.revelationfix.common.data.ritual;

import net.minecraft.network.FriendlyByteBuf;

import java.util.Optional;

public class RitualDataWriter implements FriendlyByteBuf.Writer<RitualData> {
    public static RitualDataWriter INSTANCE;
    @Override
    public void accept(FriendlyByteBuf friendlyByteBuf, RitualData ritualData) {
        friendlyByteBuf.writeUtf(ritualData.getPluginName());
        friendlyByteBuf.writeUtf(ritualData.getIconItemKey());
    }
    public static RitualDataWriter getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RitualDataWriter();
        return INSTANCE;
    }
}
