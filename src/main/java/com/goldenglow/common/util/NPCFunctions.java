package com.goldenglow.common.util;

import com.goldenglow.common.battles.CustomBattleHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.CustomNpcs;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCFunctions {

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
            if(npc.getRotationYawHead()==0||npc.getRotationYawHead()==180){
                if(Math.abs(player.getPosition().getX()-npc.getPosition().getX())<1&&Math.abs(player.getPosition().getY()-npc.getPosition().getY())<3){
                    if(npc.getRotationYawHead()==0&&player.getPosition().getZ()>=npc.getPosition().getZ()){
                        if(!playerWrapper.hasReadDialog(initDialogID)){
                            NoppesUtilServer.openDialog(player, npc, (Dialog)DialogController.instance.get(initDialogID));
                        }
                    }
                    if(npc.getRotationYawHead()==180&&player.getPosition().getZ()<=npc.getPosition().getZ()){
                        if(!playerWrapper.hasReadDialog(initDialogID)){
                            NoppesUtilServer.openDialog(player, npc, (Dialog)DialogController.instance.get(initDialogID));
                        }
                    }
                }
            }
            else if(npc.getRotationYawHead()==90||npc.getRotationYawHead()==270){
                if(Math.abs(player.getPosition().getZ()-npc.getPosition().getZ())<1&&Math.abs(player.getPosition().getY()-npc.getPosition().getY())<3){
                    if(npc.getRotationYawHead()==90&&player.getPosition().getX()<=npc.getPosition().getX()){
                        if(!playerWrapper.hasReadDialog(initDialogID)){
                            NoppesUtilServer.openDialog(player, npc, (Dialog)DialogController.instance.get(initDialogID));
                        }
                    }
                    if(npc.getRotationYawHead()==270&&player.getPosition().getX()>=npc.getPosition().getX()){
                        if(!playerWrapper.hasReadDialog(initDialogID)){
                            NoppesUtilServer.openDialog(player, npc, (Dialog)DialogController.instance.get(initDialogID));
                        }
                    }
                }
            }
    }

    public static void setCamera(EntityPlayerMP player, int posX, int posY, int posZ, int targetX, int targetY, int targetZ) {
        //Pixelmon.network.sendTo(new ClientCameraPacket(posX, posY, posZ, targetX, targetY, targetZ), player);
    }

    public static void releaseCamera(EntityPlayer player)
    {
        //BetterStorage.networkChannel.sendTo(new PacketGGSidemod(EnumGGPacketType.RESET_CAMERA), player);
    }
}
