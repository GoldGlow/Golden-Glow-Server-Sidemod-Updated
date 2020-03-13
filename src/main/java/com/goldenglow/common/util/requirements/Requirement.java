package com.goldenglow.common.util.requirements;

import net.minecraft.entity.player.EntityPlayerMP;

public interface Requirement {
    boolean hasRequirement(String data, EntityPlayerMP player);
    String getName();
}
