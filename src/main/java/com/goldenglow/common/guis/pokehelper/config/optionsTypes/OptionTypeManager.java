package com.goldenglow.common.guis.pokehelper.config.optionsTypes;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.general.OptionScoreboard;
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
                case ROUTE_NOTIFICATION:
                    if(option instanceof OptionRouteNotification){
                        return option;
                    }
                case TITLE:
                    if(option instanceof OptionTitle){
                        return option;
                    }
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
        TITLE
    }
}
