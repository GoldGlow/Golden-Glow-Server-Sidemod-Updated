package com.goldenglow.common.guis.pokehelper.config.optionsTypes.music;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual.RouteNotificationType;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public class OptionPvpSelection implements OptionType {
    private String name="PVP Theme Selection";
    private ArrayList<SingleOptionData> options=new ArrayList<SingleOptionData>();

    public OptionPvpSelection(){
        this.options.add(new PvpSelectionType("Always Opponent's", "This means that you're always listening to your opponent's PVP theme.", 0));
        this.options.add(new PvpSelectionType("Not default", "This means that you're always listening to your opponent's PVP theme as long as they're not using the default one.", 1));
        this.options.add(new PvpSelectionType("If unique", "This means that you'll only listen to an opponent's theme if they have a unique one", 2));
        this.options.add(new PvpSelectionType("Never Opponent's", "This means that you'll always listen to your theme in PVP battles.", 3));
    }

    public String getName(){
        return this.name;
    }

    public PvpSelectionType getOptionFromValue(int value){
        for(SingleOptionData option:options){
            if(((PvpSelectionType)option).getValue()==value){
                return (PvpSelectionType) option;
            }
        }
        return null;
    }

    public PvpSelectionType getOption(String name){
        for(SingleOptionData option: options){
            if(option.getName().equals(name)){
                return (PvpSelectionType) option;
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
