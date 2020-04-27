package com.goldenglow.common.guis;

import com.goldenglow.GoldenGlow;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class ConfigMenu implements EssentialsGuis {
    private static final int id=6100;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public ConfigMenu(){
        ActionData routeButtonAction = new ActionData("OPEN_GUI", "null@" + 6101);
        EssentialsButton routeButton = new EssentialsButton(1, routeButtonAction);
        this.addButton(routeButton);
        ActionData reloadGuiAction=new ActionData("OPEN_GUI", "null@"+6100);
        ActionData hardButtonAction = new ActionData("COMMAND", "lp user @dp permission unset hard");
        EssentialsButton hardButton = new EssentialsButton(2, new ActionData[]{hardButtonAction, reloadGuiAction});
        this.addButton(hardButton);
        ActionData normalButtonAction = new ActionData("COMMAND", "lp user @dp permission set hard");
        EssentialsButton normalButton = new EssentialsButton(21, new ActionData[]{normalButtonAction, reloadGuiAction});
        this.addButton(normalButton);
        ActionData battleThemeAction=new ActionData("OPEN_GUI", "null@"+6102);
        EssentialsButton battleThemeButton=new EssentialsButton(5, battleThemeAction);
        this.addButton(battleThemeButton);
        ActionData scoreboardsAction = new ActionData("OPEN_GUI", "null@"+6105);
        EssentialsButton scoreboardsButton = new EssentialsButton(6, scoreboardsAction);
        this.addButton(scoreboardsButton);
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
        gui.addTexturedButton(1, "", 50, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 64, 128);
        if(GoldenGlow.permissionUtils.checkPermission(player, "hard")) {
            gui.addTexturedButton(2, "", 110, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 32, 128);
        }
        else{
            gui.addTexturedButton(21, "", 110, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 192, 0);
        }
        gui.addTexturedButton(3, "", 170, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 224, 64);
        gui.addTexturedButton(4, "", 50, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 64, 192);
        gui.addTexturedButton(5, "", 110, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 96, 128);
        gui.addTexturedButton(6, "", 170, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 32, 0);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
