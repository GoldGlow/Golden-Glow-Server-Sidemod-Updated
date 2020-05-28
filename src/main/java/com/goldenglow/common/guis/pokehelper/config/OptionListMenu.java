package com.goldenglow.common.guis.pokehelper.config;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.OptionTitle;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.TitleType;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;

public class OptionListMenu implements EssentialsGuis {
    private static final int id=6101;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();
    private OptionTypeManager.EnumOptionType optionType;
    private String[] items=new String[0];
    private int index=-1;

    public OptionListMenu(){
        this.addButton(new EssentialsButton(501, new ActionData("SAVE_OPTION", "")));
        this.addButton(new EssentialsButton(502, new ActionData("OPEN_GUI", "null@"+6100)));
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<EssentialsButton> getButtons(){
        return this.buttons;
    }

    public void addButton(EssentialsButton button) {
        this.buttons.add(button);
    }

    public void setIndex(int index){
        this.index=index;
    }

    public void setIndex(String index){
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

    public void init(EntityPlayerMP player, EntityNPCInterface npc){
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        ArrayList<SingleOptionData> optionData=this.getOptions(player);
        if(this.optionType== OptionTypeManager.EnumOptionType.TITLE){
            OptionTitle titles=(OptionTitle) GoldenGlow.optionTypeManager.getOptionFromEnum(OptionTypeManager.EnumOptionType.TITLE);
            this.items=titles.getNames(player);
        }
        else {
            this.items = OptionTypeManager.getOptionNames(optionData);
        }
        CustomGuiWrapper gui = new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addLabel(201, this.getOptionName(), 100, 0, 128, 20);
        gui.addScroll(300, 128, 30, 118, 216, this.items);
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 5, 30, 118, 98);
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

    public void updateScroll(EntityPlayerMP player){
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
            if(optionType instanceof OptionTitle){
                type = optionType.getOptions().get(this.index);
                gui.addLabel(200, ((TitleType) type).getProperDescription(player), 7, 32, 114, 94);
                if(!((TitleType)type).getProperName(player).equals("???")){
                    gui.addButton(501, "Save", 10, 183, 108, 20);
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
