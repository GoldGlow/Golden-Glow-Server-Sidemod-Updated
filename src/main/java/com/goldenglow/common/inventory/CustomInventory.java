package com.goldenglow.common.inventory;

import com.goldenglow.common.util.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.IContainerCustomChest;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.containers.ContainerCustomChest;

/**
 * Created by JeanMarc on 6/18/2019.
 */
public class CustomInventory extends ContainerCustomChest {

    String name;
    CustomItem[][] items;
    Requirement[] requirements;

    public CustomInventory(EntityPlayerMP player, int rows, String name, CustomItem[][] items, Requirement[] requirements){
        super(player, rows);
        this.name=name;
        this.items=items;
        this.requirements=requirements;
        ((IContainerCustomChest)this).setName(name);
        for(int i=0;i<rows*9;i++){
            if(i<items.length){
                CustomItem item=getItem(items[i], player);
                if(item.item!=null){
                    ((IContainer)this).setSlot(i, (NpcAPI.Instance().getIItemStack(item.item)));
                }
            }
        }
    }

    public boolean canInteractWith(EntityPlayerMP player){
        return Requirement.checkRequirements(requirements, player);
    }

    public CustomItem getItem(CustomItem[] items, EntityPlayerMP player){
        for(CustomItem item: items){
            if(Requirement.checkRequirements(item.requirements, player)){
                return item;
            }
        }
        return null;
    }
}
