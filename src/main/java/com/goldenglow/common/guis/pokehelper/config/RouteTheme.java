package com.goldenglow.common.guis.pokehelper.config;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class RouteTheme implements EssentialsGuis {
    private static final int id=6169;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public RouteTheme(){
        ActionData firstButtonAction = new ActionData("COMMAND", "notification @dp 0");
        EssentialsButton firstButton = new EssentialsButton(501, firstButtonAction);
        this.addButton(firstButton);
        ActionData secondButtonAction = new ActionData("COMMAND", "notification @dp 1");
        EssentialsButton secondButton = new EssentialsButton(502, secondButtonAction);
        this.addButton(secondButton);
        ActionData thirdButtonAction = new ActionData("COMMAND", "notification @dp 2");
        EssentialsButton thirdButton = new EssentialsButton(503, thirdButtonAction);
        this.addButton(thirdButton);
        ActionData fourthButtonAction = new ActionData("COMMAND", "notification @dp 3");
        EssentialsButton fourthButton = new EssentialsButton(504, fourthButtonAction);
        this.addButton(fourthButton);
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
        gui.addTexturedButton(501, "1", 50, 48, 32, 32, "obscureobsidian:textures/gui/grey_square.png");
        gui.addTexturedButton(502, "2", 110, 48, 32, 32, "obscureobsidian:textures/gui/grey_square.png");
        gui.addTexturedButton(503, "3", 170, 48, 32, 32, "obscureobsidian:textures/gui/grey_square.png");
        gui.addTexturedButton(504, "4", 50, 112, 32, 32, "obscureobsidian:textures/gui/grey_square.png");
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
