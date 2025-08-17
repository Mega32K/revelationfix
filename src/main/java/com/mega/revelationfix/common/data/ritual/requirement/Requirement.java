package com.mega.revelationfix.common.data.ritual.requirement;

import com.google.gson.JsonElement;

public interface Requirement {
    String getType();

    void compileData(JsonElement jsonElement);
}
