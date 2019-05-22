package com.goldenglow.common.battles.raids;

import com.goldenglow.GoldenGlow;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.WildPixelmonParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PlayerPartyStorage;
import net.minecraft.entity.player.EntityPlayerMP;

import static com.pixelmonmod.pixelmon.battles.BattleRegistry.registerBattle;

public class RaidHandler {

    public EntityPixelmon raidBoss;

    public RaidHandler() {
    }

    public BattleControllerBase createRaidBattle(EntityPlayerMP playerMP) {
            GoldenGlow.logger.info("Creating Raid Entity");
            raidBoss = new PokemonSpec("Magikarp").create(playerMP.world);
            raidBoss.setPosition(playerMP.posX, playerMP.posY, playerMP.posZ);
            raidBoss.world.spawnEntity(raidBoss);
        PlayerPartyStorage storage = Pixelmon.storageManager.getParty(playerMP);
        EntityPixelmon startingPixelmon = storage.findOne(p -> (!p.isEgg() && p.getHealth() > 0)).getOrSpawnPixelmon(playerMP);
        PlayerParticipant p = new PlayerParticipant(playerMP, new EntityPixelmon[]{startingPixelmon});
        WildPixelmonParticipant w = new WildPixelmonParticipant(raidBoss);
        BattleControllerBase battle = new RaidBattleController(p, w, new BattleRules());
        BattleStartedEvent battleStartedEvent = new BattleStartedEvent(battle, new BattleParticipant[]{p}, new BattleParticipant[]{w});
        Pixelmon.EVENT_BUS.post(battleStartedEvent);
        if (battleStartedEvent.isCanceled()) return null;
        registerBattle(battle);
        return battle;
    }
}
