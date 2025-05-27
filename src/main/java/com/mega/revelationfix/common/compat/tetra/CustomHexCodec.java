package com.mega.revelationfix.common.compat.tetra;


import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.PrimitiveCodec;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CustomHexCodec implements PrimitiveCodec<Integer> {
    public static final se.mickelus.mutil.util.HexCodec instance = new se.mickelus.mutil.util.HexCodec();

    public CustomHexCodec() {
    }

    public <T> DataResult<Integer> read(DynamicOps<T> ops, T input) {
        return ops.getStringValue(input).map((val) -> {
            return (int) Long.parseLong(val, 16);
        });
    }

    public <T> T write(DynamicOps<T> ops, Integer value) {
        return ops.createString(Integer.toHexString(value));
    }

    public String toString() {
        return "mutil-hex";
    }
}