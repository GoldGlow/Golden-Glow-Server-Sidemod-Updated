package com.goldenglow.common.util.actions.types.tutorials;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialMenu;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialsMenu;
import com.goldenglow.common.guis.pokehelper.info.data.TutorialsInfo;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;

public class ChangeTutorialPageAction extends ActionBase {
    public ChangeTutorialPageAction(){
        super("CHANGE_PAGE");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        if(data instanceof ActionIdData){
            EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
            CustomGuiWrapper guiWrapper= CustomGuiController.getOpenGui(player);
            PlayerWrapper playerWrapper=new PlayerWrapper(player);
            if(gui instanceof TutorialMenu){
                ((TutorialMenu) gui).setPage(((TutorialMenu) gui).getPage()+((ActionIdData) data).getId());
                GGLogger.info(((TutorialMenu) gui).getPage());
                if(((TutorialMenu) gui).getPage()<1||((TutorialMenu) gui).getPage()>((TutorialMenu) gui).getTutorial().getPageTotal()){
                    TutorialsMenu newMenu=new TutorialsMenu();
                    newMenu.init(player);
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
}