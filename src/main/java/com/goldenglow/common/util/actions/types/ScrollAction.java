package com.goldenglow.common.util.actions.types;

import com.goldenglow.common.guis.BagMenu;
import com.goldenglow.common.guis.FriendRequestsMenu;
import com.goldenglow.common.guis.TutorialsMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class ScrollAction implements Action {
    public final String name="SCROLL";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof BagMenu){
            ((BagMenu) gui).setIndex(Integer.parseInt(value));
            ((BagMenu) gui).updateScroll(player);
        }
        else if(gui instanceof TutorialsMenu){
            ((TutorialsMenu) gui).setIndex(Integer.parseInt(value));
            ((TutorialsMenu) gui).updateScroll(player);
        }
        else if(gui instanceof FriendRequestsMenu){
            ((FriendRequestsMenu) gui).setIndex(Integer.parseInt(value));
            ((FriendRequestsMenu) gui).updateScroll(player);
        }
    }
}
