package com.goldenglow.common.battles.boss;

import com.goldenglow.common.battles.boss.fights.BossBase;
import com.goldenglow.common.battles.boss.phase.Phase;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;

public class BossParticipant extends WildPixelmonParticipant {

    BossBase bossBase;
    Phase currentPhase;

    public BossParticipant() {
    }

    public BossParticipant(BossBase bossBase, EntityPlayerMP player) {
        this(bossBase, player, player.posX, player.posY, player.posZ);
    }

    public BossParticipant(BossBase bossBase, EntityPixelmon... pixelmon) {
        this.bossBase = bossBase;
        this.init(pixelmon);
    }

    public BossParticipant(BossBase bossBase, EntityPlayerMP player, double x, double y, double z) {
        super(bossBase.createPokemon().getOrSpawnPixelmon(player.world, x, y, z));
        this.bossBase = bossBase;
    }

    public void init(EntityPixelmon... pixelmon) {
        this.allPokemon = new PixelmonWrapper[pixelmon.length];

        for(int i = 0; i < pixelmon.length; ++i) {
            BossWrapper pw = new BossWrapper(this, pixelmon[i], i);
            this.allPokemon[i] = pw;
            this.controlledPokemon.add(pw);
        }
    }

    public void onTurnEnd(BattleControllerBase bc) {
        bossBase.phaseCheck(bc, this);
    }

    public BossBase getBossBase() {
        return this.bossBase;
    }

    public Phase getCurrentPhase() {
        return this.currentPhase;
    }

    public void setCurrentPhase(Phase phase) {
        this.currentPhase = phase;
    }

}
