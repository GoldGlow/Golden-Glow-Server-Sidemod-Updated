package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.scripting.VisibilityFunctions;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class SetViewAction extends ActionBase {
    public SetViewAction(){
        super("SET_VIEW");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData actionData){
        if(actionData instanceof ActionStringData){
            OOPlayerData data=(OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
            if(((ActionStringData) actionData).getValue().equalsIgnoreCase("anyone")){
                data.setSeesAnyone(!data.getSeesAnyone());
            }
            else if(((ActionStringData) actionData).getValue().equalsIgnoreCase("friends")){
                data.setSeesFriends(!data.getSeesFriends());
            }
            VisibilityFunctions.refreshPlayerVisibility(new PlayerWrapper(player));
        }
    }
}
