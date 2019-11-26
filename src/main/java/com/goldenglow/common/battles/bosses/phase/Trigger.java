package com.goldenglow.common.battles.bosses.phase;

import com.goldenglow.common.battles.bosses.BossParticipant;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackAction;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackResult;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleActionBase;
import com.pixelmonmod.pixelmon.battles.controller.log.MoveResults;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.status.StatusType;
import com.pixelmonmod.pixelmon.enums.EnumType;

public class Trigger {

    float hpPercentage = -1F;
    StatusType status = null;
    EnumType hitByType = null;

    public boolean conditionsMet(BossParticipant participant, PixelmonWrapper activePokemon) {
        return (hpPercentage==-1F || activePokemon.getHealthPercent()<=hpPercentage) && (status==null || activePokemon.hasStatus(status)) && (hitByType==null || checkHitByType(participant, activePokemon));
    }

    boolean checkHitByType(BossParticipant participant, PixelmonWrapper activePokemon) {
        BattleActionBase[] lastTurn = participant.bc.battleLog.getLog().get(participant.bc.battleTurn-1);
        for(BattleActionBase b : lastTurn) {
            if(b instanceof AttackAction && b.pokemon!=activePokemon && b.pokemon.lastAttack.getType()==hitByType) {
                AttackAction a = (AttackAction)b;
                for(MoveResults result : a.moveResults) {
                    if(result.target==activePokemon && !missed(result.result))
                        return true;
                }
            }
        }
        return false;
    }

    boolean missed(AttackResult result) {
        return result == AttackResult.missed || result == AttackResult.failed || result == AttackResult.unable;
    }

}
