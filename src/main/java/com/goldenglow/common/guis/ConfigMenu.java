package com.goldenglow.common.guis;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.helperSkins.Phone;
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
        ActionData homeButtonAction=new ActionData("OPEN_GUI", "null@"+6000);
        EssentialsButton homeButton=new EssentialsButton(0, homeButtonAction);
        this.addButton(homeButton);
        ActionData generalButtonAction = new ActionData("OPEN_GUI", "null@" + 6101);
        EssentialsButton generalButton = new EssentialsButton(1, generalButtonAction);
        this.addButton(generalButton);
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
        CustomGuiWrapper gui= Phone.getPhoneGui(player, this.getId());
        gui.addTexturedButton(1, "General", 104, 71, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        gui.addTexturedButton(2, "Visual", 104, 101, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        gui.addTexturedButton(3, "Social", 104, 131, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        gui.addTexturedButton(4, "Music", 104, 161, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
