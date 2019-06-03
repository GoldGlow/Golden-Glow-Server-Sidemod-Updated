package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.battles.CustomNPCBattle;
import com.goldenglow.common.battles.DoubleNPCBattle;
import com.goldenglow.common.battles.raids.RaidBattleRules;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.LevelUpEvent;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerDeath;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.entity.IPlayer;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.InventoryArchetypes;

public class GGEventHandler {

    @SubscribeEvent
    public void playerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        if(!event.player.getEntityData().hasKey("RouteNotification"))
            event.player.getEntityData().setInteger("RouteNotification", 0);
    }

    @SubscribeEvent
    public void itemDroppedEvent(ItemTossEvent event){
        for(String id: GoldenGlow.phoneItemListHandler.itemIDs) {
            int item=Item.getIdFromItem(Item.getByNameOrId(id));
            if (Item.getIdFromItem(event.getEntityItem().getItem().getItem()) ==item){
                ItemStack itemStack = event.getEntityItem().getItem();
                event.setCanceled(true);
                event.getPlayer().inventory.addItemStackToInventory(itemStack);
                event.getPlayer().sendMessage(new TextComponentString(Reference.red+"Cannot drop this item!"));
            }
        }
    }

    @Listener
    public void itemStoredEvent(ClickInventoryEvent event){
        if(event.getTargetInventory().getArchetype()!= InventoryArchetypes.PLAYER){
            for(String id: GoldenGlow.phoneItemListHandler.itemIDs) {
                if(event.getTransactions().get(0).getFinal().getType().getName().equals(id)){
                    event.setCancelled(true);
                }
            }
        }
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
                    SongManager.stopSong(((PlayerParticipant) participant).player);
                    SongManager.playSong(((PlayerParticipant) participant).player, GoldenGlow.songManager.trainerBattleSong);
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
                            GGLogger.info("Playing wild theme!");
                            SongManager.stopSong(((PlayerParticipant) participant).player);
                            SongManager.playSong(((PlayerParticipant) participant).player, GoldenGlow.songManager.wildBattleSong);
                        }
                    }
                    if(wildBattle)
                        break;
                }
            }
            if(!wildBattle){
                String participantTheme=CustomBattleHandler.getCustomTheme(participants);
                String opponentTheme=CustomBattleHandler.getCustomTheme(opponents);
                if(!opponentTheme.equals("")){
                    for(BattleParticipant participant:participants){
                        if (participant instanceof PlayerParticipant) {
                            SongManager.stopSong(((PlayerParticipant) participant).player);
                            SongManager.playSong(((PlayerParticipant) participant).player, opponentTheme);
                        }
                    }
                    if(participantTheme.equals("")){
                        for(BattleParticipant participant:opponents){
                            if (participant instanceof PlayerParticipant) {
                                SongManager.stopSong(((PlayerParticipant) participant).player);
                                SongManager.playSong(((PlayerParticipant) participant).player, opponentTheme);
                            }
                        }
                    }
                }
                if(!participantTheme.equals("")) {
                    for (BattleParticipant participant : opponents) {
                        if (participant instanceof PlayerParticipant) {
                            SongManager.stopSong(((PlayerParticipant) participant).player);
                            SongManager.playSong(((PlayerParticipant) participant).player, opponentTheme);
                        }
                    }
                    if (opponentTheme.equals("")) {
                        for (BattleParticipant participant : participants) {
                            if (participant instanceof PlayerParticipant) {
                                SongManager.stopSong(((PlayerParticipant) participant).player);
                                SongManager.playSong(((PlayerParticipant) participant).player, opponentTheme);
                            }
                        }
                    }
                }
                if(participantTheme.equals("")&&opponentTheme.equals("")){
                    for (BattleParticipant participant : participants) {
                        if (participant instanceof PlayerParticipant) {
                            SongManager.stopSong(((PlayerParticipant) participant).player);
                            SongManager.playSong(((PlayerParticipant) participant).player, GoldenGlow.songManager.trainerBattleSong);
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onLevelUp(LevelUpEvent event){
        SongManager.playSound(event.player, "neutral", GoldenGlow.songManager.levelUpSound);
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event)
    {
        if(event.bc.rules instanceof RaidBattleRules) {
            GoldenGlow.raidHandler.endBattle(event.bc);
        }
        else if(event.bc.rules instanceof CustomNPCBattle) {
            EntityPlayerMP mcPlayer = event.getPlayers().get(0);
            BattleResults results = event.results.get(event.bc.participants.get(0));
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            CustomNPCBattle battle = (CustomNPCBattle) event.bc.rules;
            BattleRegistry.deRegisterBattle(event.bc);
            SongManager.stopSong(mcPlayer);
            if (results == BattleResults.VICTORY) {
                SongManager.playSong(mcPlayer, GoldenGlow.songManager.victorySong);
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            if (results == BattleResults.DEFEAT) {
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getLoseDialog());
                ((IPlayer) mcPlayer).removeDialog(battle.getInitDiag().getId());
                SongManager.playRouteSong(mcPlayer);
            }
        }
        else if(event.bc.rules instanceof DoubleNPCBattle){
            DoubleNPCBattle rules=(DoubleNPCBattle)event.bc.rules;
            rules.getFirstNpc().getEntityData().setBoolean("inBattle", false);
            rules.getSecondNpc().getEntityData().setBoolean("inBattle", false);
            rules.getSecondPokemon().unloadEntity();
            rules.getFirstPokemon().unloadEntity();
        }
        else{
            for(EntityPlayerMP player:event.getPlayers()){
                SongManager.stopSong(player);
                SongManager.playRouteSong(player);
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
}
