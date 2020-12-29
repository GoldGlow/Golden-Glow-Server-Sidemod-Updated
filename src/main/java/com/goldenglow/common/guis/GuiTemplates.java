package com.goldenglow.common.guis;

import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;

public class GuiTemplates {
    public static CustomGuiWrapper getListGui(int id, String title){
        CustomGuiWrapper gui = new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addLabel(201, title, 100, 0, 128, 20);
        gui.addScroll(300, 128, 30, 118, 216, new String[0]);
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 5, 30, 118, 98);
        return gui;
    }
}
