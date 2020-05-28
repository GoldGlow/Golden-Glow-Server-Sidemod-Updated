package com.goldenglow.common.util.actions.types.oldActions;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.Scoreboards;
import com.pixelmonessentials.common.api.action.Action;
import net.minecraft.entity.player.EntityPlayerMP;

public class ScoreboardAction implements Action {
    public final String name="SCOREBOARD_TYPE";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        data.setScoreboardType(Scoreboards.EnumScoreboardType.valueOf(value));
    }
}
