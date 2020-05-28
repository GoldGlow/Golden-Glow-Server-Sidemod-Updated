package com.goldenglow.common.util.actions.types.options;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.config.OptionListMenu;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.general.OptionScoreboard;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.general.ScoreboardType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.OptionTitle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.TitleType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.HelperSkinType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionHelperSkin;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionRouteNotification;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.RouteNotificationType;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.Action;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;

public class SaveOptionAction implements Action {
    private final String name="SAVE_OPTION";

    public String getName(){
        return this.name;
    }

    public void doAction(String actionValue, EntityPlayerMP player){
        EssentialsGuis gui=PixelmonEssentials.essentialsGuisHandler.getGui(player);
        OOPlayerData data = (OOPlayerData)player.getCapability(OOPlayerProvider.OO_DATA, null);
        if(gui instanceof OptionListMenu){
            OptionTypeManager.EnumOptionType optionType=((OptionListMenu) gui).getOptionType();
            String optionName=((OptionListMenu) gui).getItems()[((OptionListMenu) gui).getIndex()];
            if(optionName.equals("???")){
                return;
            }
            OptionType option=GoldenGlow.optionTypeManager.getOptionFromEnum(optionType);
            SingleOptionData singleOptionData=option.getOption(optionName);
            switch (optionType){
                case SCOREBOARD:
                    data.setScoreboardType(((ScoreboardType)singleOptionData).getType());
                    break;
                case HELPER_SKIN:
                    data.setHelperOption(((HelperSkinType)singleOptionData).getValue());
                    break;
                case ROUTE_NOTIFICATION:
                    data.setNotificationScheme(((RouteNotificationType)singleOptionData).getValue());
                    break;
                case TITLE:
                    if(!((TitleType)singleOptionData).getProperName(player).equals("???")){
                        GGLogger.info("test");
                        GoldenGlow.permissionUtils.unsetPermissionsWithStart(player, "prefix.3.");
                        GoldenGlow.permissionUtils.setPrefix(player, ((TitleType)singleOptionData).getProperName(player));
                    }
                    break;
                default:
                    break;
            }
            PixelmonEssentials.actionHandler.doAction(new ActionData("OPEN_GUI", "null@"+6100), player);
        }
    }
}
