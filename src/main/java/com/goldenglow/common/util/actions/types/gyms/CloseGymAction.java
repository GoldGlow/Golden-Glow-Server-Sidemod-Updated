package com.goldenglow.common.util.actions.types.gyms;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.gyms.GymLeaderUtils;
import com.goldenglow.common.inventory.BagInventories;
import com.goldenglow.common.util.Scoreboards;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.action.types.guiActions.CloseGuiAction;
import net.minecraft.entity.player.EntityPlayerMP;

public class CloseGymAction extends ActionBase {
    public CloseGymAction(){
        super("CLOSE_GYM");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            GymLeaderUtils.closeGym(((ActionStringData) data).getValue());
        }
    }
}
