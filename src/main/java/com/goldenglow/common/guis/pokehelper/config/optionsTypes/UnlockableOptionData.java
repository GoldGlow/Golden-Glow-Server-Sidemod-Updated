package com.goldenglow.common.guis.pokehelper.config.optionsTypes;

import net.minecraft.entity.player.EntityPlayerMP;

public interface UnlockableOptionData extends SingleOptionData {
    String getProperName(EntityPlayerMP playerMP);
    String getProperDescription(EntityPlayerMP playerMP);
    boolean isUnlocked(EntityPlayerMP playerMP);
}
