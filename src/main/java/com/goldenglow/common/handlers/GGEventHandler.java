package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.battles.CustomNPCBattle;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerDeath;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.npcs.NoppesUtilServer;
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

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event)
    {
        if(event.bc instanceof CustomNPCBattle && CustomBattleHandler.battles.contains(event.bc))
        {
            EntityPlayerMP mcPlayer= event.getPlayers().get(0);
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            CustomNPCBattle battle = (CustomNPCBattle)event.bc;
            BattleRegistry.deRegisterBattle(battle);
            CustomBattleHandler.battles.remove(battle);
            if(event.results.get(mcPlayer)== BattleResults.VICTORY) {
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            if(event.results.get(mcPlayer)==BattleResults.DEFEAT){
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getLoseDialog());
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
