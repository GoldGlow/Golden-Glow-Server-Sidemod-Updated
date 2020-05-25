package com.goldenglow.common.guis.config.optionsTypes.general;

import com.goldenglow.common.guis.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.util.Scoreboards;

public class ScoreboardType implements SingleOptionData {
    private String name="";
    private String description="";
    private Scoreboards.EnumScoreboardType type= Scoreboards.EnumScoreboardType.NONE;

    public ScoreboardType(){}

    public ScoreboardType(String name, String description, Scoreboards.EnumScoreboardType type){
        this.name=name;
        this.description=description;
        this.type=type;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.description;
    }

    public Scoreboards.EnumScoreboardType getType(){
        return this.type;
    }
}
