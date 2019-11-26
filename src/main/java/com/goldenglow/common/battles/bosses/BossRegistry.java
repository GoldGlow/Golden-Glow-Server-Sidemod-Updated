package com.goldenglow.common.battles.bosses;

import com.goldenglow.common.battles.bosses.fights.BossBirdBear;

import java.util.HashMap;

public class BossRegistry {
    private static HashMap<String, BossBase> bosses = new HashMap<>();

    public static void register(String registryName, BossBase bossBase) {
        if(!bosses.containsKey(registryName))
            bosses.put(registryName, bossBase);
    }

    public static void init() {
        //Load and Register json Boss files here
        //Register hard-coded bosses here
        bosses.put("bird_bear", new BossBirdBear());
    }
}
