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
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedRectWrapper;

public class SetLocationAction implements Action {
    public final String name="SET_LOCATION";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof MapMenu){
            LocationDetails locationDetails= GoldenGlow.locationList.locations.get(value);
            CustomGuiComponentWrapper[] extraComponents=new CustomGuiComponentWrapper[2];
            int extra=0;
            if(locationDetails.canTransportTo()) {
                extraComponents[extra++] = new CustomGuiButtonWrapper(9, "Take an OU there", -104, 108, 90, 20);
            }
            else{
                extraComponents[1] = new CustomGuiButtonWrapper(9, "", -104, 138, 90, 20);
            }
            if(locationDetails.hasMoreInfo()){
                extraComponents[extra++] = new CustomGuiButtonWrapper(10, "More Info", -104, 138, 90, 20);
            }
            else{
                extraComponents[extra] = new CustomGuiButtonWrapper(10, "", -104, 138, 90, 20);
            }
            CustomGuiComponentWrapper[] components=new CustomGuiComponentWrapper[4+extra];
            components[0] = new CustomGuiTexturedRectWrapper(100, "customnpcs:textures/gui/bgfilled.png", -120, 0, 128, 256, 128, 0);
            components[1] = new CustomGuiLabelWrapper(200, locationDetails.getName(), -88, 16, 32, 16);
            components[2] = new CustomGuiButtonWrapper(7, "Players in area", -104, 48, 90, 20);
            components[3] = new CustomGuiButtonWrapper(8, "Pokemon found", -104, 78, 90, 20);
            for(int i=0;i<extra;i++){
                components[4+i]=extraComponents[i];
            }
            int[] removedIds=new int[Math.max(2-extra, 0)];
            PlayerWrapper playerWrapper=new PlayerWrapper(player);
            if(extra<2){
                for(int i=0;i<2-extra;i++){
                    removedIds[i]=extraComponents[1-i].getID();
                }
            }
            playerWrapper.updateCustomGui(components, removedIds);
        }
    }
}
