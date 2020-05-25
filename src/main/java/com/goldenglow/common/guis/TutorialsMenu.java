package com.goldenglow.common.guis;

import com.goldenglow.GoldenGlow;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class TutorialsMenu implements EssentialsGuis {
    private static final int id = 6002;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();
    private int index;

    public TutorialsMenu() {
        ActionData openAction = new ActionData("OPEN_TUT", "");
        EssentialsButton openButton = new EssentialsButton(1, openAction);
        this.addButton(openButton);
        this.addButton(new EssentialsButton(2, new ActionData("SCROLL", "-1")));
        this.index=-1;
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<EssentialsButton> getButtons(){
        return this.buttons;
    }

    public void addButton(EssentialsButton button) {
        this.buttons.add(button);
    }

    public void setIndex(String index){
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

    public void init(EntityPlayerMP player, EntityNPCInterface npc) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addLabel(201, "Tutorials", 100, 0, 128, 20);
        gui.addScroll(300, 128, 30, 118, 216, GoldenGlow.tutorialsManager.getTutorialList());
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 5, 30, 118, 98);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    public void updateScroll(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
        if(this.index==-1){
            if(gui.getComponent(1)!=null){
                gui.getComponents().remove(gui.getComponent(1));
            }
            if(gui.getComponent(2)!=null){
                gui.getComponents().remove(gui.getComponent(2));
            }
            if(gui.getComponent(300)!=null){
                gui.getComponents().remove(gui.getComponent(300));
            }
            if(gui.getComponent(200)!=null){
                gui.getComponents().remove(gui.getComponent(200));
            }
            gui.addScroll(300, 128, 30, 118, 216, GoldenGlow.tutorialsManager.getTutorialList());
            gui.addLabel(200, "", 7, 32, 114, 94);
        }
        else{
            String name=GoldenGlow.tutorialsManager.getTutorialList()[this.index];
            CustomGuiComponentWrapper[] components=new CustomGuiComponentWrapper[3];
            if(gui.getComponent(1)!=null){
                gui.getComponents().remove(gui.getComponent(1));
            }
            if(gui.getComponent(2)!=null){
                gui.getComponents().remove(gui.getComponent(2));
            }
            if(gui.getComponent(200)!=null){
                gui.getComponents().remove(gui.getComponent(200));
            }
            gui.addLabel(200, GoldenGlow.tutorialsManager.getTutorial(name).getDescription(), 7, 32, 114, 94);
            gui.addButton(1, "Open", 10, 183, 108, 20);
            gui.addButton(2, "Cancel", 10, 213, 108, 20);
        }
        CustomGuiController.updateGui(playerWrapper, gui);
    }
}
