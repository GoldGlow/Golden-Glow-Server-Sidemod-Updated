package com.goldenglow.common.music;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import noppes.npcs.client.controllers.MusicController;

/**
 * Created by JeanMarc on 5/9/2019.
 */
public class SongManager {

    public SongManager(){

    }

    public static void playSong(EntityPlayerMP player, String sound) {
        MusicController.Instance.playStreaming(sound, player);
    }
}
