package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class SetDmAction implements Action {
    public final String name="SET_DM";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        if(value.equalsIgnoreCase("anyone")){
            ((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).setCanAnyoneDm(!((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).canAnyoneDm());
        }
        else if(value.equalsIgnoreCase("friends")){
            ((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).setCanFriendsDm(!((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).canFriendsDm());
        }
    }
}
