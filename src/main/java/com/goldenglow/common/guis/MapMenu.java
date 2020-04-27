package com.goldenglow.common.guis;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class MapMenu implements EssentialsGuis {
    private static final int id = 6008;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public MapMenu() {
        ActionData waterlilButtonAction = new ActionData("SET_LOCATION", "waterlil");
        EssentialsButton waterlilButton = new EssentialsButton(1, waterlilButtonAction);
        this.addButton(waterlilButton);
        ActionData valleyhillButtonAction = new ActionData("SET_LOCATION", "valleyhill");
        EssentialsButton valleyhillButton = new EssentialsButton(5, valleyhillButtonAction);
        this.addButton(valleyhillButton);
        ActionData acanthusButtonAction = new ActionData("SET_LOCATION", "acanthus");
        EssentialsButton acanthusButton = new EssentialsButton(2, acanthusButtonAction);
        this.addButton(acanthusButton);
        ActionData chateauButtonAction = new ActionData("SET_LOCATION", "chateau");
        EssentialsButton chateauButton = new EssentialsButton(3, chateauButtonAction);
        this.addButton(chateauButton);
        ActionData sakuraButtonAction = new ActionData("SET_LOCATION", "sakura");
        EssentialsButton sakuraButton = new EssentialsButton(4, sakuraButtonAction);
        this.addButton(sakuraButton);
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

    public void init(EntityPlayerMP player, EntityNPCInterface npc) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/first_part_map.png");
        gui.addTexturedButton(1, "", 44, 88, 16, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(2, "", 124, 116, 16, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(3, "", 142, 162, 16, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(4, "", 174, 132, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 160, 192);
        gui.addTexturedButton(5, "", 60, 88, 80, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        gui.addTexturedButton(5, "", 124, 88, 16, 28, "obscureobsidian:textures/gui/oobuttons.png", 160, 196);
        gui.addTexturedButton(6, "", 124, 132, 16, 24, "obscureobsidian:textures/gui/oobuttons.png", 160, 200);
        gui.addTexturedButton(6, "", 124, 140, 50, 16, "obscureobsidian:textures/gui/oobuttons.png", 160, 208);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
