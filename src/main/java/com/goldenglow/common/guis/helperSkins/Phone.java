package com.goldenglow.common.guis.helperSkins;

import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.gui.ICustomGuiComponent;
import noppes.npcs.api.wrapper.gui.CustomGuiTexturedButtonWrapper;
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
        ICustomGuiComponent button= new CustomGuiTexturedButtonWrapper(id, "", coordinates[id-1][0], coordinates[id-1][1], 25, 25, texture, textureX, textureY).setHoverText(hover);
        gui.getComponents().add(button);
        return gui;
    }

    public static CustomGuiWrapper getPhoneGui(EntityPlayerMP player, int id){
        CustomGuiWrapper gui=new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/phone/blackphone_backgroundblue.png");
        gui.addTexturedRect(199, "obscureobsidian:textures/gui/phone/blackphone_frame.png", 0, 0, 256, 256);
        gui.addTexturedButton(0, "", 111, 211, 35, 7, "obscureobsidian:textures/gui/phone/collective.png", 0, 75);
        return gui;
    }
}
