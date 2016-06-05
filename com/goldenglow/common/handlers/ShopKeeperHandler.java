package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.realmsclient.util.JsonUtils;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperData;
import com.pixelmonmod.pixelmon.enums.EnumShopKeeperType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import noppes.npcs.Server;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by JeanMarc on 5/28/2016.
 */
public class ShopKeeperHandler {
    public ArrayList<NPCShopkeeper> shopkeepers;
    public HashMap<String, BaseShopItem> shopItems;
    File items;
    File salesmen;

    public ShopKeeperHandler(){
        shopItems=new HashMap<String, BaseShopItem>();
        shopkeepers=new ArrayList<NPCShopkeeper>();
    }

    public void init(){
        items = new File(Reference.configDir, "shops/items.json");
        salesmen = new File(Reference.configDir, "shops/shopkeepers.json");
        if(!items.exists()) {
            if (!items.getParentFile().exists())
                items.getParentFile().mkdirs();
        }
        else {
                this.loadItems();
        }
        salesmen = new File(Reference.configDir, "shops/shopkeepers.json");
        if(!salesmen.exists()) {
            if (!salesmen.getParentFile().exists())
                salesmen.getParentFile().mkdirs();
        }
        else
            this.loadNpcs();
    }

    public void loadItems(){
        try {
            InputStream istream=new FileInputStream(items);
            JsonObject json = new JsonParser().parse(new InputStreamReader(istream, StandardCharsets.UTF_8)).getAsJsonObject();
            JsonArray array = json.getAsJsonArray("items");
            for(int i=0;i<array.size();i++){
                JsonObject object= array.get(i).getAsJsonObject();
                String itemName = object.get("name").getAsString();
                String id=null;
                if(object.has("id")){
                    id = object.get("id").getAsString();
                }
                else
                    id=itemName;
                int buy=-1;
                if(object.has("buy"))
                    buy=object.get("buy").getAsInt();
                int sell=-1;
                if(object.has("sell"))
                    sell=object.get("sell").getAsInt();
                int itemData=0;
                if(object.has("itemData"))
                    itemData=object.get("itemData").getAsInt();
                String nbtData=null;
                if(object.has("nbtData"))
                    nbtData=object.get("nbtData").getAsString();
                Item item = null;
                ResourceLocation loc = new ResourceLocation(itemName);
                item= GameRegistry.findItem(loc.getResourceDomain(), loc.getResourcePath());
                if(item == null){
                    GGLogger.info("Item cancel! Can't find: "+itemName);
                }
                else{
                    ItemStack itemStack = new ItemStack(item, 1, itemData);
                    if(nbtData!=null)
                        itemStack.setTagCompound(JsonToNBT.getTagFromJson(nbtData));
                    BaseShopItem baseItem = new BaseShopItem(id, itemStack, buy, sell);
                    this.shopItems.put(id, baseItem);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NBTException e) {
            e.printStackTrace();
        }
    }

    public void loadNpcs(){
        try {
            InputStream istream = new FileInputStream(salesmen);
            JsonArray json = new JsonParser().parse(new InputStreamReader(istream, StandardCharsets.UTF_8)).getAsJsonArray();
            for(int i=0;i<json.size();i++){
                JsonObject object= json.get(i).getAsJsonObject();
                ShopkeeperData data = new ShopkeeperData("Apu");
                data.type= EnumShopKeeperType.PokemartMain;
                data.addChat("", "");
                if(object.has("items")){
                    JsonArray items=object.get("items").getAsJsonArray();
                    for(int j=0;j<items.size();j++){
                        JsonObject item = items.get(j).getAsJsonObject();
                        String itemName = item.get("name").getAsString();
                        float multi=1.0F;
                        float rarity=1.0F;
                        boolean canPriceVary=false;
                        if(!shopItems.containsKey(itemName))
                            GGLogger.info("No Shop Item found for: "+itemName);
                        else{
                            ShopItem shopItem = new ShopItem(shopItems.get(itemName), multi, rarity, canPriceVary);
                            data.addItem(shopItem);
                            ArrayList<String> names= ObfuscationReflectionHelper.getPrivateValue(ShopkeeperData.class, data, 3);
                            names.add("Pokemart");
                            ObfuscationReflectionHelper.setPrivateValue(ShopkeeperData.class,data,names,3);
                            NPCShopkeeper shop= new NPCShopkeeper(DimensionManager.getWorld(0));
                            shop.init(data);
                            shop.setId(this.shopkeepers.size());
                            this.shopkeepers.add(shop);
                        }
                    }
                }
        }} catch (FileNotFoundException e) {
            e.printStackTrace();
        }


    }
}
