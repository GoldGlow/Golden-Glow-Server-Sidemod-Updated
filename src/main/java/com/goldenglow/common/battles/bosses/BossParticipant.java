package com.goldenglow.common.battles.bosses;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.battles.bosses.fights.BossBase;
import com.goldenglow.common.battles.bosses.phase.Phase;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;

import java.util.ArrayList;
import java.util.List;

public class BossParticipant extends WildPixelmonParticipant {

    BossBase bossBase;
    Phase currentPhase;

    public BossParticipant(BossBase bossBase) {
        this.bossBase = bossBase;
        List<Pokemon> p = new ArrayList<>();
        p.add(this.bossBase.getPokemon());
        this.loadParty(p);
        GoldenGlow.logger.info(this.allPokemon[0]==null);
    }

}
