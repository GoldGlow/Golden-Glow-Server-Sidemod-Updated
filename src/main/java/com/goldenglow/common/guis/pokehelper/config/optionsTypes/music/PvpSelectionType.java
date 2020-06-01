package com.goldenglow.common.guis.pokehelper.config.optionsTypes.music;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;

public class PvpSelectionType implements SingleOptionData {
    private String name;
    private String description;
    private int value;

    public PvpSelectionType(String name, String description, int value){
        this.name=name;
        this.description=description;
        this.value=value;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public int getValue(){
        return this.value;
    }
}
