package com.goldenglow.common.battles.boss;

import com.pixelmonmod.pixelmon.battles.rules.BattleRules;

public class BossBattleRules extends BattleRules {

    public final BossParticipant bossParticipant;

    public BossBattleRules(BossParticipant participant) {
        this.bossParticipant = participant;
    }

}
