package com.goldenglow.common.util.actions.types.gui;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.map.data.LocationDetails;
import com.goldenglow.common.guis.pokehelper.map.MapMenu;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.controllers.CustomGuiController;

public class SetLocationAction implements Action {
    public final String name="SET_LOCATION";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
        CustomGuiWrapper guiWrapper= CustomGuiController.getOpenGui(player);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        if(gui instanceof MapMenu){
            LocationDetails locationDetails= GoldenGlow.locationList.locations.get(value);
            ((MapMenu) gui).setRoute(value);

            guiWrapper.removeComponent(511);
            guiWrapper.removeComponent(512);
            guiWrapper.removeComponent(509);
            guiWrapper.removeComponent(510);
            guiWrapper.removeComponent(100);
            guiWrapper.removeComponent(200);

            if(locationDetails.canTransportTo()) {
                guiWrapper.addButton(509, "Take an OU there", -104, 108, 90, 20);
            }
            if(locationDetails.hasMoreInfo()){
                guiWrapper.addButton(510, "More Info", -104, 138, 90, 20);
            }

            guiWrapper.addTexturedRect(100, "customnpcs:textures/gui/bgfilled.png", -120, 0, 128, 256, 128, 0);
            guiWrapper.addLabel(200, locationDetails.getName(), -88, 16, 128, 16);
            guiWrapper.addButton(511, "Players in area", -104, 48, 90, 20);
            guiWrapper.addButton(512, "Pokemon found", -104, 78, 90, 20);
            CustomGuiController.updateGui(playerWrapper, guiWrapper);
        }
    }
}
