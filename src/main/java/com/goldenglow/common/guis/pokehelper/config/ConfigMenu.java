package com.goldenglow.common.guis.pokehelper.config;

import com.goldenglow.common.guis.pokehelper.helperSkins.Phone;
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
        EssentialsButton homeButton=new EssentialsButton(500, homeButtonAction);
        this.addButton(homeButton);
        ActionData generalButtonAction = new ActionData("OPEN_GUI", "null@" + 6102);
        EssentialsButton generalButton = new EssentialsButton(501, generalButtonAction);
        this.addButton(generalButton);
        ActionData visualButtonAction=new ActionData("OPEN_GUI", "null@"+6103);
        EssentialsButton visualButton = new EssentialsButton(502, visualButtonAction);
        this.addButton(visualButton);
        ActionData socialButtonAction=new ActionData("OPEN_GUI", "null@"+6104);
        EssentialsButton socialButton=new EssentialsButton(503, socialButtonAction);
        this.addButton(socialButton);
        ActionData scoreboardsAction = new ActionData("OPEN_GUI", "null@"+6105);
        EssentialsButton scoreboardsButton = new EssentialsButton(504, scoreboardsAction);
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
        gui.addTexturedButton(501, "General", 104, 71, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        gui.addTexturedButton(502, "Visual", 104, 101, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        gui.addTexturedButton(503, "Social", 104, 131, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        gui.addTexturedButton(504, "Music", 104, 161, 50, 20, "obscureobsidian:textures/gui/oobuttons.png", 160, 204);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
