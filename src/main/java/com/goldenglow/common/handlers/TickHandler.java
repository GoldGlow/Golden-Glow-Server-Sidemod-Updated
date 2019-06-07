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
    static int wTick = 0;

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if(!battleNPCs.isEmpty()) {
            for(NPCWrapper npc : battleNPCs.keySet()) {
                if(npc!=null) {
                    raytraceNPCBattle(npc, battleNPCs.get(npc));
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWorldTick(TickEvent.WorldTickEvent event) {
        if(event.phase == TickEvent.Phase.END) {
            GoldenGlow.raidHandler.update();
        }
        if(wTick==20) {
            wTick = 0;
            for(EntityPlayerMP player : GoldenGlow.routeManager.visualPlayers) {
                Route r = GoldenGlow.routeManager.getRoute(player);
                if(r!=null) {
                    for (BlockVector2D b : r.region.getPoints()) {
                        for(int i = r.region.getMinimumY(); i < r.region.getMaximumY(); i++) {
                            ((WorldServer) event.world).spawnParticle(player, EnumParticleTypes.FLAME, true, b.getX() + 0.5, i+0.5, b.getZ() + 0.5, 1, (double) 0, (double) 0, (double) 0, (double) 0);
                        }
                    }
                }
            }
        }
        wTick++;
    }

    static void raytraceNPCBattle(NPCWrapper npc, int initDialogID) {
        IEntity[] losEntities = npc.rayTraceEntities(5, true, true);
        if(losEntities.length > 0) {
            for(IEntity e : losEntities) {
                if(e instanceof PlayerWrapper) {
                    PlayerWrapper p = (PlayerWrapper)e;
                    if(!p.hasReadDialog(initDialogID)) {
                        npcBattleDialog((EntityPlayerMP)p.getMCEntity(), (EntityNPCInterface)npc.getMCEntity(), initDialogID);
                    }
                }
            }
        }
    }

    static void npcBattleDialog(EntityPlayerMP player, EntityNPCInterface npc, int initDialogID) {
        SongManager.setCurrentSong(player, GoldenGlow.songManager.encounterSong);
        NoppesUtilServer.openDialog(player, npc, (Dialog) DialogController.instance.get(initDialogID));
    }

}
