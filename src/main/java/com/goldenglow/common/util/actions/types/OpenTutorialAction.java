package com.goldenglow.common.util.actions.types;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.TutorialMenu;
import com.goldenglow.common.guis.TutorialsMenu;
import com.goldenglow.common.music.SongManager;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class OpenTutorialAction implements Action {
    public final String name="OPEN_TUT";

    public String getName(){
        return this.name;
    }

    public void doAction(String value, EntityPlayerMP player){
        EssentialsGuis gui= PixelmonEssentials.essentialsGuisHandler.getGui(player);
        if(gui instanceof TutorialsMenu) {
            TutorialMenu tutorialMenu = new TutorialMenu();
            tutorialMenu.setTutorial(GoldenGlow.tutorialsManager.getTutorialList()[((TutorialsMenu) gui).getIndex()]);
            tutorialMenu.init(player, null);
            SongManager.setCurrentSong(player, "obscureobsidian:menu.tutorial");
        }
    }
}
