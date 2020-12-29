package com.goldenglow.common.util.actions.types.friendList;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.social.FriendRequestsMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class RejectFriendAction extends ActionBase {
    public RejectFriendAction(){
        super("REJECT_FRIEND");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof FriendRequestsMenu){
            IPlayerData playerData = player.getCapability(OOPlayerProvider.OO_DATA, null);
            EntityPlayerMP otherPlayer=FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(((FriendRequestsMenu) gui).getRequestNames()[((FriendRequestsMenu) gui).getIndex()]);
            playerData.denyFriendRequest(otherPlayer.getUniqueID());
            gui.init(player);
        }
    }
}
