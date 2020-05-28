package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class ChatVisibilityAction implements Action {
    public final String name="CHAT_VISIBILITY";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        if(value.equalsIgnoreCase("GLOBAL")){
            data.setGlobalChat(!data.seesGlobalChat());
        }
    }
}
