package com.goldenglow.common.guis.config.optionsTypes;

import com.goldenglow.common.guis.config.optionsTypes.general.OptionScoreboard;

import java.util.ArrayList;

public class OptionTypeManager {
    private ArrayList<OptionType> options=new ArrayList<>();

    public OptionTypeManager(){
        this.options.add(new OptionScoreboard());
    }

    public OptionType getOptionFromEnum(EnumOptionType type){
        for(OptionType option: options){
            switch(type){
                case SCOREBOARD:
                    if(option instanceof OptionScoreboard){
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
        SCOREBOARD
    }
}
