package com.goldenglow.common.guis.pokehelper;

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

public class PhoneMenu extends EssentialsGuiBase {

    public PhoneMenu(){
        super(6000);
        ActionIdData homeButtonAction=new ActionIdData("OPEN_GUI", 6000);
        EssentialsButton homeButton=new EssentialsButton(0, homeButtonAction);
        this.addButton(homeButton);
        ActionIdData trainerButtonAction=new ActionIdData("OPEN_GUI", 6001);
        EssentialsButton trainerButton=new EssentialsButton(1, trainerButtonAction);
        this.addButton(trainerButton);
        ActionIdData mapButtonAction=new ActionIdData("OPEN_GUI", 6008);
        EssentialsButton mapButton=new EssentialsButton(3, mapButtonAction);
        this.addButton(mapButton);
        ActionIdData configButtonAction=new ActionIdData("OPEN_GUI", 6100);
        EssentialsButton configButton=new EssentialsButton(4, configButtonAction);
        this.addButton(configButton);
        ActionIdData bagButtonAction=new ActionIdData("OPEN_GUI", 6200);
        EssentialsButton bagButton=new EssentialsButton(2, bagButtonAction);
        this.addButton(bagButton);
    }

    @Override
    public void init(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=Phone.getPhoneGui(player, this.getId());
        gui=Phone.addButton(gui, 1, "obscureobsidian:textures/gui/phone/collective.png", 150, 0, "Info");
        gui=Phone.addButton(gui, 2, "obscureobsidian:textures/gui/phone/collective.png", 0, 0, "Bag");
        gui=Phone.addButton(gui, 3, "obscureobsidian:textures/gui/phone/collective.png", 50, 0);
        gui=Phone.addButton(gui, 4, "obscureobsidian:textures/gui/phone/collective.png", 25, 0);
        gui=Phone.addButton(gui, 5, "obscureobsidian:textures/gui/phone/collective.png", 125, 0);
        gui=Phone.addButton(gui, 6, "obscureobsidian:textures/gui/phone/collective.png", 75, 0);
        gui=Phone.addButton(gui, 7, "obscureobsidian:textures/gui/phone/collective.png", 175, 0);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}