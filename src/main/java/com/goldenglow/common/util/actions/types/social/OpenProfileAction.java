package com.goldenglow.common.util.actions.types.social;

import com.goldenglow.common.guis.pokehelper.social.FriendRequestsMenu;
import com.goldenglow.common.guis.social.PlayerProfileMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class OpenProfileAction implements Action {
    private final String name="OPEN_PROFILE";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof FriendRequestsMenu){
            PlayerProfileMenu profile=new PlayerProfileMenu(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(((FriendRequestsMenu) gui).getRequestNames()[((FriendRequestsMenu) gui).getIndex()]));
            profile.init(player, null);
        }
    }
}
