package com.goldenglow.common.util.scripting;

import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.music.SongManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.wrapper.NPCWrapper;

public class MusicSoundFunctions {
    //Used to play random short sounds, not really used yet, kept from leftovers from the early day music scripts
    public static void playSound(EntityPlayerMP player, String source, String path){
        SongManager.playSound(player, source, path);
    }

    //pretty much used to loop music, in the player tick scripts with the route checks
    public static void playSong(EntityPlayerMP player){
        SongManager.setCurrentSong(player, player.getCapability(OOPlayerProvider.OO_DATA, null).getCurrentSong());
    }
}
