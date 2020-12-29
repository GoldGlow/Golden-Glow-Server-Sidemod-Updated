package com.goldenglow.common.keyItems;

import com.goldenglow.GoldenGlow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;

public class OOItem {
    private String itemId;
    private int quantity;

    public OOItem(String itemId, int quantity){
        this.itemId=itemId;
        this.quantity=quantity;
    }

    public String getItemId(){
        return this.itemId;
    }

    public int getQuantity(){
        return this.quantity;
    }

    public void changeQuantity(int amount){
        this.quantity+=amount;
    }

    public String getDisplayName(){
        return GoldenGlow.customItemManager.getItemName(this);
    }

    public NBTTagCompound toNBT(){
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setString("itemId", this.itemId);
        nbt.setInteger("quantity", this.quantity);
        return nbt;
    }

    public static OOItem fromNBT(NBTTagCompound nbt){
        return new OOItem(nbt.getString("itemId"), nbt.getInteger("quantity"));
    }
}
