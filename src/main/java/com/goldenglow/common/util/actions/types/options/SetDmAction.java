package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class SetDmAction extends ActionBase {
    public SetDmAction(){
        super("SET_DM");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            if(((ActionStringData) data).getValue().equalsIgnoreCase("anyone")){
                ((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).setCanAnyoneDm(!((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).canAnyoneDm());
            }
            else if(((ActionStringData) data).getValue().equalsIgnoreCase("friends")){
                ((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).setCanFriendsDm(!((OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null)).canFriendsDm());
            }
        }
    }
}
