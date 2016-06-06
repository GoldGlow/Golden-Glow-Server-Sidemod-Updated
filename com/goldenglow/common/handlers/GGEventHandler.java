package com.goldenglow.common.handlers;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.CustomBattleHandler;
import com.goldenglow.common.battles.CustomNPCBattle;
import com.goldenglow.common.routes.Area;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.NPCFunctions;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.enums.BattleResults;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.comm.packetHandlers.PlayerDeath;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.BattleGuiClosed;
import com.pixelmonmod.pixelmon.comm.packetHandlers.battles.ExitBattle;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemAxe;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.event.DialogEvent;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.entity.EntityNPCInterface;

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
        GoldenGlow.instance.gymManager.checkPlayer(event.player);
    }

    @SubscribeEvent
    public void onPlayerBattleEnded(PlayerBattleEndedEvent event)
    {
        GGLogger.info("Battle Ended!");
        if(event.battleController instanceof CustomNPCBattle && CustomBattleHandler.battles.contains(event.battleController))
        {
            EntityPlayerMP mcPlayer= event.player;
            Pixelmon.instance.network.sendTo(new PlayerDeath(), mcPlayer);
            CustomNPCBattle battle = (CustomNPCBattle)event.battleController;
            BattleRegistry.deRegisterBattle(battle);
            CustomBattleHandler.battles.remove(battle);
            if(event.result== BattleResults.VICTORY) {
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getWinDialog());
            }
            if(event.result==BattleResults.DEFEAT){
                NoppesUtilServer.openDialog(mcPlayer, battle.getNpc(), battle.getLoseDialog());
            }
        }
    }

    @SubscribeEvent
    public void onDialogOption(DialogEvent.OptionEvent event){
        GGLogger.info("option selected");
        if(event.dialog.getId()==10000){
            NPCFunctions.loadFactoryDialog(10001+event.option.getSlot(), (PlayerWrapper)event.player, (EntityNPCInterface) event.npc.getMCEntity());
        }
        else if(event.dialog.getId()>=10001&&event.dialog.getId()<=10006){
            if(event.option.getSlot()==0){
                boolean chosen = NPCFunctions.isChosen(event.dialog.getId(), (PlayerWrapper)event.player);
                if(chosen)
                    NPCFunctions.loadFactoryDialog(event.dialog.getId()+12, (PlayerWrapper)event.player, (EntityNPCInterface)event.npc.getMCEntity());
                else
                    NPCFunctions.loadFactoryDialog(event.dialog.getId()+6, (PlayerWrapper)event.player, (EntityNPCInterface)event.npc.getMCEntity());
            }
            else
                NPCFunctions.loadFactoryDialog(10000, (PlayerWrapper)event.player, (EntityNPCInterface)event.npc.getMCEntity());
        }
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
