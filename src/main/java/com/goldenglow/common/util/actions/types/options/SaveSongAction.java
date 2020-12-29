package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.routes.Route;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class SaveSongAction extends ActionBase {
    public SaveSongAction(){
        super("SAVE_SONG");
    }

    @Override
    public void doAction(EntityPlayerMP playerMP, ActionData actionData){
        if(actionData instanceof ActionStringData){
            OOPlayerData data = (OOPlayerData)playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
            String routeSong=data.getRoute().song;
            if(!data.getCurrentSong().equals(routeSong)){
                if(((ActionStringData) actionData).getValue().equalsIgnoreCase("trainer")){
                    data.setTrainerTheme(data.getCurrentSong());
                }
                else if(((ActionStringData) actionData).getValue().equalsIgnoreCase("wild")){
                    data.setWildTheme(data.getCurrentSong());
                }
                else if(((ActionStringData) actionData).getValue().equalsIgnoreCase("pvp")){
                    data.setPVPTheme(data.getCurrentSong());
                }
            }
            playerMP.closeScreen();
        }
    }
}
