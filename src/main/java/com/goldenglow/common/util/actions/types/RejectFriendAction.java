package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.FriendRequestsMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RejectFriendAction implements Action {
    private final String name="REJECT_FRIEND";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof FriendRequestsMenu){
            IPlayerData playerData = player.getCapability(OOPlayerProvider.OO_DATA, null);
            EntityPlayerMP otherPlayer=FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(((FriendRequestsMenu) gui).getRequestNames()[((FriendRequestsMenu) gui).getIndex()]);
            playerData.denyFriendRequest(otherPlayer.getUniqueID());
            gui.init(player, null);
        }
    }
}
