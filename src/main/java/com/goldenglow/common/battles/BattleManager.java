package com.goldenglow.common.battles;

import com.goldenglow.common.battles.boss.BossBattleRules;
import com.goldenglow.common.battles.boss.BossParticipant;
import com.goldenglow.common.battles.boss.fights.BossBase;
import com.goldenglow.common.battles.boss.raid.RaidBattle;
import com.goldenglow.common.battles.boss.raid.RaidBattleRules;
import com.goldenglow.common.events.BossReceivedDamageEvent;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.events.battles.AttackEvents;
import com.pixelmonmod.pixelmon.api.events.battles.TurnEndEvent;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BattleManager {

    private static HashMap<String, BossBase> bosses = new HashMap<>();
    private static HashMap<String, BossBase> raid_bosses = new HashMap<>();

    public static RaidBattle currentRaid;

    public static void init() {
        GGLogger.info("Loading Bosses...");
        /**Load and Register json Boss files**/
        File dir = new File(Reference.bossDir);
        if(!dir.exists())
            dir.mkdir();
        for(File f : dir.listFiles()) {
            if(f.getName().endsWith(".json")) {
                try {
                    bosses.put(f.getName().replace(".json",""), BossBase.loadFromFile(f));
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        dir = new File(dir, "raid");
        if(!dir.exists())
            dir.mkdir();
        for(File f : dir.listFiles()) {
            if(f.getName().endsWith(".json")) {
                try {
                    raid_bosses.put(f.getName().replace(".json",""), BossBase.loadFromFile(f));
                }
                catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        /**Register hard-coded bosses here**/
        //bosses.put("bird_bear", new BossBirdBear());
    }

    public static BattleControllerBase startBossBattle(EntityPlayerMP player, String bossName) {
        return startBossBattle(player, bossName, player.posX, player.posY, player.posZ);
    }

    public static BattleControllerBase startBossBattle(EntityPlayerMP player, String bossName, double x, double y, double z) {
        if(bosses.containsKey(bossName)) {
            BossParticipant bossParticipant = new BossParticipant(bosses.get(bossName), player, x, y, z);
            PlayerParticipant playerParticipant = new PlayerParticipant(player, Pixelmon.storageManager.getParty(player).getAndSendOutFirstAblePokemon(player));
            return BattleRegistry.startBattle(new BattleParticipant[]{playerParticipant}, new BattleParticipant[]{bossParticipant}, new BossBattleRules(bossParticipant));
        }
        return null;
    }

    public static RaidBattle createRaidBattle(String bossName, World world, double x, double y, double z, int timer) {
        if(raid_bosses.containsKey(bossName))
            currentRaid = new RaidBattle(raid_bosses.get(bossName), world, x, y, z, timer);
        return currentRaid;
    }

    public static List<BossBase> getBossList() {
        return new ArrayList<>(bosses.values());
    }

    public static String getBosses() {
        StringBuilder b = new StringBuilder();
        for(String s : bosses.keySet()) {
            b.append(s);
        }
        return b.toString();
    }

    @SubscribeEvent
    public static void onTurnEnd(TurnEndEvent event) {
        if(event.bcb.rules instanceof BossBattleRules) {
            ((BossBattleRules)event.bcb.rules).bossParticipant.onTurnEnd(event.bcb);
        }
    }

    @SubscribeEvent
    public static void damageEvent(BossReceivedDamageEvent event) {
        BattleRules rules = event.source.bc.rules;
        if(rules instanceof RaidBattleRules && event.source.getPlayerOwner()!=null) {
            ((RaidBattleRules)rules).getRaidBattle().updateHP(event.source, event.target, (int)event.damage, event.damageType);
        }
    }

}
