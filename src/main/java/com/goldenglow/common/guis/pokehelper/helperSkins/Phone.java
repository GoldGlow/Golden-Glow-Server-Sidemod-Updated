package com.goldenglow.common.guis.pokehelper.helperSkins;

import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiButtonWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public class Phone {
    private static int[][] coordinates;

    public Phone(){
        coordinates=new int[12][2];
        for(int i=0;i<12;i++){
            coordinates[i][0]=86+29*(i%3);
            coordinates[i][1]=69+29*(i/3);
        }
    }

    public int[][] getCoordinates(){
        return this.coordinates;
    }

    public static CustomGuiWrapper addButton(CustomGuiWrapper gui, int id, String texture, int textureX, int textureY){
        gui.addTexturedButton(id, "", coordinates[id-1][0], coordinates[id-1][1], 25, 25, texture, textureX, textureY);
        return gui;
    }

    public static CustomGuiWrapper addButton(CustomGuiWrapper gui, int id, String texture, int textureX, int textureY, String hover){
        ICustomGuiComponent button= new CustomGuiButtonWrapper(id, "", coordinates[id-1][0], coordinates[id-1][1], 25, 25, texture, textureX, textureY).setHoverText(hover);
        gui.getComponents().add(button);
        return gui;
    }

    public static CustomGuiWrapper getPhoneGui(EntityPlayerMP player, int id){
        CustomGuiWrapper gui=new CustomGuiWrapper(id, 256, 256, false);
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        if (data.getHelperOption().startsWith("phone")) {
            String color=data.getHelperOption().split("_")[1];
            gui.setBackgroundTexture("obscureobsidian:textures/gui/phone/blackphone_background"+color+".png");
            gui=addButtonBasedOnColor(gui, color);
            gui.addTexturedRect(198, "obscureobsidian:textures/gui/phone/blackphone_frame.png", 0, 0, 256, 256);
        }
        return gui;
    }

    public static CustomGuiWrapper addButtonBasedOnColor(CustomGuiWrapper gui, String color){
        if(color.equals("blue")){
            gui.addTexturedButton(500, "", 111, 209, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 0, 75);
        }
        else if(color.equals("orange")){
            gui.addTexturedButton(500, "", 111, 209, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 35, 75);
        }
        else if(color.equals("indigo")){
            gui.addTexturedButton(500, "", 111, 209, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 70, 75);
        }
        else if(color.equals("purple")){
            gui.addTexturedButton(500, "", 111, 209, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 105, 75);
        }
        else if(color.equals("yellow")){
            gui.addTexturedButton(500, "", 111, 209, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 140, 75);
        }
        else if(color.equals("green")){
            gui.addTexturedButton(500, "", 111, 209, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 175, 75);
        }
        else if(color.equals("red")){
            gui.addTexturedButton(500, "", 111, 209, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 210, 75);
        }
        return gui;
    }
}
