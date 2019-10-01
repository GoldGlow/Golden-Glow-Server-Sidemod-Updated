package com.goldenglow.common.util.scripting;

import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.handlers.TickHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.entity.EntityNPCInterface;

public class BattleFunctions {
    public static void createNPCBattle(NPCWrapper firstNPC, String firstTeamName, NPCWrapper secondNPC, String secondTeamName){
        EntityNPCInterface firstNpc=(EntityNPCInterface) firstNPC.getMCEntity();
        EntityNPCInterface secondNpc=(EntityNPCInterface) secondNPC.getMCEntity();
        CustomBattleHandler.createCustomNPCBattle(firstNpc, firstTeamName, secondNpc, secondTeamName);
    }

    public static void createCustomBattle(PlayerWrapper playerWrapper, String teamName, int initDialogID, int winDialogID, int loseDialogID, NPCWrapper npcWrapper) {
        EntityNPCInterface npc=(EntityNPCInterface) npcWrapper.getMCEntity();
        EntityPlayerMP player=(EntityPlayerMP)playerWrapper.getMCEntity();
        if(playerWrapper.hasPermission("hard")){
            teamName+="-hard";
        }
        CustomBattleHandler.createCustomBattle(player, teamName, initDialogID, winDialogID, loseDialogID, npc);
    }

    public static void registerLOSBattle(NPCWrapper npc, int initDialogID) {
        TickHandler.battleNPCs.put(npc, initDialogID);
    }
}
