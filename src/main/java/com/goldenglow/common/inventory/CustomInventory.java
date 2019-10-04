package com.goldenglow.common.inventory;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Requirement;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.config.PixelmonItemsPokeballs;
import com.pixelmonmod.pixelmon.items.ItemPixelmonSprite;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.*;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import noppes.npcs.api.item.IItemStack;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Created by JeanMarc on 6/18/2019.
 */
public class CustomInventory extends ContainerChest {

    CustomInventoryData data;

    public CustomInventory(IInventory playerInv, IInventory chestInv, EntityPlayerMP playerMP){
        super(playerInv, chestInv, playerMP);
    }

    public static void openInventory(String inventoryName, EntityPlayerMP player){
        if(inventoryName.equalsIgnoreCase("seals")) {
            SealsInventory.openCustomSealsInventory(player);
            return;
        }
        else if (inventoryName.contains("sealChoice")) {
            SealsInventory.openSealSelection(player, Integer.parseInt(inventoryName.split(" ")[1]));
            return;
        }
        else if(inventoryName.equalsIgnoreCase("KeyItems")){
            BagInventories.openKeyItems(player);
            return;
        }
        else if(inventoryName.equalsIgnoreCase("TMCase")){
            BagInventories.openTMCase(player);
        }
        else if(inventoryName.equalsIgnoreCase("Party")){
            CustomInventory.openPartyInventoryTest(player);
            return;
        }
        for(CustomInventoryData inventoryData: GoldenGlow.customInventoryHandler.inventories) {
            if (inventoryData.getName().equals(inventoryName)) {
                CustomInventory.openCustomInventory(player, inventoryData);
                return;
            }
        }
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

    public static ItemStack[] getPartyIcons(EntityPlayerMP player){
        ItemStack[] party=new ItemStack[6];
        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player);
        for(int i = 0; i < 6; i++) {
            ItemStack stack = new ItemStack(Blocks.BARRIER);
            if(partyStorage.get(i)!=null) {
                stack = ItemPixelmonSprite.getPhoto(partyStorage.get(i));
                stack.setStackDisplayName(partyStorage.get(i).getSpecies().name);
            }
            if(!stack.equals(new ItemStack(Blocks.BARRIER)))
                party[i]=stack;
        }
        return party;
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
                    if(item!=null){
                        if(item.getItem()!=null) {
                            chestInventory.setInventorySlotContents(i, item.getItem());
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

    public static void openPartyInventoryTest(EntityPlayerMP playerMP){
        InventoryBasic chestInventory=new InventoryBasic("Party", true, 9);
        ItemStack[] party=CustomInventory.getPartyIcons(playerMP);
        CustomInventoryData data=new CustomInventoryData(1, "Party", "Party", new CustomItem[9][], null);
        for(int i=0;i<party.length;i++){
            if(party[i]!=null) {
                data.items[i] = new CustomItem[]{new CustomItem(party[i], null)};
                chestInventory.setInventorySlotContents(i, party[i]);
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
