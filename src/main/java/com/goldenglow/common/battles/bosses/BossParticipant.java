package com.goldenglow.common.battles.bosses;

import com.goldenglow.common.battles.bosses.fights.BossBase;
import com.goldenglow.common.battles.bosses.phase.Phase;
import com.pixelmonmod.pixelmon.api.events.battles.TurnEndEvent;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;


public class BossParticipant extends WildPixelmonParticipant {

    BossBase bossBase;
    Phase currentPhase;

    public BossParticipant(BossBase bossBase, EntityPlayerMP player) {
        super(new EntityPixelmon[] {bossBase.getPokemon().getOrSpawnPixelmon(player.world, player.posX, player.posY, player.posZ)});
        this.bossBase = bossBase;
    }

    public void onTurnEnd(BattleControllerBase bc) {
        bossBase.phaseCheck(bc, this);
    }

    public Phase getCurrentPhase() {
        return this.currentPhase;
    }

    public void setCurrentPhase(Phase phase) {
        this.currentPhase = phase;
    }

}
