package com.goldenglow.common.guis.pokehelper.info.tutorials;

import com.goldenglow.GoldenGlow;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.EssentialsScrollGui;
import com.pixelmonessentials.common.api.gui.bases.EssentialsScrollGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class TutorialsMenu extends EssentialsScrollGuiBase {
    private int index;

    public TutorialsMenu() {
        super(6002);
        ActionData openAction = new ActionData("OPEN_TUT");
        EssentialsButton openButton = new EssentialsButton(501, openAction);
        this.addButton(openButton);
        this.addButton(new EssentialsButton(502, new ActionIdData("SCROLL", -1)));
        this.index=-1;
    }

    public void setIndex(int id, String index){
        String[] items=GoldenGlow.tutorialsManager.getTutorialList();
        for(int i=0;i<items.length;i++){
            if(items[i].equals(index)){
                this.index=i;
                return;
            }
        }
    }

    public void setIndex(int index){
        this.index=index;
    }

    public int getIndex(){
        return this.index;
    }

    public void init(EntityPlayerMP player) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addLabel(201, "Tutorials", 100, 0, 128, 20);
        gui.addScroll(300, 128, 30, 118, 216, GoldenGlow.tutorialsManager.getTutorialList());
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 5, 30, 118, 98);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    public void updateScroll(int id, EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
        gui.removeComponent(501);
        gui.removeComponent(502);
        gui.removeComponent(200);
        gui.removeComponent(300);
        if(this.index==-1){
            gui.addScroll(300, 128, 30, 118, 216, GoldenGlow.tutorialsManager.getTutorialList());
            gui.addLabel(200, "", 7, 32, 114, 94);
        }
        else{
            String name=GoldenGlow.tutorialsManager.getTutorialList()[this.index];
            gui.addScroll(300, 128, 30, 118, 216, GoldenGlow.tutorialsManager.getTutorialList()).setDefaultSelection(this.index);
            gui.addLabel(200, GoldenGlow.tutorialsManager.getTutorial(name).getDescription(), 7, 32, 114, 94);
            gui.addButton(501, "Open", 10, 183, 108, 20);
            gui.addButton(502, "Cancel", 10, 213, 108, 20);
        }
        CustomGuiController.updateGui(playerWrapper, gui);
    }
}
