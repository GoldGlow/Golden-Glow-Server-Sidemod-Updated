package com.goldenglow.common.keyItems;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Iterator;

public class CustomItemManager {
    File keyItemFile;
    public HashMap<String, KeyItem> keyItems=new HashMap<String, KeyItem>();
    public HashMap<String, ItemStack> ooItemStackCache=new HashMap<String, ItemStack>();

    public KeyItem getKeyItem(String name){
        return keyItems.get(name);
    }

    public void init(){
        keyItemFile=new File(Reference.configDir, "keyItems.json");
        if(!keyItemFile.exists()){
            try {
                keyItemFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            loadKeyItems();
        }
    }

    public void loadKeyItems(){
        GGLogger.info("Loading Key Items...");
        this.keyItems.clear();
        try {
            InputStream iStream = new FileInputStream(this.keyItemFile);
            JsonArray json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonArray();
            for(JsonElement e:json){
                JsonObject object=(JsonObject)e;
                if(object.has("name")&&object.has("desc")&&object.has("texture")){
                    String name=object.get("name").getAsString();
                    String description=object.get("desc").getAsString();
                    String texture=object.get("texture").getAsString();
                    if(!this.keyItems.containsKey(name)){
                        this.keyItems.put(name, new KeyItem(name, description, texture));
                    }
                    else{
                        GGLogger.info(name+" already exists!");
                    }
                }
            }
        }catch (IOException e){
        }
    }

    public void saveKeyItems() {
        Iterator<String> keys=this.keyItems.keySet().iterator();
        try{
            if (!keyItemFile.exists())
                keyItemFile.createNewFile();
            JsonWriter file=new JsonWriter(new FileWriter(keyItemFile));
            file.setIndent("\t");
            file.beginArray();
            while(keys.hasNext()){
                KeyItem item=keyItems.get(keys.next());
                file.beginObject();
                file.name("name").value(item.name);
                file.name("desc").value(item.description);
                file.name("texture").value(item.resourceLocation);
                file.endObject();
            }
            file.endArray();
            file.close();
        }
        catch (IOException e){
        }
    }

    public String getItemName(OOItem item){
        if(this.ooItemStackCache.containsKey(item.getItemId())){
            return this.ooItemStackCache.get(item.getItemId()).getDisplayName();
        }
        ItemStack itemStack=new ItemStack(Item.getByNameOrId(item.getItemId()));
        this.ooItemStackCache.put(item.getItemId(), itemStack);
        return itemStack.getDisplayName();
    }
}
