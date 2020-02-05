package com.goldenglow.common.util.scripting;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.PermissionUtils;
import com.goldenglow.common.util.Scoreboards;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.storage.PCStorage;
import com.pixelmonmod.pixelmon.comm.packetHandlers.OpenScreen;
import com.pixelmonmod.pixelmon.comm.packetHandlers.npc.SetNPCData;
import com.pixelmonmod.pixelmon.entities.npcs.NPCShopkeeper;
import com.pixelmonmod.pixelmon.entities.npcs.registry.BaseShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItem;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItemWithVariation;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopkeeperChat;
import com.pixelmonmod.pixelmon.enums.EnumGuiScreen;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.Server;
import noppes.npcs.api.item.IItemStack;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.constants.EnumPacketClient;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.PlayerDataController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.MarkData;
import noppes.npcs.controllers.data.PlayerData;
import noppes.npcs.entity.EntityNPCInterface;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.effect.sound.SoundTypes;
import org.spongepowered.api.entity.Entity;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;

import java.util.UUID;

public class OtherFunctions {

    public static void sendNotification(PlayerWrapper playerWrapper, String firstLine, String secondLine) {
        sendNotification(playerWrapper, firstLine, secondLine, Integer.valueOf(playerWrapper.getMCEntity().getEntityData().getInteger("RouteNotification")));
    }

    public static void sendNotification(PlayerWrapper playerWrapper, String firstLine, String secondLine, int id) {
        if(id < 0 || id > 7)
            id = 0;
        Server.sendData((EntityPlayerMP)playerWrapper.getMCEntity(), EnumPacketClient.MESSAGE, firstLine, secondLine, id);
    }

    //Debug?
    public static void test() {
        System.out.println("Test works");
    }

    public static void unlockBugCatcher(EntityPlayerMP player){
        if(!PermissionUtils.checkPermission(player, "titles.bug_catcher")) {
            sendNotification(new PlayerWrapper(player), "Titles", "Unlocked title: Bug Catcher");
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

    public static void resetPlayerData(EntityPlayerMP player) {
        try {
            MinecraftServer server = player.server;
            //CNPCs - Works
            PlayerData cnpcData = PlayerDataController.instance.getDataFromUsername(server, player.getName());
            cnpcData.setNBT(new NBTTagCompound());
            cnpcData.save(true);
            //Minecraft - Works but player spawns in void (pos 0,0,0) not in the lobby dimension
            File tmpDat = new File(DimensionManager.getCurrentSaveRootDirectory(), "playerdata/" + player.getUniqueID() + ".dat.tmp");
            File dat = new File(DimensionManager.getCurrentSaveRootDirectory(), "playerdata/" + player.getUniqueID() + ".dat");
            //Pixelmon - Works. (Weird double starter choice menu? Shouldn't matter.)
            PlayerPartyStorage party = Pixelmon.storageManager.getParty(player);
            PCStorage pc = Pixelmon.storageManager.getPCForPlayer(player);
            Field loadedParties = Pixelmon.storageManager.getClass().getDeclaredField("parties");
            Field loadedPCs = Pixelmon.storageManager.getClass().getDeclaredField("pcs");
            loadedParties.setAccessible(true);
            loadedPCs.setAccessible(true);
            ((Map<UUID, PlayerPartyStorage>)loadedParties.get(Pixelmon.storageManager)).remove(player.getUniqueID());
            ((Map<UUID, PCStorage>)loadedPCs.get(Pixelmon.storageManager)).remove(player.getUniqueID());
            //Post-Kick
            player.connection.disconnect(new TextComponentString("Resetting your data! Bare with us..."));
            server.addScheduledTask( () -> {
                player.readEntityFromNBT(new NBTTagCompound());
                player.readFromNBT(new NBTTagCompound());
                if(tmpDat.exists())
                    tmpDat.delete();
                dat.delete();
                party.getFile().delete();
                pc.getFile().delete();
            });
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
