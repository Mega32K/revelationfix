package com.mega.revelationfix.util.other;

import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import org.joml.Vector2f;

public class MathUtils {
    public static final double phi = 1.618033988749894;
    public static final double pi = Math.PI;
    public static final float toDeg = 57.29577951308232F;
    public static final float toRad = 0.017453292519943F;
    public static final double sqrt2 = 1.414213562373095;
    private static final float[] SIN = Util.make(new float[65536], (p_14077_) -> {
        for (int i = 0; i < p_14077_.length; ++i) {
            p_14077_[i] = (float) Math.sin((double) i * Math.PI * 2.0D / 65536.0D);
        }

    });
    public static double[] SIN_TABLE = new double[65536];
    public static float[] ASIN = new float[65536];
    public static double[] ASIN_TABLE = new double[65536];
    public static float[] ACOS = new float[65536];
    public static double[] ACOS_TABLE = new double[65536];

    static {
        for (int i = 0; i < 65536; ++i) {
            SIN_TABLE[i] = Math.sin((double) i / 65536.0 * 2.0 * Math.PI);
        }
        for (int i = 0; i < 65536; ++i) {
            ASIN[i] = (float) Math.asin((double) i / 65536.0);
        }
        for (int i = 0; i < 65536; ++i) {
            ASIN_TABLE[i] = Math.asin((double) i / 65536.0);
        }
        for (int i = 0; i < 65536; ++i) {
            ACOS[i] = (float) Math.acos((double) i / 65536.0);
        }
        for (int i = 0; i < 65536; ++i) {
            ACOS_TABLE[i] = Math.acos((double) i / 65536.0);
        }
    }

    public MathUtils() {
    }

    public static double sin(double d) {
        return SIN_TABLE[(int) ((float) d * 10430.378F) & '\uffff'];
    }

    public static double cos(double d) {
        return SIN_TABLE[(int) ((float) d * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float sin(float p_14032_) {
        return SIN[(int) (p_14032_ * 10430.378F) & '\uffff'];
    }

    public static float cos(float p_14090_) {
        return SIN[(int) (p_14090_ * 10430.378F + 16384.0F) & '\uffff'];
    }

    public static float asin(float p_14090_) {
        return ASIN[(int) (p_14090_ * 65535.0F) & '\uffff'];
    }

    public static double asin(double p_14090_) {
        return ASIN_TABLE[(int) (p_14090_ * 65535.0F) & '\uffff'];
    }

    public static float acos(float p_14090_) {
        return ACOS[(int) (p_14090_ * 65535.0F) & '\uffff'];
    }

    public static double acos(double p_14090_) {
        return ACOS_TABLE[(int) (p_14090_ * 65535.0F) & '\uffff'];
    }

    public static float approachLinear(float a, float b, float max) {
        return a > b ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
    }

    public static double approachLinear(double a, double b, double max) {
        return a > b ? (a - b < max ? b : a - max) : (b - a < max ? b : a + max);
    }

    public static float interpolate(float a, float b, float d) {
        return a + (b - a) * d;
    }

    public static double interpolate(double a, double b, double d) {
        return a + (b - a) * d;
    }

    public static double approachExp(double a, double b, double ratio) {
        return a + (b - a) * ratio;
    }

    public static double approachExp(double a, double b, double ratio, double cap) {
        double d = (b - a) * ratio;
        if (Math.abs(d) > cap) {
            d = Math.signum(d) * cap;
        }

        return a + d;
    }

    public static double retreatExp(double a, double b, double c, double ratio, double kick) {
        double d = (Math.abs(c - a) + kick) * ratio;
        return d > Math.abs(b - a) ? b : a + Math.signum(b - a) * d;
    }

    public static double clip(double value, double min, double max) {
        if (value > max) {
            value = max;
        }

        if (value < min) {
            value = min;
        }

        return value;
    }

    public static float clip(float value, float min, float max) {
        if (value > max) {
            value = max;
        }

        if (value < min) {
            value = min;
        }

        return value;
    }

    public static int clip(int value, int min, int max) {
        if (value > max) {
            value = max;
        }

        if (value < min) {
            value = min;
        }

        return value;
    }

    public static double map(double valueIn, double inMin, double inMax, double outMin, double outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static float map(float valueIn, float inMin, float inMax, float outMin, float outMax) {
        return (valueIn - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }

    public static double round(double number, double multiplier) {
        return (double) Math.round(number * multiplier) / multiplier;
    }

    public static float round(float number, float multiplier) {
        return (float) Math.round(number * multiplier) / multiplier;
    }

    public static boolean between(double min, double value, double max) {
        return min <= value && value <= max;
    }

    public static int approachExpI(int a, int b, double ratio) {
        int r = (int) Math.round(approachExp(a, b, ratio));
        return r == a ? b : r;
    }

    public static int retreatExpI(int a, int b, int c, double ratio, int kick) {
        int r = (int) Math.round(retreatExp(a, b, c, ratio, kick));
        return r == a ? b : r;
    }

    public static int floor(double d) {
        int i = (int) d;
        return d < (double) i ? i - 1 : i;
    }

    public static int floor(float f) {
        int i = (int) f;
        return f < (float) i ? i - 1 : i;
    }

    public static int ceil(double d) {
        int i = (int) d;
        return d > (double) i ? i + 1 : i;
    }

    public static int ceil(float f) {
        int i = (int) f;
        return f > (float) i ? i + 1 : i;
    }

    public static float sqrt(float f) {
        return (float) Math.sqrt(f);
    }

    public static float sqrt(double f) {
        return (float) Math.sqrt(f);
    }

    public static int roundAway(double d) {
        return (int) (d < 0.0 ? Math.floor(d) : Math.ceil(d));
    }

    public static int compare(int a, int b) {
        return Integer.compare(a, b);
    }

    public static int compare(double a, double b) {
        return Double.compare(a, b);
    }

    public static BlockPos min(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.min(pos1.getX(), pos2.getX()), Math.min(pos1.getY(), pos2.getY()), Math.min(pos1.getZ(), pos2.getZ()));
    }

    public static BlockPos max(Vec3i pos1, Vec3i pos2) {
        return new BlockPos(Math.max(pos1.getX(), pos2.getX()), Math.max(pos1.getY(), pos2.getY()), Math.max(pos1.getZ(), pos2.getZ()));
    }

    public static int absSum(BlockPos pos) {
        return Math.abs(pos.getX()) + Math.abs(pos.getY()) + Math.abs(pos.getZ());
    }

    public static boolean isAxial(BlockPos pos) {
        return pos.getX() == 0 ? pos.getY() == 0 || pos.getZ() == 0 : pos.getY() == 0 && pos.getZ() == 0;
    }

    public static int toSide(BlockPos pos) {
        Direction side = getSide(pos);
        return side == null ? -1 : side.get3DDataValue();
    }

    public static Direction getSide(BlockPos pos) {
        if (!isAxial(pos)) {
            return null;
        } else if (pos.getY() < 0) {
            return Direction.DOWN;
        } else if (pos.getY() > 0) {
            return Direction.UP;
        } else if (pos.getZ() < 0) {
            return Direction.NORTH;
        } else if (pos.getZ() > 0) {
            return Direction.SOUTH;
        } else if (pos.getX() < 0) {
            return Direction.WEST;
        } else {
            return pos.getX() > 0 ? Direction.EAST : null;
        }
    }

    public static void clamp(Vector2f src, float xm, float ym, float xM, float yM) {
        src.x = Mth.clamp(src.x, xm, xM);
        src.y = Mth.clamp(src.y, ym, yM);
    }

    /**
     * @param color int color
     * @return ARGB
     */
    public static int[] iColorToFloats(int color) {
        return new int[]{color >>> 24, color >> 16 & 255, color >> 8 & 255, color & 255};
    }
}
