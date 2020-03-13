package com.goldenglow.common.util.actions;

import net.minecraft.entity.player.EntityPlayerMP;

public interface Action {
    String getName();
    void doAction(String value, EntityPlayerMP player);
}
