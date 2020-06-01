package com.goldenglow.common.guis.pokehelper.config.optionsTypes.music;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.TitleType;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class OptionWildBattle implements OptionType {
    private String name="Wild Battle Theme";
    private ArrayList<SingleOptionData> options=new ArrayList<SingleOptionData>();
    File dir;

    public OptionWildBattle(){
        dir = new File(Reference.settingsDir, "wild_themes.json");
        if(!dir.exists()) {
            if (!dir.getParentFile().exists())
                dir.getParentFile().mkdirs();
            try {
                dir.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            this.loadThemes();
        }
    }

    public void loadThemes(){
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(dir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonArray json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonArray();
        for(JsonElement element:json){
            JsonObject object=element.getAsJsonObject();
            ThemeType themeType=null;
            if(object.has("lockedDescription")&&object.has("permission")){
                themeType=new ThemeType(object.get("name").getAsString(), object.get("description").getAsString(), object.get("value").getAsString(), object.get("lockedDescription").getAsString(), object.get("permission").getAsString());
            }
            else{
                themeType=new ThemeType(object.get("name").getAsString(), object.get("description").getAsString(), object.get("value").getAsString());
            }
            this.options.add(themeType);
        }
    }

    public String getName(){
        return this.name;
    }

    public ThemeType getOption(String name){
        for(SingleOptionData option:options){
            if(((ThemeType)option).getName().equals(name)){
                return (ThemeType)option;
            }
        }
        return null;
    }

    public ThemeType getOptionFromValue(String value){
        for(SingleOptionData option:options){
            if(((ThemeType)option).getValue().equals(value)){
                return (ThemeType)option;
            }
        }
        return null;
    }

    public ArrayList<SingleOptionData> getOptions(){
        return this.options;
    }

    public ArrayList<SingleOptionData> getSortedOptions(ArrayList<SingleOptionData> options, EntityPlayerMP playerMP){
        return options;
    }
}
