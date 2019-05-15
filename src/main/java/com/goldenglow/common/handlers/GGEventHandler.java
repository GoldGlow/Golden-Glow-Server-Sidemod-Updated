package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.battles.CustomNPCBattle;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.NPCFunctions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.EvolveEvent;
import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerDeath;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class GGEventHandler {

    /*@SubscribeEvent
    public void playerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
        if(GoldenGlow.instance.followerHandler.followMap.containsKey(event.player)) {
            GoldenGlow.instance.followerHandler.followMap.get(event.player).despawn();
            GoldenGlow.instance.followerHandler.followMap.remove(event.player);
        }
    }*/

    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        //GoldenGlow.instance.gymManager.checkPlayer(event.player);
    }

    /*@SubscribeEvent
    public void onEvolution(EvolveEvent event){
        if(event instanceof EvolveEvent.PostEvolve){
            NPCFunctions.stopSound(event.player, "music", GoldenGlow.songManager.evolutionSong);
            NPCFunctions.playSound(event.player, "music", GoldenGlow.routeManager.getRoute(event.player).song);
        }
        else {
            NPCFunctions.stopSound(event.player, "music", GoldenGlow.routeManager.getRoute(event.player).song);
            NPCFunctions.playSound(event.player, "music", GoldenGlow.songManager.evolutionSong);
            if (event.isCanceled()) {
                NPCFunctions.stopSound(event.player, "music", GoldenGlow.songManager.evolutionSong);
                NPCFunctions.playSound(event.player, "music", GoldenGlow.routeManager.getRoute(event.player).song);
            }
        }
    }*/

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event){
        BattleParticipant[] participants=event.participant1;
        BattleParticipant[] opponents=event.participant2;
        if(event.bc.rules instanceof CustomNPCBattle) {
            for (BattleParticipant participant : participants) {
                if (participant instanceof PlayerParticipant) {
                    NPCFunctions.stopSound(((PlayerParticipant) participant).player, "music", GoldenGlow.songManager.encounterSong);
                    NPCFunctions.playSound(((PlayerParticipant) participant).player, "music", GoldenGlow.songManager.trainerBattleSong);
                }
            }
        }
        else{
            boolean wildBattle=false;
            for(BattleParticipant opponent: opponents){
                if(opponent instanceof WildPixelmonParticipant){
                    wildBattle=true;
                    for (BattleParticipant participant : participants) {
                        if (participant instanceof PlayerParticipant) {
                            NPCFunctions.stopSound(((PlayerParticipant) participant).player, "music", GoldenGlow.routeManager.getRoute(((PlayerParticipant) participant).player).song);
                            NPCFunctions.playSound(((PlayerParticipant) participant).player, "music", GoldenGlow.songManager.wildBattleSong);

                        }
                    }
                }
            }
            if(!wildBattle){
                String participantTheme=CustomBattleHandler.getCustomTheme(participants);
                String opponentTheme=CustomBattleHandler.getCustomTheme(opponents);
                if(!opponentTheme.equals("")){
                    for(BattleParticipant participant:participants){
                        if (participant instanceof PlayerParticipant) {
                            NPCFunctions.stopSound(((PlayerParticipant) participant).player, "music", GoldenGlow.routeManager.getRoute(((PlayerParticipant) participant).player).song);
                            NPCFunctions.playSound(((PlayerParticipant) participant).player, "music", opponentTheme);
                        }
                    }
                    if(participantTheme.equals("")){
                        for(BattleParticipant participant:opponents){
                            if (participant instanceof PlayerParticipant) {
                                NPCFunctions.stopSound(((PlayerParticipant) participant).player, "music", GoldenGlow.routeManager.getRoute(((PlayerParticipant) participant).player).song);
                                NPCFunctions.playSound(((PlayerParticipant) participant).player, "music", opponentTheme);
                            }
                        }
                    }
                }
                if(!participantTheme.equals("")) {
                    for (BattleParticipant participant : opponents) {
                        if (participant instanceof PlayerParticipant) {
                            NPCFunctions.stopSound(((PlayerParticipant) participant).player, "music", GoldenGlow.routeManager.getRoute(((PlayerParticipant) participant).player).song);
                            NPCFunctions.playSound(((PlayerParticipant) participant).player, "music", opponentTheme);
                        }
                    }
                    if (opponentTheme.equals("")) {
                        for (BattleParticipant participant : participants) {
                            if (participant instanceof PlayerParticipant) {
                                NPCFunctions.stopSound(((PlayerParticipant) participant).player, "music", GoldenGlow.routeManager.getRoute(((PlayerParticipant) participant).player).song);
                                NPCFunctions.playSound(((PlayerParticipant) participant).player, "music", opponentTheme);
                            }
                        }
                    }
                }
                if(participantTheme.equals("")&&opponentTheme.equals("")){
                    for (BattleParticipant participant : participants) {
                        if (participant instanceof PlayerParticipant) {
                            NPCFunctions.stopSound(((PlayerParticipant) participant).player, "music", GoldenGlow.routeManager.getRoute(((PlayerParticipant) participant).player).song);
                            NPCFunctions.playSound(((PlayerParticipant) participant).player, "music", GoldenGlow.songManager.trainerBattleSong);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLevelUp(LevelUpEvent event){
        NPCFunctions.playSound(event.player, "sound", GoldenGlow.songManager.levelUpSound);
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event)
    {
        if(event.bc.rules instanceof CustomNPCBattle) {
            EntityPlayerMP mcPlayer = event.getPlayers().get(0);
            BattleResults results = event.results.get(event.bc.participants.get(0));
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            CustomNPCBattle battle = (CustomNPCBattle) event.bc.rules;
            BattleRegistry.deRegisterBattle(event.bc);
            NPCFunctions.stopSound(mcPlayer, "music", GoldenGlow.songManager.trainerBattleSong);
            if (results == BattleResults.VICTORY) {
                NPCFunctions.playSound(mcPlayer, "music", GoldenGlow.songManager.victorySong);
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            if (results == BattleResults.DEFEAT) {
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getLoseDialog());
                ((IPlayer) mcPlayer).removeDialog(battle.getInitDiag().getId());
            }
        }
        else{
            for(EntityPlayerMP player:event.getPlayers()){
                NPCFunctions.stopSound(player, "music", GoldenGlow.songManager.wildBattleSong);
                NPCFunctions.stopSound(player, "music", GoldenGlow.songManager.trainerBattleSong);
                NPCFunctions.playSound(player, "music", GoldenGlow.routeManager.getRoute(player).song);
            }
        }
        /*else if(event.battleController instanceof FactoryBattle && CustomBattleHandler.factoryBattles.contains(event.battleController)){
            EntityPlayerMP mcPlayer= event.player;
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            FactoryBattle battle = (FactoryBattle)event.battleController;
            BattleRegistry.deRegisterBattle(battle);
            CustomBattleHandler.factoryBattles.remove(battle);
            PlayerWrapper player = new PlayerWrapper(mcPlayer);
            if(event.result==BattleResults.VICTORY){
                player.addFactionPoints(11, 1);
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            else if(event.result==BattleResults.DEFEAT){
                player.addFactionPoints(11, -player.getFactionPoints(11));
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getLoseDialog());
            }
        }*/
    }

    @SubscribeEvent
    public void playerUseItem(PlayerInteractEvent event) {
        /*if(event.entityPlayer.getCurrentEquippedItem() != null) {
            if (event.action.equals(PlayerInteractEvent.Action.LEFT_CLICK_BLOCK) && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe) {
                BlockPos pos1 = event.pos;
                Area area = new Area();
                if (GoldenGlow.instance.tempHandler.playerBlocks.containsKey(event.entityPlayer)) {
                    area = GoldenGlow.instance.tempHandler.playerBlocks.get(event.entityPlayer);
                }
                area.pos1 = pos1;
                GoldenGlow.instance.tempHandler.playerBlocks.put(event.entityPlayer, area);
            }
            if (event.action.equals(PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemAxe) {
                BlockPos pos2 = event.pos;
                Area area = new Area();
                if (GoldenGlow.instance.tempHandler.playerBlocks.containsKey(event.entityPlayer)) {
                    area = GoldenGlow.instance.tempHandler.playerBlocks.get(event.entityPlayer);
                }
                area.pos2 = pos2;
                GoldenGlow.instance.tempHandler.playerBlocks.put(event.entityPlayer, area);
            }
        }*/
    }
}
