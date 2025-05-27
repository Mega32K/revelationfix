package com.mega.revelationfix.util;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModFile;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.forgespi.language.IModInfo;

import java.util.*;

public class EarlyConfig {
    public static Set<String> modIds = null;
    public static Map<String, String> modVerisonMap = new HashMap<>();

    static {
        modIds = new HashSet<>();
        LoadingModList loadingModList = FMLLoader.getLoadingModList();
        final List<List<IModInfo>> modInfos = loadingModList.getModFiles().stream()
                .map(ModFileInfo::getFile)
                .map(ModFile::getModInfos).toList();
        modIds = new HashSet<>();
        for (List<IModInfo> iModInfoList : modInfos) {
            for (IModInfo modInfo : iModInfoList) {
                modIds.add(modInfo.getModId());
                modVerisonMap.put(modInfo.getModId(), modInfo.getVersion().getQualifier());
            }
        }
    }
}
