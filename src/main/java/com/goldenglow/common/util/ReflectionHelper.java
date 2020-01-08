package com.goldenglow.common.util;

import net.minecraft.item.ItemStack;

import java.lang.reflect.Field;

public class ReflectionHelper {

    public static String getPrivateString(Object o, String field) throws NoSuchFieldException, IllegalAccessException {
        Field privateField = o.getClass().getDeclaredField(field);
        privateField.setAccessible(true);
        String s = (String)privateField.get(o);
        return s;
    }

    public static ItemStack getPrivateStack(Object o, String field) throws NoSuchFieldException, IllegalAccessException {
        Field privateField = o.getClass().getDeclaredField(field);
        privateField.setAccessible(true);
        ItemStack stack = (ItemStack)privateField.get(o);
        return stack;
    }

    public static int getPrivateInt(Object o, String field) throws NoSuchFieldException, IllegalAccessException {
        Field privateField = o.getClass().getDeclaredField(field);
        privateField.setAccessible(true);
        int num = (int)privateField.get(o);
        return num;
    }

    public static void setPrivateBoolean(Object o, String field, boolean value)  throws NoSuchFieldException, IllegalAccessException {
        Field privateField = o.getClass().getDeclaredField(field);
        privateField.setAccessible(true);
        privateField.set(o, value);
    }

}
