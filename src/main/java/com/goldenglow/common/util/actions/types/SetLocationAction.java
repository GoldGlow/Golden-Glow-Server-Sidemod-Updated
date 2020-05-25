package com.goldenglow.common.util.actions.types;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.data.LocationDetails;
import com.goldenglow.common.guis.MapMenu;
import com.goldenglow.common.util.GGLogger;
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

            if(guiWrapper.getComponent(9)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(9));
            }
            if(locationDetails.canTransportTo()) {
                guiWrapper.addButton(9, "Take an OU there", -104, 108, 90, 20);
            }

            if(guiWrapper.getComponent(10)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(10));
            }
            if(locationDetails.hasMoreInfo()){
                guiWrapper.addButton(10, "More Info", -104, 138, 90, 20);
            }

            if(guiWrapper.getComponent(100)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(100));
            }
            if(guiWrapper.getComponent(200)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(200));
            }
            if(guiWrapper.getComponent(7)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(7));
            }
            if(guiWrapper.getComponent(8)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(8));
            }

            guiWrapper.addTexturedRect(100, "customnpcs:textures/gui/bgfilled.png", -120, 0, 128, 256, 128, 0);
            guiWrapper.addLabel(200, locationDetails.getName(), -88, 16, 32, 16);
            guiWrapper.addButton(7, "Players in area", -104, 48, 90, 20);
            guiWrapper.addButton(8, "Pokemon found", -104, 78, 90, 20);
            CustomGuiController.updateGui(playerWrapper, guiWrapper);
        }
    }
}
