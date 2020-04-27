package com.goldenglow.common.guis;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.guis.NpcHomeGui;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class PhoneMenu implements EssentialsGuis {
    private static final int id=6000;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public PhoneMenu(){
        ActionData trainerButtonAction=new ActionData("OPEN_GUI", "null@"+6001);
        EssentialsButton trainerButton=new EssentialsButton(1, trainerButtonAction);
        this.addButton(trainerButton);
        ActionData mapButtonAction=new ActionData("OPEN_GUI", "null@"+6008);
        EssentialsButton mapButton=new EssentialsButton(3, mapButtonAction);
        this.addButton(mapButton);
        ActionData configButtonAction=new ActionData("OPEN_GUI", "null@"+6100);
        EssentialsButton configButton=new EssentialsButton(4, configButtonAction);
        this.addButton(configButton);
        ActionData bagButtonAction=new ActionData("OPEN_GUI", "null@"+6200);
        EssentialsButton bagButton=new EssentialsButton(2, bagButtonAction);
        this.addButton(bagButton);
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
        gui.addTexturedButton(1, "", 50, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 128, 0);
        gui.addTexturedButton(2, "", 110, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 32, 64);
        gui.addTexturedButton(3, "", 170, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 192, 128);
        gui.addTexturedButton(4, "", 50, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 224, 64);
        gui.addTexturedButton(5, "", 110, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 160, 0);
        gui.addTexturedButton(6, "", 170, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 0, 192);
        gui.addTexturedButton(7, "", 110, 176, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 0, 128);
        gui.addTexturedButton(8, "", 170, 176, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 32, 128);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}