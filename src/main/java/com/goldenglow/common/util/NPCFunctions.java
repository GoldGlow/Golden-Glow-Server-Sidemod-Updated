package com.goldenglow.common.util;

import com.goldenglow.common.battles.CustomBattleHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.entity.EntityNPCInterface;

public class NPCFunctions {

    public static void openStarterGui(EntityPlayerMP player) {
        //Pixelmon.instance.network.sendTo(new SelectPokemonListPacket(StarterList.getStarterList()), player);
    }

    public static void createCustomBattle(EntityPlayerMP player, String teamName, int winDialogID, int loseDialogID, EntityNPCInterface npc) {
        CustomBattleHandler.createCustomBattle(player, teamName, winDialogID, loseDialogID, npc);
    }

    public static void standardNPCBattle(EntityPlayerMP player, String teamName, int winDialogID, int loseDialogID, EntityNPCInterface npc){
        createCustomBattle(player, teamName, winDialogID, loseDialogID, npc);
    }

    public static void setCamera(EntityPlayerMP player, int posX, int posY, int posZ, int targetX, int targetY, int targetZ) {
        //Pixelmon.network.sendTo(new ClientCameraPacket(posX, posY, posZ, targetX, targetY, targetZ), player);
    }

    public static void releaseCamera(EntityPlayer player)
    {
        //BetterStorage.networkChannel.sendTo(new PacketGGSidemod(EnumGGPacketType.RESET_CAMERA), player);
    }
}
