package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.FriendRequestsMenu;
import com.goldenglow.common.util.Reference;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class AcceptFriendAction implements Action {
    private final String name="ACCEPT_FRIEND";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof FriendRequestsMenu) {
            IPlayerData playerData = player.getCapability(OOPlayerProvider.OO_DATA, null);
            EntityPlayerMP otherPlayer = FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(((FriendRequestsMenu) gui).getRequestNames()[((FriendRequestsMenu) gui).getIndex()]);
            IPlayerData otherPlayerData = otherPlayer.getCapability(OOPlayerProvider.OO_DATA, null);
            otherPlayerData.addFriend(player.getUniqueID());
            playerData.acceptFriendRequest(otherPlayer.getUniqueID());
            otherPlayer.sendMessage(new TextComponentString(Reference.darkGreen + player.getName() + " accepted your friend request!"));
            player.sendMessage(new TextComponentString(Reference.darkGreen + "You are now friends with " + otherPlayer.getName() + "!"));
            gui.init(player, null);
        }
    }
}
