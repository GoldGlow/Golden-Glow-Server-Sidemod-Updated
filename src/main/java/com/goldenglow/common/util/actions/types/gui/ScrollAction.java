package com.goldenglow.common.util.actions.types.gui;

import com.goldenglow.common.guis.pokehelper.bag.BagMenu;
import com.goldenglow.common.guis.pokehelper.social.FriendRequestsMenu;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialsMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class ScrollAction extends ActionBase {
    public ScrollAction(){
        super("SCROLL");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionIdData){
            EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
            if(gui instanceof BagMenu){
                ((BagMenu) gui).setIndex(((ActionIdData) data).getId());
                ((BagMenu) gui).updateScroll(0, player);
            }
            else if(gui instanceof TutorialsMenu){
                ((TutorialsMenu) gui).setIndex(((ActionIdData) data).getId());
                ((TutorialsMenu) gui).updateScroll(0, player);
            }
            else if(gui instanceof FriendRequestsMenu){
                ((FriendRequestsMenu) gui).setIndex(((ActionIdData) data).getId());
                ((FriendRequestsMenu) gui).updateScroll(0, player);
            }
        }
    }
}
