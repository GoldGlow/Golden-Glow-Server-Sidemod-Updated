package com.goldenglow.common.util.actions.types.gui;

import com.goldenglow.common.guis.pokehelper.map.AreaDexMenu;
import com.goldenglow.common.guis.pokehelper.map.MapMenu;
import com.goldenglow.common.routes.Route;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class OpenSpawnsAction implements Action {
    public final String name="OPEN_SPAWNS";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof MapMenu){
            Route route=((MapMenu) gui).getRoute();
            AreaDexMenu areaDexMenu=new AreaDexMenu();
            areaDexMenu.setRoute(route);
            areaDexMenu.init(player, null);
        }
    }
}
