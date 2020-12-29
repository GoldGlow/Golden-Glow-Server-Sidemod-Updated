package com.goldenglow.common.util.actions.types.gyms;

import com.goldenglow.common.gyms.GymLeaderUtils;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class StartBattleAction extends ActionBase {
    public StartBattleAction(){
        super("START_BATTLE");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionStringData){
            GymLeaderUtils.startGymBattle(((ActionStringData) data).getValue());
        }
    }
}
