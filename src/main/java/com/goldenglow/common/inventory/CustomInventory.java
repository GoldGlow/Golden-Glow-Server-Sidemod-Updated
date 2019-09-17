package com.goldenglow.common.inventory;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
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
            CustomInventory.openCustomSealsInventory(player);
        }
        else if (inventoryName.contains("sealChoice")) {
            CustomInventory.openSealSelection(player, Integer.parseInt(inventoryName.split(" ")[1]));
        }
        for(CustomInventoryData inventoryData: GoldenGlow.customInventoryHandler.inventories) {
            if (inventoryData.getName().equals(inventoryName)) {
                CustomInventory.openCustomInventory(player, inventoryData);
            }
            return;
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

    static void openCustomSealsInventory(EntityPlayerMP player) {
        OOPlayerData playerData = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        CustomInventoryData data = new CustomInventoryData(9, "Seals", "Seals", new CustomItem[9][], null);
        InventoryBasic inventory = new InventoryBasic("Seals", true, 9);
        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player);
        for(int i = 0; i < 6; i++) {
            String name = playerData.getEquippedSeals()[i];
            if(name==null || name.isEmpty())
                name = "None";
            ItemStack stack = new ItemStack(Blocks.BARRIER);
            if(partyStorage.get(i)!=null)
                stack = ItemPixelmonSprite.getPhoto(partyStorage.get(i));
            inventory.setInventorySlotContents(i+1, stack.setStackDisplayName(TextFormatting.RESET+"Slot "+(i+1)+": "+name));
            data.items[i] = new CustomItem[]{ new CustomItem(stack, null).setLeftClickActions(new Action[]{ new Action(Action.ActionType.OPEN_INV, "sealChoice "+i) }) };
        }
        player.getNextWindowId();
        player.connection.sendPacket(new SPacketOpenWindow(player.currentWindowId, "minecraft:container", new TextComponentString(TextFormatting.GOLD+"Seals"), 9));
        player.openContainer = new CustomInventory(player.inventory, inventory, player);
        ((CustomInventory)player.openContainer).setData(data);
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addListener(player);
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.openContainer));
    }

    static void openSealSelection(EntityPlayerMP player, int slot) {
        Random r = new Random();
        OOPlayerData playerData = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<String> unlockedSeals = playerData.getUnlockedSeals();
        int rows = Math.max((int)Math.ceil(playerData.getUnlockedSeals().size()/6.0), 1);

        PlayerPartyStorage partyStorage = Pixelmon.storageManager.getParty(player);
        InventoryBasic inventory = new InventoryBasic("Choose Seal", true, rows*9);
        CustomInventoryData data = new CustomInventoryData(rows*9, "Choose a Seal: Slot "+slot, "Choose a Seal: Slot "+slot, new CustomItem[rows*9][], null);

        for(int i = 0; i < unlockedSeals.size(); i++) {
            int s = (((i/6)*9)+(i%6))+1;
            if(!Arrays.asList(playerData.getEquippedSeals()).contains(unlockedSeals.get(i))) {
                ItemStack stack = new ItemStack(PixelmonItemsPokeballs.getPokeballListNoMaster().get(r.nextInt(PixelmonItemsPokeballs.getPokeballListNoMaster().size()))).setStackDisplayName(unlockedSeals.get(i));
                inventory.setInventorySlotContents(s, stack);
                data.items[s] = new CustomItem[]{new CustomItem(stack, null).setLeftClickActions(new Action[]{new Action(Action.ActionType.SEAL_SET, unlockedSeals.get(i) + ":" + i)})};
            }
        }
        ItemStack stack = new ItemStack(Blocks.BARRIER).setStackDisplayName("Back");
        inventory.setInventorySlotContents(8, stack);
        data.items[8] = new CustomItem[]{new CustomItem(stack, null).setLeftClickActions(new Action[]{new Action(Action.ActionType.OPEN_INV, "seals")})};

        player.getNextWindowId();
        player.connection.sendPacket(new SPacketOpenWindow(player.currentWindowId, "minecraft:container", new TextComponentString(TextFormatting.GOLD+"Seal Choice: Slot "+slot), rows*9));
        player.openContainer = new CustomInventory(player.inventory, inventory, player);
        ((CustomInventory)player.openContainer).setData(data);
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addListener(player);
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.openContainer));
    }
}
