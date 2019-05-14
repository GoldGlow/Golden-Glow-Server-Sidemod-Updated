package com.goldenglow.common.util;

import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.handlers.TickHandler;
import com.goldenglow.common.inventory.InstancedContainer;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.network.play.server.SPacketOpenWindow;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.PixelmonHelper;
import noppes.npcs.controllers.data.Dialog;
import net.minecraft.inventory.InventoryBasic;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.entity.EntityCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCFunctions {

	public static void playSound(EntityPlayerMP player, String source, String path){
        player.connection.sendPacket(new SPacketCustomSound(path, SoundCategory.getByName(source), player.getPosition().getX(), player.getPosition().getY(), player.getPosition().getZ(), 1, 1));
    }

    public static void stopSound(EntityPlayerMP player, String source, String path){
        PacketBuffer packetBuffer=new PacketBuffer(Unpooled.buffer());
        packetBuffer.writeString(source);
        packetBuffer.writeString(path);
        player.connection.sendPacket(new SPacketCustomPayload("MC|StopSound", packetBuffer));
    }

    public static void openStarterGui(EntityPlayerMP player) {
        //Pixelmon.instance.network.sendTo(new SelectPokemonListPacket(StarterList.getStarterList()), player);
    }

    public static void createCustomBattle(PlayerWrapper playerWrapper, String teamName, int initDialogID, int winDialogID, int loseDialogID, NPCWrapper npcWrapper) {
        EntityNPCInterface npc=(EntityNPCInterface) npcWrapper.getMCEntity();
        EntityPlayerMP player=(EntityPlayerMP)playerWrapper.getMCEntity();
        CustomBattleHandler.createCustomBattle(player, teamName, initDialogID, winDialogID, loseDialogID, npc);
    }

    public static void standardNPCBattle(PlayerWrapper playerWrapper, NPCWrapper npcWrapper, int initDialogID){
        EntityNPCInterface npc=(EntityNPCInterface) npcWrapper.getMCEntity();
        EntityPlayerMP player=(EntityPlayerMP)playerWrapper.getMCEntity();
        EntityPixelmon pixelmon= Pixelmon.storageManager.getParty(player).getAndSendOutFirstAblePokemon(player);
        if(pixelmon!=null) {
            if (npc.getRotationYawHead() == 0 || npc.getRotationYawHead() == 180) {
                if (Math.abs(player.getPosition().getX() - npc.getPosition().getX()) < 1 && Math.abs(player.getPosition().getY() - npc.getPosition().getY()) < 3) {
                    if (npc.getRotationYawHead() == 0 && player.getPosition().getZ() >= npc.getPosition().getZ()) {
                        if (!playerWrapper.hasReadDialog(initDialogID)) {
                            NPCFunctions.stopSound(player, "music", "customnpcs:songs.route3");
                            NPCFunctions.playSound(player, "music", "customnpcs:songs.rivaltest");
                            NoppesUtilServer.openDialog(player, npc, (Dialog) DialogController.instance.get(initDialogID));
                        }
                    }
                    if (npc.getRotationYawHead() == 180 && player.getPosition().getZ() <= npc.getPosition().getZ()) {
                        if (!playerWrapper.hasReadDialog(initDialogID)) {
                            NPCFunctions.stopSound(player, "music", "customnpcs:songs.route3");
                            NPCFunctions.playSound(player, "music", "customnpcs:songs.rivaltest");
                            NoppesUtilServer.openDialog(player, npc, (Dialog) DialogController.instance.get(initDialogID));
                        }
                    }
                }
            } else if (npc.getRotationYawHead() == 90 || npc.getRotationYawHead() == 270) {
                if (Math.abs(player.getPosition().getZ() - npc.getPosition().getZ()) < 1 && Math.abs(player.getPosition().getY() - npc.getPosition().getY()) < 3) {
                    if (npc.getRotationYawHead() == 90 && player.getPosition().getX() <= npc.getPosition().getX()) {
                        if (!playerWrapper.hasReadDialog(initDialogID)) {
                            NPCFunctions.stopSound(player, "music", "customnpcs:songs.route3");
                            NPCFunctions.playSound(player, "music", "customnpcs:songs.rivaltest");
                            NoppesUtilServer.openDialog(player, npc, (Dialog) DialogController.instance.get(initDialogID));
                        }
                    }
                    if (npc.getRotationYawHead() == 270 && player.getPosition().getX() >= npc.getPosition().getX()) {
                        if (!playerWrapper.hasReadDialog(initDialogID)) {
                            NPCFunctions.stopSound(player, "music", "customnpcs:songs.route3");
                            NPCFunctions.playSound(player, "music", "customnpcs:songs.rivaltest");
                            NoppesUtilServer.openDialog(player, npc, (Dialog) DialogController.instance.get(initDialogID));
                        }
                    }
                }
            }
        }
    }

    public static void registerLOSBattle(NPCWrapper npc, int initDialogID) {
        TickHandler.battleNPCs.put(npc, initDialogID);
    }

    public static void setCamera(EntityPlayerMP player, int posX, int posY, int posZ, int targetX, int targetY, int targetZ) {
        //Pixelmon.network.sendTo(new ClientCameraPacket(posX, posY, posZ, targetX, targetY, targetZ), player);
    }

    public static void releaseCamera(EntityPlayer player)
    {
        //BetterStorage.networkChannel.sendTo(new PacketGGSidemod(EnumGGPacketType.RESET_CAMERA), player);
    }

    public static void createInstancedInv(EntityPlayerMP playerMP, String[] items, String containerName, int questID) {
        HashMap<Integer, QuestData> data = PlayerData.get(playerMP).questData.activeQuests;
        if(data.containsKey(questID)) {
            QuestData qData = data.get(questID);
            if(!qData.isCompleted) {
                //Create Inventory with specified items
                InventoryBasic inv = new InventoryBasic(containerName, false, (9+(9 % items.length)));
                for (String tag : items) {
                    try {
                        ItemStack stack = new ItemStack(JsonToNBT.getTagFromJson(tag));
                        if(stack!=null) {
                            inv.addItem(stack);
                        }
                    } catch (NBTException e) {
                        e.printStackTrace();
                    }
                }
                //Show inventory to player
                playerMP.getNextWindowId();
                playerMP.connection.sendPacket(new SPacketOpenWindow(playerMP.currentWindowId, "minecraft:container", inv.getDisplayName(), inv.getSizeInventory()));
                playerMP.openContainer = new InstancedContainer(playerMP.inventory, inv, playerMP);
                playerMP.openContainer.windowId = playerMP.currentWindowId;
                playerMP.openContainer.addListener(playerMP);
                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(new net.minecraftforge.event.entity.player.PlayerContainerEvent.Open(playerMP, playerMP.openContainer));
            }
        }
    }

    public static void setPixelmon(EntityCustomNpc npc, String name) {
        npc.modelData.setEntityClass(EntityPixelmon.class);
        npc.modelData.extra.setString("Name", name);
        try {
            EntityPixelmon mon = EntityPixelmon.class.getConstructor(new Class[]{World.class}).newInstance(new Object[]{npc.world});
            mon.readEntityFromNBT(npc.modelData.extra);
        } catch (Exception e) {
            e.printStackTrace();
        }
        npc.display.setSkinTexture(((EntityPixelmon)npc.modelData.getEntity(npc)).getTexture().toString());
        System.out.println(PixelmonHelper.isPixelmon(npc.modelData.getEntity(npc)));
    }
}
