package com.goldenglow.common.util.scripting;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.PermissionUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;

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

}
