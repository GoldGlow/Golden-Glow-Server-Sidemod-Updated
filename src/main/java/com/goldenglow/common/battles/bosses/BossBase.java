package com.goldenglow.common.battles.bosses;

import com.goldenglow.common.battles.bosses.phase.Phase;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;

import java.util.ArrayList;
import java.util.List;

public class BossBase {

    Pokemon pokemon;
    List<Phase> phases = new ArrayList<>();

    public BossBase(Pokemon pokemon, List<Phase> phases) {
        this.pokemon = pokemon;
        this.phases = phases;
    }

}
