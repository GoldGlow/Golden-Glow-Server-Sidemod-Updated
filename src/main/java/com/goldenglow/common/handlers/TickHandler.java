package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.Route;
import com.sk89q.worldedit.BlockVector2D;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.HashMap;
import java.util.Map;

public class TickHandler {

    public static Map<NPCWrapper, Integer> battleNPCs = new HashMap();

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.ServerTickEvent event) {
        if(!battleNPCs.isEmpty()) {
            for(NPCWrapper npc : battleNPCs.keySet()) {
                if(npc!=null) {
                    raytraceNPCBattle(npc, battleNPCs.get(npc));
                }
            }
        }
    }

    static void raytraceNPCBattle(NPCWrapper npc, int initDialogID) {
        IEntity[] losEntities = npc.rayTraceEntities(5, true, true);
        if(losEntities.length > 0) {
            for(IEntity e : losEntities) {
                if(e instanceof PlayerWrapper) {
                    PlayerWrapper p = (PlayerWrapper)e;
                    if(!p.hasReadDialog(initDialogID) && !(p.getGamemode()==1 || p.getGamemode()==3)) {
                        npcBattleDialog((EntityPlayerMP)p.getMCEntity(), (EntityNPCInterface)npc.getMCEntity(), initDialogID);
                    }
                }
            }
        }
    }

    public static void npcBattleDialog(EntityPlayerMP player, EntityNPCInterface npc, int initDialogID) {
        SongManager.setCurrentSong(player, GoldenGlow.songManager.encounterDefault);
        NoppesUtilServer.openDialog(player, npc, (Dialog) DialogController.instance.get(initDialogID));
    }

}
