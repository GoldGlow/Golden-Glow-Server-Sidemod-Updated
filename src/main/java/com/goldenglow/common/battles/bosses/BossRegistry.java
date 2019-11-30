package com.goldenglow.common.battles.bosses;

import com.goldenglow.common.battles.bosses.fights.BossBase;
import com.goldenglow.common.battles.bosses.fights.BossBirdBear;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.battles.BattleRegistry;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public class BossRegistry {
    private static HashMap<String, BossBase> bosses = new HashMap<>();

    public static void register(String registryName, BossBase bossBase) {
        if(!bosses.containsKey(registryName))
            bosses.put(registryName, bossBase);
    }

    public static void init() {
        //Load and Register json Boss files
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
        //Register hard-coded bosses here
        bosses.put("bird_bear", new BossBirdBear());
    }

    public static BattleControllerBase startBossBattle(EntityPlayerMP player, String bossName) {
        if(bosses.containsKey(bossName)) {
            BossParticipant participant = new BossParticipant(bosses.get(bossName));
            BattleControllerBase bc = BattleRegistry.startBattle(new PlayerParticipant(player), participant);
        }
        return null;
    }
}
