package com.goldenglow.common.guis.pokehelper.config;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.GuiTemplates;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.UnlockableOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.ThemeType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.OptionTitle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.TitleType;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.EssentialsScrollGui;
import com.pixelmonessentials.common.api.gui.bases.EssentialsScrollGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class OptionListMenu extends EssentialsScrollGuiBase {;
    private OptionTypeManager.EnumOptionType optionType;
    private String[] items=new String[0];
    private int index=-1;

    public OptionListMenu(){
        super(6101);
        this.addButton(new EssentialsButton(501, new ActionData("SAVE_OPTION")));
        this.addButton(new EssentialsButton(502, new ActionIdData("OPEN_GUI", 6100)));
    }

    public void setIndex(int index){
        this.index=index;
    }

    @Override
    public void setIndex(int id, String index){
        for(int i=0;i<this.items.length;i++){
            if(items[i].equals(index)){
                this.index=i;
                return;
            }
        }
    }

    public int getIndex(){
        return this.index;
    }

    public OptionTypeManager.EnumOptionType getOptionType(){
        return this.optionType;
    }

    public String[] getItems(){
        return this.items;
    }

    public void setOptionType(OptionTypeManager.EnumOptionType optionType){
        this.optionType=optionType;
    }

    @Override
    public void init(EntityPlayerMP player){
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        ArrayList<SingleOptionData> optionData=this.getOptions(player);
        if(this.optionType== OptionTypeManager.EnumOptionType.TITLE){
            OptionTitle titles=(OptionTitle) GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.TITLE);
            this.items=titles.getNames(player);
        }
        else {
            this.items = OptionTypeManager.getOptionNames(optionData);
        }
        CustomGuiWrapper gui = GuiTemplates.getListGui(this.getId(), this.getOptionName());
        ((CustomGuiScrollWrapper)gui.getComponent(300)).setList(this.items);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    public String getOptionName(){
        return GoldenGlow.optionTypeManager.getOptionFromEnum(this.optionType).getName();
    }

    public ArrayList<SingleOptionData> getOptions(EntityPlayerMP playerMP){
        OptionType optionType=GoldenGlow.optionTypeManager.getOptionFromEnum(this.optionType);
        ArrayList<SingleOptionData> optionData=optionType.getSortedOptions(optionType.getOptions(), playerMP);
        return optionData;
    }

    @Override
    public void updateScroll(int id, EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=CustomGuiController.getOpenGui(player);
        gui.removeComponent(501);
        gui.removeComponent(502);
        gui.removeComponent(200);
        gui.removeComponent(300);
        if(this.index==-1){
            gui.addScroll(300, 128, 30, 118, 216, this.items);
            gui.update(playerWrapper);
        }
        else{
            String optionName=this.getItems()[this.index];
            OptionType optionType=GoldenGlow.optionTypeManager.getOptionFromEnum(this.optionType);
            SingleOptionData type=null;
            if(optionType.getOptions().get(0) instanceof UnlockableOptionData){
                type = optionType.getOptions().get(this.index);
                gui.addLabel(200, ((UnlockableOptionData) type).getProperDescription(player), 7, 32, 114, 94);
                if((((UnlockableOptionData) type).isUnlocked(player))){
                    gui.addButton(501, "Save", 10, 183, 108, 20);
                }
                if(type instanceof ThemeType){
                    SongManager.setCurrentSong(player, ((ThemeType) type).getValue());
                }
            }
            else {
                type = optionType.getOption(optionName);
                gui.addLabel(200, type.getDescription(), 7, 32, 114, 94);
                gui.addButton(501, "Save", 10, 183, 108, 20);
            }
            gui.addButton(502, "Cancel", 10, 213, 108, 20);
            gui.addScroll(300, 128, 30, 118, 216, this.items).setDefaultSelection(this.index);
            gui.update(playerWrapper);
        }
    }
}
