package com.goldenglow.common.util.requirements.types;

import com.goldenglow.common.util.requirements.Requirement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import noppes.npcs.api.entity.IPlayer;

public class ItemRequirement implements Requirement {
    private final String name="ITEM";

    public String getName(){
        return this.name;
    }

    public boolean hasRequirement(String data, EntityPlayerMP player){
        try {
            if(player.inventory.hasItemStack(new ItemStack(JsonToNBT.getTagFromJson(data)))) {
                return true;
            }
        } catch (NBTException e) {
            e.printStackTrace();
        }
        return false;
    }
}
