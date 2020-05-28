package com.goldenglow.common.util.actions.types.tutorials;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialMenu;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialsMenu;
import com.goldenglow.common.guis.pokehelper.info.data.TutorialsInfo;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;

public class ChangeTutorialPageAction implements Action {
    public final String name="CHANGE_PAGE";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
        CustomGuiWrapper guiWrapper= CustomGuiController.getOpenGui(player);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        if(gui instanceof TutorialMenu){
            ((TutorialMenu) gui).setPage(((TutorialMenu) gui).getPage()+Integer.parseInt(value));
            if(((TutorialMenu) gui).getPage()<1||((TutorialMenu) gui).getPage()>((TutorialMenu) gui).getTutorial().getPageTotal()){
                player.closeScreen();
                TutorialsMenu newMenu=new TutorialsMenu();
                newMenu.init(player, null);
            }
            CustomGuiComponentWrapper[] components=new CustomGuiComponentWrapper[3];
            TutorialsInfo page=((TutorialMenu) gui).getTutorial().getTutorialPage(((TutorialMenu) gui).getPage());
            if(guiWrapper.getComponent(100)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(100));
            }
            if(guiWrapper.getComponent(201)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(201));
            }
            if(guiWrapper.getComponent(203)!=null){
                guiWrapper.getComponents().remove(guiWrapper.getComponent(203));
            }
            guiWrapper.getComponents().add(page.getPicture());
            guiWrapper.addLabel(201, page.getText(), 6, 176, 244, 64);
            guiWrapper.addLabel(203, page.getPage()+"/"+ GoldenGlow.tutorialsManager.getTutorial(((TutorialMenu) gui).getTutorialName()).getPageTotal(), 128, 224, 32, 32);
            CustomGuiController.updateGui(playerWrapper, guiWrapper);
        }
    }
}