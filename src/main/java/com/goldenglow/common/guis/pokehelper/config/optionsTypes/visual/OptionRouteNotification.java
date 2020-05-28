package com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public class OptionRouteNotification implements OptionType {
    private String name="Route Notification";
    private ArrayList<SingleOptionData> options=new ArrayList<SingleOptionData>();

    public OptionRouteNotification(){
        this.options.add(new RouteNotificationType("Theme 1", "this changes the box showing the new route message to be dark with rounded corners", 0));
        this.options.add(new RouteNotificationType("Theme 2", "this changes the box showing the new route message to be white with rounded corners", 1));
        this.options.add(new RouteNotificationType("Theme 3", "this changes the box showing the new route message to be dark blue with square corners", 2));
        this.options.add(new RouteNotificationType("Theme 4", "this changes the box showing the new route message to be white with square corners", 3));
    }

    public String getName(){
        return this.name;
    }

    public RouteNotificationType getOptionFromValue(int value){
        for(SingleOptionData option:options){
            if(((RouteNotificationType)option).getValue()==value){
                return (RouteNotificationType) option;
            }
        }
        return null;
    }

    public RouteNotificationType getOption(String name){
        for(SingleOptionData option: options){
            if(option.getName().equals(name)){
                return (RouteNotificationType) option;
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
