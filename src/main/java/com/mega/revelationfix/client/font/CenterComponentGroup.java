package com.mega.revelationfix.client.font;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;

import java.util.Arrays;

public class CenterComponentGroup {
    public final ObjectArrayList<Component> toCenter = new ObjectArrayList<>();
    public final ObjectArrayList<String> toBake = new ObjectArrayList<>();

    public CenterComponentGroup(final Component... components) {
        toCenter.addAll(Arrays.asList(components));
        OdamaneFont.centerComponentGroups.add(this);
    }

    public boolean is(String text) {
        if (toBake.isEmpty())
            toBake.addAll(toCenter.stream().map(Component::getString).toList());
        for (String s : toBake) {
            if (s.contains(text))
                return true;
        }
        return false;
    }
}
