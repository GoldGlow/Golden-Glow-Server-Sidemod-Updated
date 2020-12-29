package com.goldenglow.common.guis.pokehelper.info;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class InfoMenu extends EssentialsGuiBase {

    public InfoMenu() {
        super(6001);
        ActionData tutorialsButtonAction = new ActionIdData("OPEN_GUI", 6002);
        EssentialsButton tutorialsButton = new EssentialsButton(501, tutorialsButtonAction);
        this.addButton(tutorialsButton);
    }

    @Override
    public void init(EntityPlayerMP player) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addTexturedButton(501, "", 50, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 128, 0);
        gui.addTexturedButton(502, "", 110, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 32, 192);
        gui.addTexturedButton(503, "", 170, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 128, 128);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
