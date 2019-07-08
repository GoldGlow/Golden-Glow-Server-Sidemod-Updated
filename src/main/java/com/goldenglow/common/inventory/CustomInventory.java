package com.goldenglow.common.inventory;

import com.goldenglow.common.util.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.api.IContainer;
import noppes.npcs.api.IContainerCustomChest;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.ItemStackWrapper;
import noppes.npcs.containers.ContainerCustomChest;

/**
 * Created by JeanMarc on 6/18/2019.
 */
public class CustomInventory extends ContainerChest {

    CustomInventoryData data;

    public CustomInventory(IInventory playerInv, IInventory chestInv, EntityPlayerMP playerMP){
        super(playerInv, chestInv, playerMP);
    }

    public boolean canInteractWith(EntityPlayerMP player){
        return true;
    }

    public static CustomItem getItem(CustomItem[] items, EntityPlayerMP player){
        for(CustomItem item: items){
            if(Requirement.checkRequirements(item.requirements, player)){
                return item;
            }
        }
        return null;
    }

    public void setData(CustomInventoryData data){
        this.data=data;
    }

    public CustomInventoryData getData(){
        return this.data;
    }

    public static void openCustomInventory(EntityPlayerMP playerMP, CustomInventoryData data){
        InventoryBasic chestInventory=new InventoryBasic(data.getName(), true, data.getRows()*9);
        if(Requirement.checkRequirements(data.requirements, playerMP)) {
            for(int i=0;i<data.getRows()*9;i++){
                if(i<data.getItems().length){
                    CustomItem item= CustomInventory.getItem(data.getItems()[i], playerMP);
                    if(item.getItem()!=null){
                        chestInventory.setInventorySlotContents(i, item.getItem());
                    }
                }
            }
            playerMP.getNextWindowId();
            playerMP.connection.sendPacket(new SPacketOpenWindow(playerMP.currentWindowId, "minecraft:container", new TextComponentString(data.getName()), data.getRows() * 9));
            playerMP.openContainer = new CustomInventory(playerMP.inventory, chestInventory, playerMP);
            playerMP.openContainer.windowId = playerMP.currentWindowId;
            playerMP.openContainer.addListener(playerMP);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
        }
    }
}
