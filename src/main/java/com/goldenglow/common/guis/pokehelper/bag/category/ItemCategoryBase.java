package com.goldenglow.common.guis.pokehelper.bag.category;

import com.goldenglow.common.guis.pokehelper.bag.BagCategory;
import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;

import java.util.Iterator;
import java.util.List;

public class ItemCategoryBase implements BagCategory {
    String name;

    public ItemCategoryBase(String name){
        this.name=name;
    }

    public String getName(){
        return this.name;
    }

    public String[] getItemNames(EntityPlayerMP playerMP){
        return new String[0];
    }

    public CustomGuiWrapper generateButtons(EntityPlayerMP player, CustomGuiWrapper gui, EssentialsGuis essentialsGui){
        return gui;
    }

    public int[] getButtonIds(){
        return new int[0];
    }

    public CustomGuiWrapper displayItem(EntityPlayerMP player, CustomGuiWrapper gui){
        return CustomGuiController.getOpenGui(player);
    }

    public void storeItem(EntityPlayerMP player, OOItem item){
    }

    public void withdrawItem(EntityPlayerMP player, OOItem item){
    }

    public int removeFromInventory(EntityPlayerMP player, OOItem item){
        int remainingCount=item.getQuantity();
        Iterator<ItemStack> itemSlots=player.inventory.mainInventory.iterator();
        while(itemSlots.hasNext()){
            ItemStack itemStack=itemSlots.next();
            if(itemStack.getItem().getRegistryName().toString().equals(item.getItemId())){
                if(itemStack.getCount()<remainingCount){
                    remainingCount-=itemStack.getCount();
                    itemStack.setCount(0);
                }
                else{
                    itemStack.setCount(itemStack.getCount()-remainingCount);
                    return 0;
                }
            }
        }
        return remainingCount;
    }

    public int addToInventory(EntityPlayerMP player, OOItem item){
        int remainingCount=item.getQuantity();
        Iterator<ItemStack> itemSlots=player.inventory.mainInventory.iterator();
        int itemSlot=0;
        while(itemSlots.hasNext()&&remainingCount>0){
            ItemStack itemStack=itemSlots.next();
            if(itemStack.getCount()==0){
                ItemStack newStack=new ItemStack(Item.getByNameOrId(item.getItemId()));
                int stackSize=Math.min(newStack.getMaxStackSize(), remainingCount);
                newStack.setCount(stackSize);
                player.inventory.setInventorySlotContents(itemSlot, newStack);
                remainingCount-=stackSize;
            }
            else if(itemStack.getItem().getRegistryName().toString().equals(item.getItemId())){
                int remainingSize=itemStack.getMaxStackSize()-itemStack.getCount();
                int countDifference=Math.min(remainingSize, remainingCount);
                itemStack.setCount(itemStack.getCount()+countDifference);
                player.inventory.setInventorySlotContents(itemSlot, itemStack);
                remainingCount-=countDifference;
            }
            itemSlot++;
        }
        return remainingCount;
    }

    public void useItem(OOItem item){
    }

    public OOItem getItem(EntityPlayerMP playerMP, int index){
        return null;
    }
}
