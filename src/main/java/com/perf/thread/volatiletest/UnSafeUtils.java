package com.perf.thread.volatiletest;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnSafeUtils {
    public static Unsafe getUnsafe() {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            return (Unsafe)field.get(null);

        } catch (Exception e) {
        }
        return null;
    }
    private static long normalize(int value) {
        if(value >= 0) return value;
        return (0L >>> 32) & value;
    }
}
