package com.goldenglow.common.battles.bosses.phase;

import com.goldenglow.common.battles.bosses.BossParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumGrowth;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import com.pixelmonmod.pixelmon.enums.EnumType;
import com.pixelmonmod.pixelmon.items.heldItems.HeldItem;

import java.util.List;

public class Phase {

    Trigger[] triggers;

    String nickname;
    List<EnumType> type;
    AbilityBase ability;
    EnumNature nature;
    Moveset moveset;
    int form = -1;
    EnumGrowth growth;
    HeldItem heldItem;

    public Phase(Trigger[] triggers) {
        this.triggers = triggers;
    }

    public void onPhaseChange(BossParticipant bossParticipant, PixelmonWrapper activePokemon) {
        if(nickname!=null)
            activePokemon.pokemon.setNickname(nickname);
        if(type!=null)
            activePokemon.setTempType(this.type);
        if(ability!=null) {
            activePokemon.setTempAbility(ability);
            activePokemon.tempAbility.applySwitchInEffect(activePokemon);
        }
        if(nature!=null)
            activePokemon.pokemon.setNature(nature);
        if(moveset!=null)
            activePokemon.setTemporaryMoveset(this.moveset);
        if(form!=-1)
            activePokemon.setForm(form);
        if(growth!=null)
            activePokemon.pokemon.setGrowth(growth);
        if(heldItem!=null)
            activePokemon.setHeldItem(heldItem);
    }

    public boolean checkTriggers(BossParticipant participant, PixelmonWrapper activePokemon) {
        for(Trigger t : this.triggers) {
            if(t.conditionsMet(participant, activePokemon)) {
                this.onPhaseChange(participant, activePokemon);
                return true;
            }
        }
        return false;
    }

    public Phase setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }
    public Phase setType(List<EnumType> type) {
        this.type = type;
        return this;
    }
    public Phase setAbility(AbilityBase ability) {
        this.ability = ability;
        return this;
    }
    public Phase setNature(EnumNature nature) {
        this.nature = nature;
        return this;
    }
    public Phase setMoveset(Moveset moveset) {
        this.moveset = moveset;
        return this;
    }
    public Phase setForm(int form) {
        this.form = form;
        return this;
    }
    public Phase setGrowth(EnumGrowth growth) {
        this.growth = growth;
        return this;
    }
    public Phase setHeldItem(HeldItem heldItem) {
        this.heldItem = heldItem;
        return this;
    }

}
