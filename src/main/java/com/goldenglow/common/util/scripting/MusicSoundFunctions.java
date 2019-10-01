package com.goldenglow.common.util.scripting;

import com.goldenglow.common.music.SongManager;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.Server;
import noppes.npcs.constants.EnumPacketClient;

public class MusicSoundFunctions {
    public static void playSound(EntityPlayerMP player, String source, String path){
        SongManager.playSound(player, source, path);
    }

    public static void playSong(EntityPlayerMP player){
        if(player.getEntityData().hasKey("Song"))
            Server.sendData(player, EnumPacketClient.PLAY_MUSIC, player.getEntityData().getString("Song"));
    }
}
