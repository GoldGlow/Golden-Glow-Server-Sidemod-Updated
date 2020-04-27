package com.goldenglow.common.util.actions.types;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.TutorialMenu;
import com.goldenglow.common.guis.TutorialsMenu;
import com.goldenglow.common.guis.data.TutorialsInfo;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiComponentWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiLabelWrapper;

public class ChangeTutorialPageAction implements Action {
    public final String name="CHANGE_PAGE";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof TutorialMenu){
            ((TutorialMenu) gui).setPage(((TutorialMenu) gui).getPage()+Integer.parseInt(value));
            if(((TutorialMenu) gui).getPage()<1||((TutorialMenu) gui).getPage()>((TutorialMenu) gui).getTutorial().getPageTotal()){
                player.closeScreen();
                TutorialsMenu newMenu=new TutorialsMenu();
                newMenu.init(player, null);
            }
            CustomGuiComponentWrapper[] components=new CustomGuiComponentWrapper[4];
            TutorialsInfo page=((TutorialMenu) gui).getTutorial().getTutorialPage(((TutorialMenu) gui).getPage());
            components[0]=page.getPicture();
            components[1]=new CustomGuiLabelWrapper(201, page.getFirstLine(), 6, 192, 32, 32);
            components[2]=new CustomGuiLabelWrapper(202, page.getSecondLine(), 6, 202, 32, 32);
            components[3]=new CustomGuiLabelWrapper(203, page.getPage()+"/"+ GoldenGlow.tutorialsManager.getTutorial(((TutorialMenu) gui).getTutorialName()).getPageTotal(), 128, 224, 32, 32);
            PlayerWrapper playerWrapper=new PlayerWrapper(player);
            playerWrapper.updateCustomGui(components, new int[0]);
        }
    }
}