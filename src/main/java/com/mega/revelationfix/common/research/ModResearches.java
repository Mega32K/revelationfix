package com.mega.revelationfix.common.research;

import com.Polarice3.Goety.common.research.Research;
import com.Polarice3.Goety.common.research.ResearchList;

public class ModResearches {
    public static final Research GOD_FORGING = new Research("lord");
    public static final Research EDEN = new Research("eden");

    static {
        ResearchList.registerResearch(ModResearches.GOD_FORGING.getId(), ModResearches.GOD_FORGING);
        ResearchList.registerResearch(ModResearches.EDEN.getId(), ModResearches.EDEN);
    }
}
