package com.goldenglow.common.guis.pokehelper.config.optionsTypes;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.general.OptionScoreboard;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionPvpBattle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionPvpSelection;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionTrainerBattle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.OptionWildBattle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.OptionTitle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionHelperSkin;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.OptionRouteNotification;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.RouteNotificationType;

import java.util.ArrayList;

public class OptionTypeManager {
    private ArrayList<OptionType> options=new ArrayList<>();

    public OptionTypeManager(){
        this.options.add(new OptionScoreboard());
        this.options.add(new OptionHelperSkin());
        this.options.add(new OptionRouteNotification());
        this.options.add(new OptionTitle());
        this.options.add(new OptionWildBattle());
        this.options.add(new OptionTrainerBattle());
        this.options.add(new OptionPvpBattle());
        this.options.add(new OptionPvpSelection());
    }

    public OptionType getOptionFromEnum(EnumOptionType type){
        for(OptionType option: options){
            switch(type){
                case SCOREBOARD:
                    if(option instanceof OptionScoreboard){
                        return option;
                    }
                    break;
                case HELPER_SKIN:
                    if(option instanceof OptionHelperSkin) {
                        return option;
                    }
                    break;
                case ROUTE_NOTIFICATION:
                    if(option instanceof OptionRouteNotification){
                        return option;
                    }
                    break;
                case TITLE:
                    if(option instanceof OptionTitle){
                        return option;
                    }
                    break;
                case WILD_THEME:
                    if(option instanceof OptionWildBattle){
                        return option;
                    }
                    break;
                case TRAINER_THEME:
                    if(option instanceof OptionTrainerBattle){
                        return option;
                    }
                    break;
                case PVP_THEME:
                    if(option instanceof OptionPvpBattle){
                        return option;
                    }
                    break;
                case PVP_SELECTION:
                    if(option instanceof OptionPvpSelection){
                        return option;
                    }
                    break;
                default:
                    break;
            }
        }
        return null;
    }

    public static String[] getOptionNames(ArrayList<SingleOptionData> singleOptionData){
        String[] names=new String[singleOptionData.size()];
        for(int i=0;i<singleOptionData.size();i++){
            names[i]=singleOptionData.get(i).getName();
        }
        return names;
    }

    public enum EnumOptionType{
        SCOREBOARD,
        HELPER_SKIN,
        ROUTE_NOTIFICATION,
        TITLE,
        WILD_THEME,
        TRAINER_THEME,
        PVP_THEME,
        PVP_SELECTION
    }
}
