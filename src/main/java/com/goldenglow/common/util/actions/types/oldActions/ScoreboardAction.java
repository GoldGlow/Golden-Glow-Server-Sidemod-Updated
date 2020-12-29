package com.goldenglow.common.util.actions.types.oldActions;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.Scoreboards;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import net.minecraft.entity.player.EntityPlayerMP;

public class ScoreboardAction extends ActionBase {
    public ScoreboardAction(){
        super("SCOREBOARD_TYPE");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData actionData){
        if(actionData instanceof ActionStringData){
            OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
            data.setScoreboardType(Scoreboards.EnumScoreboardType.valueOf(((ActionStringData) actionData).getValue()));
        }
    }
}
