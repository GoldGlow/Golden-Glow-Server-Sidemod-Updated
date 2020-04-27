package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class SetFriendViewAction implements Action {
    public final String name="SET_FRIEND_VIEW";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        if(value.equalsIgnoreCase("true")){
            ((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).setPlayerVisibility(true);
        }
        else{
            ((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).setPlayerVisibility(false);
        }
    }
}
