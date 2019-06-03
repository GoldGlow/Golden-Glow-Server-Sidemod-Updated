package com.goldenglow.common.battles.raids;

import com.goldenglow.GoldenGlow;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.log.AttackAction;
import com.pixelmonmod.pixelmon.battles.controller.log.BattleActionBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;

public class RaidHandler {

    EntityPixelmon raidBoss;
    HashMap<BattleControllerBase, Integer> battleMap = new HashMap<>();

    public void startOrJoinRaid(EntityPlayerMP player) {
        EntityPixelmon boss;
        if(raidBoss!=null)
            boss = this.raidBoss;
        else {
            //ToDo: Replace Raid Poke generation
            boss = new PokemonSpec("Magikarp").create(player.world);
            this.raidBoss = boss;
            BlockPos pos = player.getPosition();
            raidBoss.setPosition((double)pos.getX(), (double)(pos.getY() + 1), (double)pos.getZ());
            raidBoss.canDespawn = false;
            raidBoss.setSpawnLocation(raidBoss.getDefaultSpawnLocation());
            player.world.spawnEntity(raidBoss);
        }
        EntityPixelmon pixelmon = Pixelmon.storageManager.getParty(player).getAndSendOutFirstAblePokemon(player);
        PlayerParticipant p = new PlayerParticipant(player, pixelmon);
        WildPixelmonParticipant w = new WildPixelmonParticipant(boss);
        BattleControllerBase bc = BattleRegistry.startBattle(new BattleParticipant[]{ p }, new BattleParticipant[]{ w }, new RaidBattleRules());
        battleMap.put(bc, 0);
    }

    public void update() {
        for(BattleControllerBase b : battleMap.keySet()) {
            if(b.battleTurn > battleMap.get(b)) {
                GoldenGlow.logger.info("Raid: New Turn");
                BattleActionBase[] actions = b.battleLog.getLog().get(b.battleTurn-1);
                if(actions[0] instanceof AttackAction) {
                    for(BattleControllerBase bc : battleMap.keySet()) {
                        if(bc!=b) {
                            bc.sendDamagePacket(bc.getParticipantForEntity(this.raidBoss).allPokemon[0], ((AttackAction) actions[0]).moveResults[0].damage);
                        }
                    }
                }
                battleMap.put(b, b.battleTurn);
            }
        }
    }

    public boolean containsBattle(BattleControllerBase bc) {
        return this.battleMap.containsKey(bc);
    }

    public void endBattle(BattleControllerBase bc) {
        this.battleMap.remove(bc);
    }
}
