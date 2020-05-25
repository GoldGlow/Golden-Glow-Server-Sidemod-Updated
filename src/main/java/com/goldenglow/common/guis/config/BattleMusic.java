package com.goldenglow.common.guis.config;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class BattleMusic implements EssentialsGuis {
    private static final int id=6199;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public BattleMusic(){
        ActionData wildThemeAction = new ActionData("OPEN_GUI", "null@"+6103);
        EssentialsButton wildThemeButton = new EssentialsButton(1, wildThemeAction);
        this.addButton(wildThemeButton);
        ActionData trainerThemeAction = new ActionData("OPEN_GUI", "null@"+6104);
        EssentialsButton trainerThemeButton = new EssentialsButton(2, trainerThemeAction);
        this.addButton(trainerThemeButton);
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

    public void init(EntityPlayerMP player, EntityNPCInterface npc){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addTexturedButton(1, "", 50, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 0, 128);
        gui.addTexturedButton(2, "", 110, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 192, 0);
        gui.addTexturedButton(3, "", 170, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 32, 128);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}