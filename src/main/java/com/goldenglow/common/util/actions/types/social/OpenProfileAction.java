package com.goldenglow.common.util.actions.types.social;

import com.goldenglow.common.guis.pokehelper.social.FriendRequestsMenu;
import com.goldenglow.common.guis.social.PlayerProfileMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class OpenProfileAction extends ActionBase {
    public OpenProfileAction(){
        super("OPEN_PROFILE");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof FriendRequestsMenu){
            PlayerProfileMenu profile=new PlayerProfileMenu(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getPlayerByUsername(((FriendRequestsMenu) gui).getRequestNames()[((FriendRequestsMenu) gui).getIndex()]));
            profile.init(player);
        }
    }
}
