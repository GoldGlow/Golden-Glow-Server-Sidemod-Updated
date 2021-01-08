package com.goldenglow.common.battles.boss;

import com.goldenglow.common.events.BossReceivedDamageEvent;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

public class BossWrapper extends PixelmonWrapper {

    public BossWrapper(BattleParticipant participant, EntityPixelmon entity, int partyPosition) {
        super(participant, entity, partyPosition);
    }

    public BossWrapper(BattleParticipant participant, EntityPixelmon pixelmon, int partyPosition, BattleControllerBase bc) {
        super(participant, pixelmon, partyPosition, bc);
    }

    public BossWrapper(BattleParticipant participant, Pokemon pokemon, int partyPosition) {
        super(participant, pokemon, partyPosition);
    }

    @Override
    public float doBattleDamage(PixelmonWrapper source, float damage, DamageTypeEnum damageType) {
        Pixelmon.EVENT_BUS.post(new BossReceivedDamageEvent(source, this, damage, damageType));
        return super.doBattleDamage(source, damage, damageType);
    }
}
