package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.routes.Route;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class SaveSongAction implements Action {
    private final String name="SAVE_SONG";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP playerMP){
        OOPlayerData data = (OOPlayerData)playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
        String routeSong=data.getRoute().song;
        if(!data.getCurrentSong().equals(routeSong)){
            if(value.equalsIgnoreCase("trainer")){
                data.setTrainerTheme(data.getCurrentSong());
            }
            else if(value.equalsIgnoreCase("wild")){
                data.setWildTheme(data.getCurrentSong());
            }
            else if(value.equalsIgnoreCase("pvp")){
                data.setPVPTheme(data.getCurrentSong());
            }
        }
        playerMP.closeScreen();
    }
}
