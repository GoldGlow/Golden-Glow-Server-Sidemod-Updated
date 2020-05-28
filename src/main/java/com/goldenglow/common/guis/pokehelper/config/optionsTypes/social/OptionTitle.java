package com.goldenglow.common.guis.pokehelper.config.optionsTypes.social;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class OptionTitle implements OptionType {
    private String name="Title";
    private ArrayList<SingleOptionData> options=new ArrayList<SingleOptionData>();
    File dir;

    public OptionTitle(){
        dir = new File(Reference.settingsDir, "titles.json");
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
            this.loadTitles();
        }
    }

    public void loadTitles(){
        InputStream iStream = null;
        try {
            iStream = new FileInputStream(dir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        JsonArray json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonArray();
        for(JsonElement element:json){
            JsonObject object=element.getAsJsonObject();
            TitleType titleType=new TitleType(object.get("name").getAsString(), object.get("unlockedDescription").getAsString(), object.get("lockedDescription").getAsString(), object.get("permission").getAsString());
            this.options.add(titleType);
        }
    }

    public String getName(){
        return this.name;
    }

    public TitleType getOption(String name){
        for(SingleOptionData option:options){
            if(((TitleType)option).getUnlockedName().equals(name)){
                return (TitleType)option;
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

    public String[] getNames(EntityPlayerMP playerMP){
        String[] names=new String[this.options.size()];
        for(int i=0;i<this.options.size();i++){
            names[i]=((TitleType)this.options.get(i)).getProperName(playerMP);
        }
        return names;
    }

    public String[] getDescriptions(EntityPlayerMP playerMP){
        String[] descriptions=new String[this.options.size()];
        for(int i=0;i<this.options.size();i++){
            descriptions[i]=((TitleType)this.options.get(i)).getProperDescription(playerMP);
        }
        return descriptions;
    }
}
