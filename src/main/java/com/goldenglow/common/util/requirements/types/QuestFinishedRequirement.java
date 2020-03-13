package com.goldenglow.common.util.requirements.types;

import com.goldenglow.common.util.requirements.Requirement;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.entity.IPlayer;

public class QuestFinishedRequirement implements Requirement {
    private final String name="QUEST_FINISHED";

    public String getName(){
        return this.name;
    }

    public boolean hasRequirement(String data, EntityPlayerMP player){
        int id=Integer.parseInt(data);
        return ((IPlayer)player).hasFinishedQuest(id);
    }
}
