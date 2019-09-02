package com.goldenglow.common.battles.raids;

import com.goldenglow.GoldenGlow;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.battles.ApplyBonusStatsEvent;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvents;
import com.pixelmonmod.pixelmon.api.events.battles.BattleEndEvent;
import com.pixelmonmod.pixelmon.api.events.battles.TurnEndEvent;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.enums.battle.BattleResults;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;
import org.spongepowered.api.text.title.Title;

import java.util.ArrayList;
import java.util.List;

public class RaidEventHandler {

    public static List<RaidController> activeRaids = new ArrayList<>();

    @SubscribeEvent
    public void onBattleStart(BattleStartedEvent event) {
        if(event.bc.rules instanceof RaidBattleRules) {
            RaidController raidController = ((RaidBattleRules)event.bc.rules).getRaidController();
            if(!activeRaids.contains(raidController))
                activeRaids.add(raidController);
        }
    }

    @SubscribeEvent
    public void onApplyBonusStats(ApplyBonusStatsEvent.Pre event) {
        if(event.getBattleController().rules instanceof RaidBattleRules) {
            GoldenGlow.logger.info("onApplyBonusStats - " + event.getPokemon());
            ((RaidBattleRules) event.getBattleController().rules).getRaidController().applyBonusStats(event.getPokemon());
        }
    }

    @SubscribeEvent
    public void onAttackDmgEvent(AttackEvents.DamageEvent event) {
        if(event.user.bc.rules instanceof RaidBattleRules) {
            GoldenGlow.logger.info("onAttackDmg (" + event.getAttack() + ") - " + event.user + " (" + event.damage + ")-> " + event.target);
            ((RaidBattleRules) event.user.bc.rules).getRaidController().onAttack(event.user.bc, event);
        }
    }

    @SubscribeEvent
    public void onTurnEnd(TurnEndEvent event) {
        if(event.bcb.rules instanceof RaidBattleRules) {
            GoldenGlow.logger.info("onTurnEnd");
            ((RaidBattleRules) event.bcb.rules).getRaidController().turnEnd(event.bcb);
        }
    }

    @SubscribeEvent
    public void onBattleEnd(BattleEndEvent event) {
        if(event.bc.rules instanceof RaidBattleRules) {
            ((RaidBattleRules) event.bc.rules).getRaidController().battleEnd(event.bc);
            for (BattleParticipant p : event.results.keySet()) {
                GoldenGlow.logger.info("onBattleEnd - "+p.getType()+": "+p.getDisplayName()+" - "+event.results.get(p));
                if(p instanceof PlayerParticipant && event.results.get(p).equals(BattleResults.VICTORY)) {
                    Title victoryTitle = Title.builder().title(Text.builder("Boss Defeated!").color(TextColors.GOLD).style(TextStyles.BOLD).build()).fadeOut(60).stay(100).build();
                    Sponge.getServer().getPlayer(((PlayerParticipant) p).player.getUniqueID()).get().sendTitle(victoryTitle);
                }
            }
        }
    }

}
