package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class ChatVisibilityAction extends ActionBase {
    public ChatVisibilityAction(){
        super("CHAT_VISIBILITY");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData actionData){
        if(actionData instanceof ActionStringData){
            OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
            if(((ActionStringData) actionData).getValue().equalsIgnoreCase("GLOBAL")){
                data.setGlobalChat(!data.seesGlobalChat());
            }
        }
    }
}
