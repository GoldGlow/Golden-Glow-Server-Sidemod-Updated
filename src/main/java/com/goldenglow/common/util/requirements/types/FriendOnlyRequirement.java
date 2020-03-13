package com.goldenglow.common.util.requirements.types;

import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.requirements.Requirement;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.entity.IPlayer;

public class FriendOnlyRequirement implements Requirement {
    private final String name="FRIEND_ONLY";

    public String getName(){
        return this.name;
    }

    public boolean hasRequirement(String data, EntityPlayerMP player){
        if(data.equalsIgnoreCase("true"))
            return player.getCapability(OOPlayerProvider.OO_DATA, null).getPlayerVisibility();
        else
            return !player.getCapability(OOPlayerProvider.OO_DATA, null).getPlayerVisibility();
    }
}
