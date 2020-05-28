package com.goldenglow.common.guis.pokehelper.config.optionsTypes;

import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;

public interface OptionType {
    public String getName();
    public ArrayList<SingleOptionData> getOptions();
    public ArrayList<SingleOptionData> getSortedOptions(ArrayList<SingleOptionData> options, EntityPlayerMP playerMP);
    public SingleOptionData getOption(String optionName);
}
