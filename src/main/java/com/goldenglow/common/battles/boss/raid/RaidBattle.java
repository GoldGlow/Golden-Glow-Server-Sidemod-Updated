package com.goldenglow.common.battles.boss.raid;

import com.goldenglow.common.battles.boss.BossParticipant;
import com.goldenglow.common.battles.boss.fights.BossBase;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.attacks.DamageTypeEnum;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.EntityWormhole;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.battle.EnumBattleEndCause;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;

public class RaidBattle {

    BossBase bossBase;
    EntityPixelmon bossEntity;

    EntityRaidWormhole wormhole;

    List<EntityPlayerMP> players = new ArrayList<>();
    List<BattleControllerBase> playerBattles = new ArrayList<>();

    public RaidBattle(BossBase bossBase, World world, double x, double y, double z, int timer) {
        this.bossBase = bossBase;
        wormhole = new EntityRaidWormhole(world, x, y, z, timer);
    }

    public void startBattles() {
        this.bossEntity = bossBase.createPokemon().getOrSpawnPixelmon(FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(0), wormhole.posX, wormhole.posY, wormhole.posZ);
        for(EntityPlayerMP p : players) {
            BossParticipant bossParticipant = new BossParticipant(bossBase, bossEntity);
            playerBattles.add(BattleRegistry.startBattle(new BattleParticipant[]{new PlayerParticipant(p, Pixelmon.storageManager.getParty(p).getAndSendOutFirstAblePokemon(p))}, new BattleParticipant[]{bossParticipant}, new RaidBattleRules(bossParticipant, this)));
        }
    }

    public boolean addPlayer(EntityPlayerMP player) {
        return players.add(player);
    }

    public boolean removePlayer(EntityPlayerMP player) {
        return players.remove(player);
    }

    public List<EntityPlayerMP> getPlayers() {
        return players;
    }

    public void updateHP(PixelmonWrapper user, PixelmonWrapper target, int damage, DamageTypeEnum damageType) {
        for(BattleControllerBase bc : playerBattles) {
            if(bc.isInBattle(user))
                continue;
            bc.sendDamagePacket(target, damage);
        }
    }

}
