package com.goldenglow.common.guis.pokehelper.config.social;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.HelperSkinType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionHelperSkin;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionRouteNotification;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.RouteNotificationType;
import com.goldenglow.common.util.PermissionUtils;
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

public class SocialConfig extends EssentialsGuiBase {
    EntityPlayerMP player;

    public SocialConfig(){
        super(6104);
        ActionData backButtonAction=new ActionIdData("OPEN_GUI", 6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData chatSettingsAction=new ActionIdData("OPEN_GUI", 6106);
        this.addButton(new EssentialsButton(501, chatSettingsAction));
        ActionData visibilityButtonAction=new ActionIdData("OPEN_GUI", 6108);
        this.addButton(new EssentialsButton(502, visibilityButtonAction));
        ActionData titleButtonAction=new ActionStringData("OPEN_OPTION", "TITLE");
        this.addButton(new EssentialsButton(503, titleButtonAction));
    }

    @Override
    public void init(EntityPlayerMP player){
        this.player=player;
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=this.getGui();
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    @Override
    public CustomGuiWrapper getGui(){
        CustomGuiWrapper gui= new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "Social Settings", 85, 4, 128, 20);
        gui.addLabel(101, "Visible Chats", 53, 30, 128, 20);
        gui.addTexturedButton(501, "Change", 122, 30, 80, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(102, "Player Visibility", 40, 55, 128, 20);
        gui.addTexturedButton(502, "Change", 122, 55, 80, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(103, "Title", 94, 80, 128, 20);
        gui.addTexturedButton(503, GoldenGlow.permissionUtils.getPrefix(player), 122, 80, 80, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addButton(500, "Back", 30, 216, 64, 20);
        return gui;
    }
}
