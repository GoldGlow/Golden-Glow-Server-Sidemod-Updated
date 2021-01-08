package com.goldenglow.common.battles.boss.raid;

import com.goldenglow.common.battles.boss.BossBattleRules;
import com.goldenglow.common.battles.boss.BossParticipant;

public class RaidBattleRules extends BossBattleRules {

    RaidBattle raidBattle;

    public RaidBattleRules(BossParticipant participant, RaidBattle raidBattle) {
        super(participant);
        this.raidBattle = raidBattle;
    }

    public RaidBattle getRaidBattle() {
        return raidBattle;
    }

}
