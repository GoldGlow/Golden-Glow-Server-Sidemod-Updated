package com.goldenglow.common.guis.pokehelper.config.optionsTypes.visual;

import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;

public class HelperSkinType implements SingleOptionData {
    private String name;
    private String description;
    private String value;

    public HelperSkinType(String name, String description, String value){
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

    public String getValue(){
        return this.value;
    }
}
