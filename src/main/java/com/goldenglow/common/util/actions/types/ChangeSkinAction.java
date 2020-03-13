package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.util.actions.Action;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;

public class ChangeSkinAction implements Action {
    public final String name="CHANGESKIN";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        String[] words=value.split(" ");
        String name="";
        player.getHeldItemMainhand().setItemDamage(Integer.parseInt(words[0]));
        for(int i=1;i<words.length;i++){
            if(i>1){
                name+=" ";
            }
            name+=words[i];
        }
        player.getHeldItemMainhand().setStackDisplayName(name);
    }
}