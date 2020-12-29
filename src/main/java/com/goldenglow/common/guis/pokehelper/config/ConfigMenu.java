package com.goldenglow.common.guis.pokehelper.config;

import com.goldenglow.common.guis.pokehelper.helperSkins.Phone;
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

public class ConfigMenu extends EssentialsGuiBase {

    public ConfigMenu(){
        super(6100);
        ActionData homeButtonAction=new ActionIdData("OPEN_GUI",6000);
        EssentialsButton homeButton=new EssentialsButton(500, homeButtonAction);
        this.addButton(homeButton);
        ActionData generalButtonAction = new ActionIdData("OPEN_GUI",6102);
        EssentialsButton generalButton = new EssentialsButton(501, generalButtonAction);
        this.addButton(generalButton);
        ActionData visualButtonAction=new ActionIdData("OPEN_GUI",6103);
        EssentialsButton visualButton = new EssentialsButton(502, visualButtonAction);
        this.addButton(visualButton);
        ActionData socialButtonAction=new ActionIdData("OPEN_GUI",6104);
        EssentialsButton socialButton=new EssentialsButton(503, socialButtonAction);
        this.addButton(socialButton);
        ActionData scoreboardsAction = new ActionIdData("OPEN_GUI",6105);
        EssentialsButton scoreboardsButton = new EssentialsButton(504, scoreboardsAction);
        this.addButton(scoreboardsButton);
    }

    @Override
    public void init(EntityPlayerMP player){
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
