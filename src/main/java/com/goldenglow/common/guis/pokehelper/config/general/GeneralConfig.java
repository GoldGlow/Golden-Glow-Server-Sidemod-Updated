package com.goldenglow.common.guis.pokehelper.config.general;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.Scoreboards;
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
    private static final int id=6102;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public GeneralConfig(){
        ActionData backButtonAction=new ActionData("OPEN_GUI", "null@"+6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData scoreboardButtonAction=new ActionData("OPEN_OPTION", "SCOREBOARD");
        this.addButton(new EssentialsButton(501, scoreboardButtonAction));
        ActionData reloadGuiAction=new ActionData("OPEN_GUI", "null@"+6102);
        ActionData hardButtonAction = new ActionData("COMMAND", "lp user @dp permission unset hard");
        this.addButton(new EssentialsButton(502, new ActionData[]{hardButtonAction, reloadGuiAction}));
        ActionData normalButtonAction = new ActionData("COMMAND", "lp user @dp permission set hard");
        this.addButton(new EssentialsButton(521, new ActionData[]{normalButtonAction, reloadGuiAction}));
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
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        CustomGuiWrapper gui= new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "General Settings", 85, 4, 128, 20);
        gui.addLabel(101, "Scoreboard", 52, 30, 128, 20);
        gui.addTexturedButton(501, getNameFromEnum(data.getScoreboardType()), 122, 30, 64, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(102, "Difficulty", 67, 55, 128, 20);
        if(GoldenGlow.permissionUtils.checkPermission(player, "hard")){
            gui.addTexturedButton(502, "Hard", 122, 55, 64, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        }
        else{
            gui.addTexturedButton(521, "Normal", 122, 55, 64, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        }
        gui.addButton(500, "Back", 30, 216, 64, 20);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    public static String getNameFromEnum(Scoreboards.EnumScoreboardType scoreboardType){
        switch(scoreboardType){
            case NONE:
                return "Disabled";
            case DEBUG:
                return "General";
            case QUEST_LOG:
                return "Quest Info";
            case CHAIN_INFO:
                return "Chain";
            case ONLINE_FRIENDS:
                return "Friends";
        }
        return "";
    }
}
