package com.goldenglow.common.battles.bosses;

import com.goldenglow.common.battles.bosses.fights.BossBase;
import com.goldenglow.common.battles.bosses.phase.Phase;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;

public class BossParticipant extends WildPixelmonParticipant {

    BossBase bossBase;
    Phase currentPhase;

    public BossParticipant(BossBase bossBase) {
        this.bossBase = bossBase;
    }

}
