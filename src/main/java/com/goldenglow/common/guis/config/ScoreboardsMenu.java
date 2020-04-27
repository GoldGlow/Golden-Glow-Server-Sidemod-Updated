package com.goldenglow.common.guis.config;

import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class ScoreboardsMenu implements EssentialsGuis {
    private static final int id = 6105;
    private ArrayList<EssentialsButton> buttons = new ArrayList<EssentialsButton>();

    public ScoreboardsMenu() {
        EssentialsButton noScoreboardButton=new EssentialsButton(1, new ActionData("SCOREBOARD_TYPE", "NONE"));
        this.addButton(noScoreboardButton);
        EssentialsButton debugButton=new EssentialsButton(2, new ActionData("SCOREBOARD_TYPE", "DEBUG"));
        this.addButton(debugButton);
        EssentialsButton questLogButton=new EssentialsButton(3, new ActionData("SCOREBOARD_TYPE", "QUEST_LOG"));
        this.addButton(questLogButton);
        EssentialsButton chainsButton=new EssentialsButton(4, new ActionData("SCOREBOARD_TYPE", "CHAIN_INFO"));
        this.addButton(chainsButton);
        EssentialsButton onlineFriendsButton=new EssentialsButton(5, new ActionData("SCOREBOARD_TYPE", "ONLINE_FRIENDS"));
        this.addButton(onlineFriendsButton);
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
        gui.addTexturedButton(1, "", 50, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 32, 0);
        gui.addTexturedButton(2, "", 110, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 224, 64);
        gui.addTexturedButton(3, "", 170, 48, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 0, 128);
        gui.addTexturedButton(4, "", 50, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 192, 64);
        gui.addTexturedButton(5, "", 110, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 160, 128);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}