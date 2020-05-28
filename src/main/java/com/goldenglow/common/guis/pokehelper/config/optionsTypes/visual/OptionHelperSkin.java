package com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.general.ScoreboardType;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public class OptionHelperSkin implements OptionType {
    private String name="Helper Skin";
    private ArrayList<SingleOptionData> options=new ArrayList<SingleOptionData>();

    public OptionHelperSkin(){
        options.add(new HelperSkinType("Phone Blue", "Changes the helper to a phone with a blue background", "phone_blue"));
        options.add(new HelperSkinType("Phone Green", "Changes the helper to a phone with a green background", "phone_green"));
        options.add(new HelperSkinType("Phone Indigo", "Changes the helper to a phone with an indigo background", "phone_indigo"));
        options.add(new HelperSkinType("Phone Orange", "Changes the helper to a phone with an orange background", "phone_orange"));
        options.add(new HelperSkinType("Phone Purple", "Changes the helper to a phone with a purple background", "phone_purple"));
        options.add(new HelperSkinType("Phone Red", "Changes the helper to a phone with a red background", "phone_red"));
        options.add(new HelperSkinType("Phone Yellow", "Changes the helper to a phone with a red background", "phone_yellow"));
    }

    public String getName(){
        return this.name;
    }

    public HelperSkinType getOptionFromValue(String value){
        for(SingleOptionData option:options){
            if(((HelperSkinType)option).getValue().equals(value)){
                return (HelperSkinType) option;
            }
        }
        return null;
    }

    public HelperSkinType getOption(String name){
        for(SingleOptionData option:options){
            if(option.getName().equals(name)){
                return (HelperSkinType) option;
            }
        }
        return null;
    }

    public ArrayList<SingleOptionData> getOptions(){
        return this.options;
    }

    public ArrayList<SingleOptionData> getSortedOptions(ArrayList<SingleOptionData> options, EntityPlayerMP playerMP){
        return options;
    }
}
