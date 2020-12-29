package com.goldenglow.common.util.actions.types.other;

import com.goldenglow.common.music.SongManager;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class SetSongAction extends ActionBase {
    public SetSongAction(){
        super("SET_SONG");
    }

    @Override
    public void doAction(EntityPlayerMP playerMP, ActionData data){
        if(data instanceof ActionStringData){
            SongManager.setCurrentSong(playerMP, ((ActionStringData) data).getValue());
        }
    }
}
