package com.goldenglow.common.inventory;

import com.goldenglow.common.data.OOPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Requirement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;

import java.util.List;

public class BagInventories {
    static void openKeyItems(EntityPlayerMP player){
        OOPlayerData playerData = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<ItemStack> items=playerData.getKeyItems();
        int rows= Math.max(((items.size()-1)/9)+1, 1);
        CustomInventoryData data=new CustomInventoryData(rows, "KeyItems", "Key Items", new CustomItem[rows*9][], new Requirement[0]);
        InventoryBasic chestInventory=new InventoryBasic(data.getName(), true, data.getRows()*9);
        GGLogger.info("Opening Key Items");
        for(int i=0;i<items.size();i++){
            GGLogger.info("Adding item: "+items.get(i).serializeNBT());
            data.items[i]=new CustomItem[]{new CustomItem(items.get(i), null)};
            chestInventory.setInventorySlotContents(i, items.get(i));
        }
        player.getNextWindowId();
        player.connection.sendPacket(new SPacketOpenWindow(player.currentWindowId, "minecraft:container", new TextComponentString("Key Items"), rows*9));
        player.openContainer = new CustomInventory(player.inventory, chestInventory, player);
        ((CustomInventory)player.openContainer).setData(data);
        player.openContainer.windowId = player.currentWindowId;
        player.openContainer.addListener(player);
        MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, player.openContainer));
    }
}
