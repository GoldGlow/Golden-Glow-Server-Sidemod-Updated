package com.goldenglow.common.util.actions.types.tutorials;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialMenu;
import com.goldenglow.common.guis.pokehelper.info.tutorials.TutorialsMenu;
import com.goldenglow.common.music.SongManager;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionBase;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class OpenTutorialAction extends ActionBase {
    public OpenTutorialAction(){
        super("OPEN_TUT");
    }

    @Override
    public void doAction(EntityPlayerMP player, ActionData data){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof TutorialsMenu) {
            TutorialMenu tutorialMenu = new TutorialMenu();
            tutorialMenu.setTutorial(GoldenGlow.tutorialsManager.getTutorialList()[((TutorialsMenu) gui).getIndex()]);
            tutorialMenu.init(player);
            SongManager.setCurrentSong(player, "obscureobsidian:menu.tutorial");
        }
    }
}
