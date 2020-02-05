package com.goldenglow.common.util.scripting;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.PermissionUtils;
import com.goldenglow.common.util.Scoreboards;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.*;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.world.WorldServer;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;

import java.util.ArrayList;

import java.util.UUID;

public class OtherFunctions {
    //Probably needs to be updated to use the MC notification system instead of CNPCs
    public static void showAchievement(PlayerWrapper playerWrapper, String firstLine, String secondLine){
        playerWrapper.sendNotification(firstLine, secondLine, Integer.valueOf(playerWrapper.getMCEntity().getEntityData().getInteger("RouteNotification")));
    }

    //Debug?
    public static void test() {
        System.out.println("Test works");
    }

    public static void unlockBugCatcher(EntityPlayerMP player){
        if(!PermissionUtils.checkPermission(player, "titles.bug_catcher")) {
            showAchievement(new PlayerWrapper(player), "Titles", "Unlocked title: Bug Catcher");
            PermissionUtils.addPermissionNode(player, "titles.bug_catcher");
            GGLogger.info("unlocked Bug Catcher");
        }
    }

    //Opens a dialog for a NPC. Used for other scripting stuff
    public static void openDialog(PlayerWrapper player, NPCWrapper npc, int dialogId){
        NoppesUtilServer.openDialog((EntityPlayerMP) player.getMCEntity(), (EntityNPCInterface) npc.getMCEntity(), (Dialog) DialogController.instance.get(dialogId));
    }

    public static void setScoreboard(PlayerWrapper player){
        Scoreboards.buildScoreboard((EntityPlayerMP)player.getMCEntity());
    }

    public static void equipArmor(EntityPlayerMP player, int slot, String item){
        ItemStack itemStack=null;
        try {
            itemStack=new ItemStack(JsonToNBT.getTagFromJson(item));
        } catch (NBTException e) {
            e.printStackTrace();
        }
        player.inventory.setInventorySlotContents(100+slot, itemStack);
    }

    public static ItemStack getNPCDialogItem(NPCWrapper npc) {
        IItemStack item=npc.getInventory().getDropItem(0);
        return item.getMCItemStack();
    }

    public static void openShopMenu(PlayerWrapper player, String name, String openMsg, String closeMsg) {
        ArrayList<ShopItemWithVariation> buyList = getBuyList(player);
        ArrayList<ShopItemWithVariation> sellList = getSellList(player);
        Pixelmon.network.sendTo(new SetNPCData(name, new ShopkeeperChat(openMsg,closeMsg), buyList, sellList), (EntityPlayerMP)player.getMCEntity());
        NPCShopkeeper shopkeeper = new NPCShopkeeper(player.getWorld().getMCWorld());
        shopkeeper.setId(999);
        shopkeeper.setPosition(player.getX(), player.getY(), player.getZ());
        ((EntityPlayerMP)player.getMCEntity()).connection.sendPacket(new SPacketSpawnMob(shopkeeper));
        OpenScreen.open((EntityPlayerMP)player.getMCEntity(), EnumGuiScreen.Shopkeeper, 999);
        ((EntityPlayerMP)player.getMCEntity()).removeEntity(shopkeeper);
    }

    public static ArrayList<ShopItemWithVariation> getBuyList(PlayerWrapper player) {
        ArrayList<ShopItemWithVariation> buyList = new ArrayList<>();
        buyList.add(new ShopItemWithVariation(new ShopItem(new BaseShopItem("minecraft:stick", new ItemStack(Items.STICK), 1, 2), 1,0, false)));
        return buyList;
    }

    public static ArrayList<ShopItemWithVariation> getSellList(PlayerWrapper player) {
        ArrayList<ShopItemWithVariation> sellList = new ArrayList<>();
        return sellList;
    }

    public static void addPlayerMark(PlayerWrapper player, int color){
        MarkData data=player.getMCEntity().getCapability(MarkData.MARKDATA_CAPABILITY, null);
        data.addMark(1, color);
    }

    public static void clearPlayerMarks(PlayerWrapper player){
        MarkData data=player.getMCEntity().getCapability(MarkData.MARKDATA_CAPABILITY, null);
        data.marks.clear();
        data.syncClients();
    }

    public static void vanishedNPC(NPCWrapper npc){
        Entity npcEntity=Sponge.getServer().getWorld(npc.getWorld().getName()).get().getEntity(UUID.fromString(npc.getUUID())).get();
        boolean visible=npcEntity.get(Keys.VANISH).orElse(false);
        npcEntity.offer(Keys.VANISH, !visible);
    }
}
