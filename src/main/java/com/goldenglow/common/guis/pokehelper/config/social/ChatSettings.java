package com.goldenglow.common.guis.pokehelper.config.social;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.HelperSkinType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionHelperSkin;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionRouteNotification;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.RouteNotificationType;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class ChatSettings implements EssentialsGuis {
    private static final int id=6106;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public ChatSettings(){
        ActionData backButtonAction=new ActionData("OPEN_GUI", "null@"+6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData dmButtonAction=new ActionData("OPEN_GUI", "null@"+6107);
        this.addButton(new EssentialsButton(501, dmButtonAction));
        ActionData reloadAction=new ActionData("OPEN_GUI", "null@"+6106);
        ActionData globalChatAction=new ActionData("CHAT_VISIBILITY", "GLOBAL");
        this.addButton(new EssentialsButton(502, new ActionData[]{globalChatAction, reloadAction}));
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
        gui.addLabel(100, "Visible Chats", 85, 4, 128, 20);
        gui.addLabel(101, "DM Settings", 61, 30, 128, 20);
        gui.addTexturedButton(501, "Change", 122, 30, 80, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(102, "Global Chat", 60, 55, 128, 20);
        if(data.seesGlobalChat()) {
            gui.addTexturedButton(502, "", 148, 55, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 216, 89);
        }
        else{
            gui.addTexturedButton(502, "", 148, 55, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 236, 89);
        }
        gui.addButton(500, "Back", 30, 216, 64, 20);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
