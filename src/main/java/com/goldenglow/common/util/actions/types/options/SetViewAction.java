package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.scripting.VisibilityFunctions;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class SetViewAction implements Action {
    public final String name="SET_VIEW";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        OOPlayerData data=(OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        if(value.equalsIgnoreCase("anyone")){
            data.setSeesAnyone(!data.getSeesAnyone());
        }
        else if(value.equalsIgnoreCase("friends")){
            data.setSeesFriends(!data.getSeesFriends());
        }
        VisibilityFunctions.refreshPlayerVisibility(new PlayerWrapper(player));
    }
}
