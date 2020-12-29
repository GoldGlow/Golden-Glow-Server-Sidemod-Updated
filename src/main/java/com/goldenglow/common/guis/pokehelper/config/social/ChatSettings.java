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
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.bases.EssentialsGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class ChatSettings extends EssentialsGuiBase {
    EntityPlayerMP player;

    public ChatSettings(){
        super(6106);
        ActionData backButtonAction=new ActionIdData("OPEN_GUI", 6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData dmButtonAction=new ActionIdData("OPEN_GUI", 6107);
        this.addButton(new EssentialsButton(501, dmButtonAction));
        ActionData reloadAction=new ActionData("RELOAD");
        ActionData globalChatAction=new ActionStringData("CHAT_VISIBILITY", "GLOBAL");
        this.addButton(new EssentialsButton(502, new ActionData[]{globalChatAction, reloadAction}));
    }

    @Override
    public void init(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        this.player=player;
        CustomGuiWrapper gui=this.getGui();
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    @Override
    public CustomGuiWrapper getGui(){
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        CustomGuiWrapper gui= new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "Visible Chats", 85, 4, 128, 20);
        gui.addLabel(101, "DM Settings", 61, 30, 128, 20);
        gui.addTexturedButton(501, "Change", 122, 30, 80, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(102, "Global Chat", 60, 55, 128, 20);
        if(data.seesGlobalChat()) {
            gui.addTexturedButton(502, "", 148, 55, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 216, 90);
        }
        else{
            gui.addTexturedButton(502, "", 148, 55, 20, 20, "obscureobsidian:textures/gui/phone/collective.png", 236, 90);
        }
        gui.addButton(500, "Back", 30, 216, 64, 20);
        return gui;
    }
}
