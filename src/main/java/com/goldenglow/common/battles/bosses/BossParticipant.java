package com.goldenglow.common.battles.bosses;

import com.goldenglow.common.battles.bosses.fights.BossBase;
import com.goldenglow.common.battles.bosses.phase.Phase;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;

public class BossParticipant extends WildPixelmonParticipant {

    BossBase bossBase;
    Phase currentPhase;

    public BossParticipant(BossBase bossBase, EntityPlayerMP player) {
        this(bossBase, player, player.posX, player.posY, player.posZ);
    }

    public BossParticipant(BossBase bossBase, EntityPlayerMP player, double x, double y, double z) {
        super(bossBase.createPokemon().getOrSpawnPixelmon(player.world, x, y, z));
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
