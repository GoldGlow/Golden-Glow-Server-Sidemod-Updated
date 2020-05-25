package com.goldenglow.common.guis.config.optionsTypes.general;

import com.goldenglow.common.guis.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.util.Scoreboards;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public class OptionScoreboard implements OptionType {
    private String name="Scoreboard";
    private ArrayList<SingleOptionData> options=new ArrayList<SingleOptionData>();

    public OptionScoreboard(){
        options.add(new ScoreboardType("Nothing", "Hide the scoreboard on the side", Scoreboards.EnumScoreboardType.NONE));
        options.add(new ScoreboardType("General", "Get some info like the in-game time, the area you're in, and what in-game day you're in.", Scoreboards.EnumScoreboardType.DEBUG));
        options.add(new ScoreboardType("Quest Info", "See the progress of your currently active quests", Scoreboards.EnumScoreboardType.QUEST_LOG));
        options.add(new ScoreboardType("Chain", "See the info about your currently active chains", Scoreboards.EnumScoreboardType.CHAIN_INFO));
        options.add(new ScoreboardType("Online Friends", "See the list of your currently online friends, along with where they are.", Scoreboards.EnumScoreboardType.ONLINE_FRIENDS));
    }

    public String getName(){
        return this.name;
    }

    public ScoreboardType getOption(String name){
        for(SingleOptionData option:options){
            if(option.getName().equals(name)){
                return (ScoreboardType) option;
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
