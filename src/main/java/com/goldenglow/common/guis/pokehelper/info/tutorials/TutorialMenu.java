package com.goldenglow.common.guis.pokehelper.info.tutorials;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.info.data.TutorialData;
import com.goldenglow.common.guis.pokehelper.info.data.TutorialsInfo;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class TutorialMenu extends EssentialsGuiBase {
    private String tutorialName;
    private int tutorialPage;

    public TutorialMenu() {
        super(6003);
        this.tutorialName = "";
        this.tutorialPage = 1;
        this.addButton(new EssentialsButton(501, new ActionIdData("CHANGE_PAGE", -1)));
        this.addButton(new EssentialsButton(502, new ActionIdData("CHANGE_PAGE", 1)));
    }

    public TutorialData getTutorial(){
        return GoldenGlow.tutorialsManager.getTutorial(this.tutorialName);
    }

    public String getTutorialName(){
        return this.tutorialName;
    }

    public void setTutorial(String name){
        this.tutorialName=name;
    }

    public int getPage(){
        return this.tutorialPage;
    }

    public void setPage(int page){
        this.tutorialPage=page;
    }

    public void init(EntityPlayerMP player) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(this.getId(), 256, 256, false);
        TutorialsInfo info= GoldenGlow.tutorialsManager.getTutorial(this.tutorialName).getTutorialPage(this.tutorialPage);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(200, "Marks", 116, 10, 32, 20);
        gui.addTexturedButton(501, "", 4, 96, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 128, 192);
        gui.addTexturedButton(502, "", 220, 96, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 96, 192);
        gui.addTexturedRect(info.getPicture().getID(), info.getPicture().getTexture(), info.getPicture().getPosX(), info.getPicture().getPosY(), info.getPicture().getWidth(), info.getPicture().getHeight(), info.getPicture().getTextureX(), info.getPicture().getTextureY());
        gui.addLabel(201, info.getText(), 6, 176, 244, 64);
        gui.addLabel(203, info.getPage()+"/"+GoldenGlow.tutorialsManager.getTutorial(this.tutorialName).getPageTotal(), 128, 224, 32, 32);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
