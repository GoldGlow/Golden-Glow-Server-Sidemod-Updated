package com.goldenglow.common.util.scripting;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.inventory.CustomInventoryData;
import com.goldenglow.common.keyItems.OOTMDummy;
import com.pixelmonmod.pixelmon.items.ItemTM;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class InventoryFunctions {

    //Add a key item to the player's Key Items Pocket. Used for different main and side quests
    public static void addKeyItem(PlayerWrapper playerWrapper, String itemStack){
        IPlayerData playerData = playerWrapper.getMCEntity().getCapability(OOPlayerProvider.OO_DATA, null);
        if(GoldenGlow.customItemManager.getKeyItem(itemStack)!=null){
            playerData.addKeyItem(itemStack);
        }
    }

    //Remove Key items from quests when they're completed/you give the item
    public static void removeKeyItem(PlayerWrapper playerWrapper, String displayName){
        IPlayerData playerData = playerWrapper.getMCEntity().getCapability(OOPlayerProvider.OO_DATA, null);
        playerData.removeKeyItem(displayName);
    }

    public static void createCustomChest(EntityPlayerMP playerMP, String inventoryName){
        CustomInventoryData data=null;
        for(CustomInventoryData inventoryData: GoldenGlow.customInventoryHandler.inventories){
            if(inventoryData.getName().equals(inventoryName)){
                data=inventoryData;
            }
        }
        if(data!=null){
        }
    }

    public static void addAwItem(PlayerWrapper player, String item){
        IPlayerData playerData = player.getMCEntity().getCapability(OOPlayerProvider.OO_DATA, null);
        ItemStack itemStack=null;
        try {
            itemStack=new ItemStack(JsonToNBT.getTagFromJson(item));
        } catch (NBTException e) {
            e.printStackTrace();
        }
        if(itemStack!=null)
            playerData.addAWItem(item);
    }

    public static void addAwFromName(PlayerWrapper player, String name){
        IPlayerData playerData = player.getMCEntity().getCapability(OOPlayerProvider.OO_DATA, null);
        playerData.addAWItem(name);
    }

    public static void clearAwItems(PlayerWrapper player){
        IPlayerData playerData = player.getMCEntity().getCapability(OOPlayerProvider.OO_DATA, null);
        playerData.clearAWItems();
    }

    //Used at a few pokeloots, probably for a quest too
    public static boolean unlockTM(PlayerWrapper player, String ItemID) {
        ItemStack tm=new ItemStack(Item.getByNameOrId(ItemID));
        if(tm.getItem() instanceof ItemTM){
            IPlayerData playerData = player.getMCEntity().getCapability(OOPlayerProvider.OO_DATA, null);
            OOTMDummy ooTM=new OOTMDummy((ItemTM) tm.getItem());
            return playerData.unlockTM(ooTM);
        }
        return false;
    }
}
