package com.goldenglow.common.guis;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonmod.pixelmon.items.ItemTM;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.List;

public class BagMenu implements EssentialsGuis {
    private static final int id=6200;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();
    private EnumCategory category;
    private String[] items;
    private int index;

    public BagMenu(){
        this.category=EnumCategory.ITEMS;
        this.index=-1;
        this.addButton(new EssentialsButton(1, new ActionData("UPDATE_BAG", "BADGES")));
        this.addButton(new EssentialsButton(2, new ActionData("UPDATE_BAG", "ITEMS")));
        this.addButton(new EssentialsButton(3, new ActionData("UPDATE_BAG", "KEY_ITEMS")));
        this.addButton(new EssentialsButton(4, new ActionData("UPDATE_BAG", "TM_HM")));
        this.addButton(new EssentialsButton(6, new ActionData("SCROLL", "-1")));
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

    public void init(EntityPlayerMP player, EntityNPCInterface npc){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=new CustomGuiWrapper(id, 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 0, 0, 256, 20);
        gui.addLabel(200, this.getCategoryName(), 100, 0, 128, 20);
        this.items=this.getItemNames(player);
        if(this.getIndex()==-1) {
            gui.addScroll(300, 128, 20, 128, 236, this.items);
        }
        else{
            gui.addScroll(300, 128, 20, 128, 236, this.items).setDefaultSelection(this.index);
            if(this.category==EnumCategory.TM_HM){
                for(EssentialsButton button:this.buttons){
                    if(button.getId()==5){
                        this.buttons.remove(button);
                        break;
                    }
                }
                IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
                ItemTM tm=(ItemTM) data.getTMs().get(this.index).getItem();
                this.addButton(new EssentialsButton(5, new ActionData("TM_GUI", tm.attackName)));
                this.addButton(new EssentialsButton(6, new ActionData("OPEN_GUI", "-1")));
                gui.addTexturedButton(5, "Teach", 0, 170, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                gui.addTexturedButton(6, "Cancel", 0, 190, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
            }
        }
        gui.addTexturedButton(1, "", 0, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 64, 64);
        gui.addTexturedButton(2, "", 32, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 96, 64);
        gui.addTexturedButton(3, "", 64, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 128, 64);
        gui.addTexturedButton(4, "", 96, 112, 32, 32, "obscureobsidian:textures/gui/oobuttons.png", 160, 64);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    public void update(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        this.setIndex(-1);
        String category=this.getCategoryName();
        this.items=this.getItemNames(player);
        CustomGuiLabelWrapper title=new CustomGuiLabelWrapper(200, this.getCategoryName(), 100, 0, 128, 20);
        CustomGuiScrollWrapper scroll=new CustomGuiScrollWrapper(300, 128, 20, 128, 236, this.items);
        playerWrapper.updateCustomGui(new CustomGuiComponentWrapper[]{title, scroll}, new int[]{5, 6});
    }

    public void updateScroll(EntityPlayerMP player){
        if(this.index!=-1) {
            PlayerWrapper playerWrapper = new PlayerWrapper(player);
            if (this.category == EnumCategory.TM_HM) {
                for (EssentialsButton button : this.buttons) {
                    if (button.getId() == 5) {
                        this.buttons.remove(button);
                        break;
                    }
                }
                IPlayerData data = player.getCapability(OOPlayerProvider.OO_DATA, null);
                ItemTM tm = (ItemTM) data.getTMs().get(this.index).getItem();
                this.addButton(new EssentialsButton(5, new ActionData("TM_GUI", tm.attackName)));
                this.addButton(new EssentialsButton(6, new ActionData("OPEN_GUI", "-1")));
                CustomGuiTexturedButtonWrapper teachButton = new CustomGuiTexturedButtonWrapper(5, "Teach", 0, 170, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                CustomGuiTexturedButtonWrapper cancelButton = new CustomGuiTexturedButtonWrapper(6, "Cancel", 0, 190, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                playerWrapper.updateCustomGui(new CustomGuiComponentWrapper[]{teachButton, cancelButton}, new int[0]);
            }
        }
        else{
            this.update(player);
        }
    }

    public int getIndex(){
        return this.index;
    }

    public void setIndex(String index){
        for(int i=0;i<this.items.length;i++){
            if(items[i].equals(index)){
                this.index=i;
                return;
            }
        }
    }

    public void setIndex(int index){
        this.index=index;
    }

    public void setCategory(EnumCategory category){
        this.category=category;
        this.index=-1;
    }

    public String[] getItemNames(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        List<ItemStack> items;
        switch(this.category){
            case ITEMS:
                items=data.getBagItems();
                break;
            case TM_HM:
                items=data.getTMs();
                break;
            case BADGES:
                items=new ArrayList<ItemStack>();
                break;
            case KEY_ITEMS:
                items=data.getKeyItems();
                break;
            default:
                items=new ArrayList<ItemStack>();
                break;
        }
        String[] names=new String[items.size()];
        for(int i=0;i<items.size();i++){
            names[i]=items.get(i).getDisplayName();
        }
        return names;
    }

    public String getCategoryName(){
        switch(this.category){
            case ITEMS:
                return "Items";
            case TM_HM:
                return "TMs/HMs";
            case BADGES:
                return "Badges";
            case KEY_ITEMS:
                return "Key Items";
            default:
                return  "";
        }
    }

    public enum EnumCategory{
        KEY_ITEMS,
        TM_HM,
        ITEMS,
        BADGES
    }
}
