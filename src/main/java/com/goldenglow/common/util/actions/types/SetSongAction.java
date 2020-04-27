package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.music.SongManager;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class SetSongAction implements Action {
    public final String name="SET_SONG";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP playerMP){
        SongManager.setCurrentSong(playerMP, value);
    }
}
