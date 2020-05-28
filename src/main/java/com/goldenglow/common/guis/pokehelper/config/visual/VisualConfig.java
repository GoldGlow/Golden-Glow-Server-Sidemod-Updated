package com.goldenglow.common.guis.pokehelper.config.visual;

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

public class VisualConfig implements EssentialsGuis {
    private static final int id=6103;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();

    public VisualConfig(){
        ActionData backButtonAction=new ActionData("OPEN_GUI", "null@"+6100);
        this.addButton(new EssentialsButton(500, backButtonAction));
        ActionData scoreboardButtonAction=new ActionData("OPEN_OPTION", "HELPER_SKIN");
        this.addButton(new EssentialsButton(501, scoreboardButtonAction));
        ActionData routeButtonAction=new ActionData("OPEN_OPTION", "ROUTE_NOTIFICATION");
        this.addButton(new EssentialsButton(502, routeButtonAction));
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
        OptionHelperSkin optionHelperSkin=(OptionHelperSkin) GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.HELPER_SKIN);
        HelperSkinType helperSkinType=optionHelperSkin.getOptionFromValue(data.getHelperOption());
        OptionRouteNotification optionRouteNotification=(OptionRouteNotification) GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.ROUTE_NOTIFICATION);
        RouteNotificationType routeNotificationType=optionRouteNotification.getOptionFromValue(data.getNotificationScheme());
        CustomGuiWrapper gui= new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "Visual Settings", 85, 4, 128, 20);
        gui.addLabel(101, "Helper Appearance", 20, 30, 128, 20);
        gui.addTexturedButton(501, helperSkinType.getName(), 122, 30, 80, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addLabel(102, "Route Notification", 29, 55, 128, 20);
        gui.addTexturedButton(502, routeNotificationType.getName(), 122, 55, 80, 20, "obscureobsidian:textures/gui/dark_grey_square.png");
        gui.addButton(500, "Back", 30, 216, 64, 20);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }
}
