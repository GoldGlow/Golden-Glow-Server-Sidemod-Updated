package com.goldenglow.common.util;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import net.minecraft.entity.player.EntityPlayerMP;

public class PixelmonUtils {
    public static Pokemon getPartySlot(EntityPlayerMP playerMP, int slot){
        return Pixelmon.storageManager.getParty(playerMP).get(slot);
    }
}
