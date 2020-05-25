package com.goldenglow.common.util.actions.types;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.OOPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.config.OptionListMenu;
import com.goldenglow.common.guis.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.config.optionsTypes.general.OptionScoreboard;
import com.goldenglow.common.guis.config.optionsTypes.general.ScoreboardType;
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
            switch (optionType){
                case SCOREBOARD:
                    String optionName=((OptionListMenu) gui).getItems()[((OptionListMenu) gui).getIndex()];
                    OptionScoreboard optionScoreboard= (OptionScoreboard) GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.SCOREBOARD);
                    ScoreboardType type=optionScoreboard.getOption(optionName);
                    data.setScoreboardType(type.getType());
                    PixelmonEssentials.actionHandler.doAction(new ActionData("OPEN_GUI", "null@"+6100), player);
                    break;
                default:
                    break;
            }
        }
    }
}
