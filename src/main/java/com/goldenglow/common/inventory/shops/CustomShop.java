package com.goldenglow.common.inventory.shops;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.inventory.CustomInventory;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.goldenglow.common.util.scripting.OtherFunctions;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.economy.IPixelmonBankAccount;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class CustomShop extends CustomInventory {
    CustomShopData data;

    public CustomShop(IInventory playerInv, IInventory chestInv, EntityPlayerMP playerMP){
        super(playerInv, chestInv, playerMP);
    }

    public static CustomShopItem getItem(CustomShopItem[] items, EntityPlayerMP player) {
        if (items.length>0){
            for (CustomShopItem item : items) {
                if (item != null) {
                    if (PixelmonEssentials.requirementHandler.checkRequirements(item.getRequirements(), player)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public void setData(CustomShopData data){this.data=data;}

    public CustomShopData getData(){
        return this.data;
    }

    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player) {
        InventoryPlayer inventoryplayer = player.inventory;
        Slot slot = getSlot(slotId);

        if(slot.isSameInventory(getSlot(0))) {
            CustomShopItem item = CustomShop.getItem(data.getItems()[slotId], (EntityPlayerMP) player);
            if (item != null) {
                if (dragType == 0) {
                        boolean didAction=false;
                        for (ActionData action : item.getLeftClickActions()) {
                            if(action instanceof ActionStringData){
                                if (PixelmonEssentials.requirementHandler.checkRequirements(action.requirements, (EntityPlayerMP) player)) {
                                    IPixelmonBankAccount bankAccount = (IPixelmonBankAccount) Pixelmon.moneyManager.getBankAccount((EntityPlayerMP) player).orElse(null);
                                    if (((ActionStringData) action).getValue().startsWith("pokegive")) {
                                        ((EntityPlayerMP) player).sendMessage(new TextComponentString("Successfully bought " + ((ActionStringData) action).getValue().split(" ")[2] + "!"));
                                        Action actionFinal=PixelmonEssentials.actionHandler.getType(action);
                                        bankAccount.changeMoney(-1 * item.buyPrice);
                                        actionFinal.doAction((EntityPlayerMP)player, action);
                                        didAction=true;
                                        break;
                                    }
                                    else if(action.name.equals("DEPOSITORY_POKEMON")){
                                        ((EntityPlayerMP) player).sendMessage(new TextComponentString("Successfully bought " + ((ActionStringData) action).getValue().split(" ")[0] + "!"));
                                        bankAccount.changeMoney(-1 * item.buyPrice);
                                        Action actionFinal=PixelmonEssentials.actionHandler.getType(action);
                                        actionFinal.doAction((EntityPlayerMP)player, action);
                                        didAction=true;
                                        break;
                                    }
                                }
                            }
                        }
                        if(!didAction) {
                            ICommandManager icommandmanager = ((EntityPlayerMP)player).getEntityWorld().getMinecraftServer().getCommandManager();
                            icommandmanager.executeCommand(new RConConsoleSource(((EntityPlayerMP)player).getEntityWorld().getMinecraftServer()), "tellraw "+((EntityPlayerMP)player).getName()+" [\"\",{\"text\":\"You can't buy this!\",\"color\":\"dark_red\"}]");
                        }else {
                            if(this.data.getName().equals("Starter")){
                                player.closeScreen();
                                new PlayerWrapper((EntityPlayerMP)player).showDialog(328, "Poke Depository Clerk");
                            }
                            else{
                                openCustomShop(((EntityPlayerMP) player), data);
                            }
                        }
                        return null;
                } else if (dragType == 1) {
                    for (ActionData action : item.getRightClickActions()) {
                        if(action instanceof ActionStringData){
                            if (PixelmonEssentials.requirementHandler.checkRequirements(action.requirements, (EntityPlayerMP) player)) {
                                PlayerWrapper playerWrapper=new PlayerWrapper((EntityPlayerMP) player);
                                IItemStack iItemStack= NpcAPI.Instance().getIItemStack(item.getItem());
                                playerWrapper.removeItem(iItemStack, 1);
                                Action actionFinal=PixelmonEssentials.actionHandler.getType(action);
                                actionFinal.doAction( (EntityPlayerMP)player, action);
                                return null;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public static void openCustomShop(EntityPlayerMP playerMP, CustomShopData data){
        if(data.pixelmonGui){
            GGLogger.info("test");
            if (PixelmonEssentials.requirementHandler.checkRequirements(data.getRequirements(), playerMP)) {
                OtherFunctions.openShopMenu(new PlayerWrapper(playerMP), data);
            }
        }
        else {
            InventoryBasic chestInventory = new InventoryBasic(data.getName(), true, data.getRows() * 9);
            if (PixelmonEssentials.requirementHandler.checkRequirements(data.getRequirements(), playerMP)) {
                for (int i = 0; i < data.getRows() * 9 - 1; i++) {
                    if (i < data.getItems().length) {
                        CustomShopItem item = CustomShop.getItem(data.getItems()[i], playerMP);
                        if (item != null) {
                            if (item.getItem() != null) {
                                chestInventory.setInventorySlotContents(i, item.getItem());
                            }
                        }
                    }
                }
                ItemStack balance = new ItemStack(Item.getByNameOrId("variedcommodities:coin_bronze"));
                IPixelmonBankAccount bankAccount = (IPixelmonBankAccount) Pixelmon.moneyManager.getBankAccount(playerMP).orElse(null);
                int amount = 0;
                if (bankAccount != null) {
                    amount = bankAccount.getMoney();
                }
                balance.setStackDisplayName(Reference.resetText + "Current Balance: " + amount);
                chestInventory.setInventorySlotContents(data.getRows() * 9 - 1, balance);
                playerMP.getNextWindowId();
                playerMP.connection.sendPacket(new SPacketOpenWindow(playerMP.currentWindowId, "minecraft:container", new TextComponentString(data.getDisplayName()), data.getRows() * 9));
                playerMP.openContainer = new CustomShop(playerMP.inventory, chestInventory, playerMP);
                ((CustomShop) playerMP.openContainer).setData(data);
                playerMP.openContainer.windowId = playerMP.currentWindowId;
                playerMP.openContainer.addListener(playerMP);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
            }
        }
    }
}
