package com.goldenglow.common.guis.pokehelper.bag;

import com.goldenglow.common.guis.pokehelper.bag.category.*;
import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonmod.pixelmon.items.ItemPPRestore;
import com.pixelmonmod.pixelmon.items.ItemPokeball;
import com.pixelmonmod.pixelmon.items.ItemPotion;
import com.pixelmonmod.pixelmon.items.ItemStatusAilmentHealer;
import com.pixelmonmod.pixelmon.items.heldItems.ItemBerry;
import moe.plushie.armourers_workshop.common.init.items.ItemSkin;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;

public class CategoryManager {
    private ArrayList<BagCategory> categories;
    private ArrayList<String> blacklistedItems;

    public CategoryManager(){
        this.categories=new ArrayList<BagCategory>();
        this.blacklistedItems=new ArrayList<String>();
    }

    public void init(){
        this.categories.add(new ArmourersItems());
        this.categories.add(new BerryItems());
        this.categories.add(new InventoryItems());
        this.categories.add(new Items());
        this.categories.add(new KeyItems());
        this.categories.add(new MedicineItems());
        this.categories.add(new Pokeballitems());
        this.categories.add(new TmItems());

        blacklistedItems.add("variedcommodities:diamond_dagger");
    }

    public boolean isBlacklisted(String itemId){
        return this.blacklistedItems.contains(itemId);
    }

    public BagCategory getCategory(String name){
        for(int i=0;i<categories.size();i++){
            if(categories.get(i).getName().equals(name)){
                return categories.get(i);
            }
        }
        return null;
    }

    public BagCategory getCategory(Item item){
        if(item instanceof ItemBerry){
            return this.getCategory("Berries");
        }
        else if(item instanceof ItemSkin){
            return this.getCategory("Clothes");
        }
        else if(item instanceof ItemPPRestore||item instanceof ItemStatusAilmentHealer||item instanceof ItemPotion){
            return this.getCategory("Medicine");
        }
        else if(item instanceof ItemPokeball){
            return this.getCategory("Pokeballs");
        }
        return this.getCategory("Items");
    }
}
