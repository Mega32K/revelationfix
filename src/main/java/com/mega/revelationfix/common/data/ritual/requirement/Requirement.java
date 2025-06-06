package com.mega.revelationfix.common.data.ritual.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Map;

public interface Requirement {
    String getType();

    void compileData(JsonElement jsonElement);
}
