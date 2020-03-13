package com.goldenglow.common.util;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.blocks.tiles.TileScripted;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityCustomNpc;

import javax.script.ScriptEngine;
import java.lang.reflect.Field;

public class ScriptDataMethods {
    public static ScriptObjectMirror getJavascriptObject(NPCWrapper npc, String variable, int page){
        Field f = null;
        try {
            f = ScriptContainer.class.getDeclaredField("engine");
            f.setAccessible(true);
            ScriptEngine engine = (ScriptEngine)f.get(((EntityCustomNpc)npc.getMCEntity()).script.getScripts().get(page));
            return (ScriptObjectMirror)engine.getContext().getAttribute(variable);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ScriptObjectMirror getJavascriptObject(NPCWrapper npc, String variable){
        Field f = null;
        try {
            f = ScriptContainer.class.getDeclaredField("engine");
            f.setAccessible(true);
            ScriptEngine engine = (ScriptEngine)f.get(((EntityCustomNpc)npc.getMCEntity()).script.getScripts().get(0));
            return (ScriptObjectMirror)engine.getContext().getAttribute(variable);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T getScriptedValue(NPCWrapper npc, String fieldName) {
        Field f = null;
        try {
            f = ScriptContainer.class.getDeclaredField("engine");
            f.setAccessible(true);
            ScriptEngine engine = (ScriptEngine)f.get(((EntityCustomNpc)npc.getMCEntity()).script.getScripts().get(0));
            return (T) engine.getContext().getAttribute(fieldName);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
