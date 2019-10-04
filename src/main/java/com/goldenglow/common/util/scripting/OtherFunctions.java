package com.goldenglow.common.util.scripting;

import noppes.npcs.api.wrapper.PlayerWrapper;

public class OtherFunctions {
    public static void showAchievement(PlayerWrapper playerWrapper, String firstLine, String secondLine){
        playerWrapper.sendNotification(firstLine, secondLine, Integer.valueOf(playerWrapper.getMCEntity().getEntityData().getInteger("RouteNotification")));
    }

    public static void test() {
        System.out.println("Test works");
    }
}
