package com.goldenglow.common.guis.config;

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

public class GeneralConfig implements EssentialsGuis {
    private static final int id=6101;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public GeneralConfig(){
        ActionData backButtonAction=new ActionData("OPEN_GUI", "null@"+6100);
        EssentialsButton backButton=new EssentialsButton(0, backButtonAction);
        this.addButton(backButton);
        ActionData scoreboardButtonAction=new ActionData("OPEN_OPTION", "SCOREBOARD");
        EssentialsButton scoreboardButton=new EssentialsButton(1, scoreboardButtonAction);
        this.addButton(scoreboardButton);
        ActionData reloadGuiAction=new ActionData("OPEN_GUI", "null@"+6101);
        ActionData hardButtonAction = new ActionData("COMMAND", "lp user @dp permission unset hard");
        EssentialsButton hardButton = new EssentialsButton(2, new ActionData[]{hardButtonAction, reloadGuiAction});
        this.addButton(hardButton);
        ActionData normalButtonAction = new ActionData("COMMAND", "lp user @dp permission set hard");
        EssentialsButton normalButton = new EssentialsButton(21, new ActionData[]{normalButtonAction, reloadGuiAction});
        this.addButton(normalButton);
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
        CustomGuiWrapper gui= new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "General Settings", 85, 4, 128, 20);
        gui.addLabel(101, "Scoreboard", 52, 30, 128, 20);
        gui.addTexturedButton(1, "Disabled", 122, 30, 64, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(102, "Difficulty", 67, 55, 128, 20);
        if(GoldenGlow.permissionUtils.checkPermission(player, "hard")){
            gui.addTexturedButton(2, "Hard", 122, 55, 64, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        }
        else{
            gui.addTexturedButton(21, "Normal", 122, 55, 64, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        }
        gui.addButton(0, "Back", 30, 216, 64, 20);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
