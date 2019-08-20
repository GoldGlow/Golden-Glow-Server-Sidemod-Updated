package com.goldenglow.common.inventory;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Requirement;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;

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

    public static CustomItem getItem(CustomItem[] items, EntityPlayerMP player) {
        if (items.length>0){
            for (CustomItem item : items) {
                if (item != null) {
                    if (Requirement.checkRequirements(item.requirements, player)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        InventoryPlayer inventoryplayer = player.inventory;
        GGLogger.info("Clicked "+slotId);
        Slot slot = getSlot(slotId);

        if(slot.isSameInventory(getSlot(0))) {
            CustomItem item = CustomInventory.getItem(data.getItems()[slotId], (EntityPlayerMP) player);
            if (item != null) {
                if (dragType == 0) {
                    for (Action action : item.leftClickActions) {
                        if (Requirement.checkRequirements(action.requirements, (EntityPlayerMP) player)) {
                            action.doAction((EntityPlayerMP) player);
                            return null;
                        }
                    }
                } else if (dragType == 1) {
                    for (Action action : item.rightClickActions) {
                        if (Requirement.checkRequirements(action.requirements, (EntityPlayerMP) player)) {
                            action.doAction((EntityPlayerMP) player);
                            return null;
                        }
                    }
                }
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
                    GGLogger.info("Loaded item in slot "+i);
                    CustomItem item= CustomInventory.getItem(data.getItems()[i], playerMP);
                    if(item!=null){
                        if(item.getItem()!=null) {
                            chestInventory.setInventorySlotContents(i, item.getItem());
                            GGLogger.info(Item.getIdFromItem(item.getItem().getItem()));
                        }
                    }
                }
            }
            playerMP.getNextWindowId();
            playerMP.connection.sendPacket(new SPacketOpenWindow(playerMP.currentWindowId, "minecraft:container", new TextComponentString(data.getDisplayName()), data.getRows() * 9));
            playerMP.openContainer = new CustomInventory(playerMP.inventory, chestInventory, playerMP);
            ((CustomInventory)playerMP.openContainer).setData(data);
            playerMP.openContainer.windowId = playerMP.currentWindowId;
            playerMP.openContainer.addListener(playerMP);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
        }
    }
}
