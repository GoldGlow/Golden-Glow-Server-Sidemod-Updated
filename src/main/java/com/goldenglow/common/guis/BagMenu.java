package com.goldenglow.common.guis;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.actions.types.ChangeClothesAction;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonmod.pixelmon.items.ItemTM;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.items.ItemSkin;
import moe.plushie.armourers_workshop.common.skin.type.chest.SkinChest;
import moe.plushie.armourers_workshop.common.skin.type.feet.SkinFeet;
import moe.plushie.armourers_workshop.common.skin.type.head.SkinHead;
import moe.plushie.armourers_workshop.common.skin.type.legs.SkinLegs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.controllers.CustomGuiController;
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
        this.addButton(new EssentialsButton(1, new ActionData("UPDATE_BAG", "ARMOURERS_WORKSHOP")));
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
        CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        this.setIndex(-1);
        String category=this.getCategoryName();
        this.items=this.getItemNames(player);
        if(gui.getComponent(5)!=null){
            gui.getComponents().remove(gui.getComponent(5));
        }
        if(gui.getComponent(6)!=null){
            gui.getComponents().remove(gui.getComponent(6));
        }
        if(gui.getComponent(200)!=null){
            gui.getComponents().remove(gui.getComponent(200));
        }
        gui.addLabel(200, this.getCategoryName(), 100, 0, 128, 20);
        if(gui.getComponent(300)!=null){
            gui.getComponents().remove(gui.getComponent(300));
        }
        gui.addScroll(300, 128, 20, 128, 236, this.items);
        CustomGuiController.updateGui(playerWrapper, gui);
    }

    public void updateScroll(EntityPlayerMP player){
        if(this.index!=-1) {
            PlayerWrapper playerWrapper = new PlayerWrapper(player);
            CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
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
                this.addButton(new EssentialsButton(6, new ActionData("SCROLL", "-1")));
                if(gui.getComponent(5)!=null){
                    gui.getComponents().remove(gui.getComponent(5));
                }
                if(gui.getComponent(6)!=null){
                    gui.getComponents().remove(gui.getComponent(6));
                }
                gui.addTexturedButton(5, "Teach", 0, 170, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                gui.addTexturedButton(6, "Cancel", 0, 190, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                CustomGuiController.updateGui(playerWrapper, gui);
            }
            else if(this.category==EnumCategory.ARMOURERS_WORKSHOP){
                for (EssentialsButton button : this.buttons) {
                    if (button.getId() == 5) {
                        this.buttons.remove(button);
                        break;
                    }
                }
                if(gui.getComponent(5)!=null){
                    gui.getComponents().remove(gui.getComponent(5));
                }
                if(gui.getComponent(6)!=null){
                    gui.getComponents().remove(gui.getComponent(6));
                }
                this.addButton(new EssentialsButton(5, new ActionData("CHANGE_CLOTHES", "")));
                this.addButton(new EssentialsButton(6, new ActionData("SCROLL", "-1")));
                if(this.isWearing(player)){
                    gui.addTexturedButton(5, "Remove", 0, 170, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                }
                else {
                    gui.addTexturedButton(5, "Wear", 0, 170, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                }
                gui.addTexturedButton(6, "Cancel", 0, 190, 128, 20, "obscureobsidian:textures/gui/arrow_select.png", 0, 0);
                CustomGuiController.updateGui(playerWrapper, gui);
            }
        }
        else{
            this.update(player);
        }
    }

    public boolean isWearing(EntityPlayerMP player){
        IPlayerData data=player.getCapability(OOPlayerProvider.OO_DATA, null);
        ItemStack item=data.getAWItems().get(this.getIndex());
        if(item.getItem() instanceof ItemSkin){
            NBTTagCompound nbt=item.getTagCompound();
            ISkinType type=((ItemSkin) item.getItem()).getSkinType(item);
            EntityEquipmentSlot slot=null;
            if(type instanceof SkinHead){
                slot=EntityEquipmentSlot.HEAD;
            }
            else if(type instanceof SkinChest){
                slot=EntityEquipmentSlot.CHEST;
            }
            else if(type instanceof SkinLegs){
                slot=EntityEquipmentSlot.LEGS;
            }
            else if(type instanceof SkinFeet){
                slot=EntityEquipmentSlot.FEET;
            }
            if(slot!=null) {
                ItemStack armor = player.getItemStackFromSlot(slot);
                if(armor!=ItemStack.EMPTY){
                    if(armor.hasTagCompound()) {
                        return armor.getTagCompound().equals(nbt);
                    }
                }
            }
        }
        return false;
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
            case ARMOURERS_WORKSHOP:
                items=data.getAWItems();
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
            case ARMOURERS_WORKSHOP:
                return "Clothes";
            default:
                return "";
        }
    }

    public enum EnumCategory{
        KEY_ITEMS,
        TM_HM,
        ITEMS,
        BADGES,
        ARMOURERS_WORKSHOP
    }
}
