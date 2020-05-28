package com.goldenglow.common.guis.pokehelper.config.battlemusic;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class TrainerBattleMusic implements EssentialsGuis {
    private static final int id = 6199;
    private ArrayList<EssentialsButton> buttons = new ArrayList<EssentialsButton>();

    public TrainerBattleMusic() {
        ActionData letsGoAction = new ActionData("SET_SONG", "obscureobsidian:trainer.lgpe");
        EssentialsButton letsGoButton = new EssentialsButton(501, letsGoAction);
        this.addButton(letsGoButton);
        ActionData rbyAction = new ActionData("SET_SONG", "obscureobsidian:trainer.rby");
        EssentialsButton rbyButton = new EssentialsButton(502, rbyAction);
        this.addButton(rbyButton);
        ActionData saveAction=new ActionData("SAVE_SONG", "trainer");
        EssentialsButton saveButton=new EssentialsButton(100, saveAction);
        this.addButton(saveButton);
        ActionData cancelAction=new ActionData("OPEN_GUI", "null@"+6102);
        EssentialsButton cancelButton=new EssentialsButton(101, cancelAction);
        this.addButton(cancelButton);
    }

    public int getId() {
        return this.id;
    }

    public ArrayList<EssentialsButton> getButtons() {
        return this.buttons;
    }

    public void addButton(EssentialsButton button) {
        this.buttons.add(button);
    }

    public void init(EntityPlayerMP player, EntityNPCInterface npc) {
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addTexturedButton(501, "", 50, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 0, 192);
        gui.addTexturedButton(502, "", 110, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 0, 128);
        gui.addButton(100, "Save", 48, 210, 64, 20);
        gui.addButton(101, "Cancel", 144, 210, 64, 20);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}