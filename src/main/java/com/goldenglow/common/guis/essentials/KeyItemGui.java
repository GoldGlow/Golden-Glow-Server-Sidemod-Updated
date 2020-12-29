package com.goldenglow.common.guis.essentials;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.keyItems.KeyItem;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsScrollGui;
import com.pixelmonessentials.common.api.gui.bases.EssentialsFormGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiTextFieldWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;

public class KeyItemGui extends EssentialsFormGuiBase implements EssentialsScrollGui {
    private String keyItem;
    private boolean editMode;
    private int index;

    public KeyItemGui(){
        super(6003,500);
        this.addButton(new EssentialsButton(501, new ActionData("NEW_KEY")));
        this.addButton(new EssentialsButton(502, new ActionData("EDIT_KEY")));
        this.addButton(new EssentialsButton(503, new ActionData("DELETE_KEY")));
        this.addButton(new EssentialsButton(504, new ActionData("PREVIEW_KEY")));
        this.addButton(new EssentialsButton(505, new ActionData("CANCEL_KEY")));
    }

    public String getKeyItem(){
        return this.keyItem;
    }

    public void setEditMode(boolean editMode){
        this.editMode=editMode;
    }

    @Override
    public void init(EntityPlayerMP player){
        this.index=-1;
        this.keyItem="";
        this.editMode=false;
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui=this.getGui();
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }

    @Override
    public CustomGuiWrapper getGui(){
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "Key Items", 100, 4, 128, 20);
        gui.addButton(501, "Add", 10, 186, 110, 20);
        if(this.index>-1){
            KeyItem item=GoldenGlow.customItemManager.keyItems.get(this.keyItem);
            gui.addScroll(300, 10, 24, 110, 160, this.getKeyItems()).setDefaultSelection(this.index);
            gui.addTexturedRect(1, item.getResourceLocation(), 170, 40, 256, 256).setScale(0.125F);
            gui.addLabel(101, keyItem, 140, 70, 108, 20);
            gui.addLabel(102, item.getDescription(), 140, 98, 108, 20);
            gui.addButton(502, "Edit", 10, 208, 54, 20);
            gui.addButton(503, "Delete", 66, 208, 54, 20);
        }
        else {
            gui.addScroll(300, 10, 24, 110, 160, this.getKeyItems());
        }
        return gui;
    }

    public CustomGuiWrapper getAddItemGui(){
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(100, "New Key Item", 90, 4, 128, 20);
        gui.addLabel(101, "Name:", 60, 80, 128, 20);
        gui.addTextField(400, 100, 80, 100, 20);
        gui.addLabel(102, "Texture:", 50, 110, 128, 20);
        gui.addTextField(401, 100, 110, 100, 20);
        gui.addLabel(103, "Description:", 20, 140, 128, 20);
        gui.addTextField(402, 84, 140, 144, 20);
        gui.addButton(504, "Preview", 105, 170, 54, 20);
        gui.addButton(500, "Save", 60, 208, 54, 20);
        gui.addButton(505, "Cancel", 160, 208, 54, 20);
        return gui;
    }

    public void updateScroll(int id, EntityPlayerMP playerMP){
        CustomGuiWrapper gui=this.getGui();
        gui.update(new PlayerWrapper(playerMP));
    }

    public void setIndex(int id, String selection){
        if(id==300){
            String[] items=this.getKeyItems();
            for(int i = 0; i < items.length; i++) {
                if (selection.equals(items[i])) {
                    this.index = i;
                    this.keyItem=selection;
                    return;
                }
            }
        }
    }

    public String[] getKeyItems(){
        Object[] keys=GoldenGlow.customItemManager.keyItems.keySet().toArray();
        String[] keyItems=new String[keys.length];
        for(int i=0;i<keyItems.length;i++){
            keyItems[i]=(String) keys[i];
        }
        return keyItems;
    }

    @Override
    public void sendForm(EntityPlayerMP playerMP){
        CustomGuiWrapper gui= CustomGuiController.getOpenGui(playerMP);
        if(((CustomGuiTextFieldWrapper)gui.getComponent(400)).getText()==null){
            gui.addLabel(104, "No name was entered", 40, 20, 128, 20, 16711680);
            gui.update(new PlayerWrapper(playerMP));
            return;
        }
        if(((CustomGuiTextFieldWrapper)gui.getComponent(401)).getText()==null){
            gui.addLabel(104, "No texture was entered", 40, 20, 128, 20, 16711680);
            gui.update(new PlayerWrapper(playerMP));
            return;
        }
        if(((CustomGuiTextFieldWrapper)gui.getComponent(402)).getText()==null){
            gui.addLabel(104, "No description was entered", 40, 20, 128, 20, 16711680);
            gui.update(new PlayerWrapper(playerMP));
            return;
        }
        String name=((CustomGuiTextFieldWrapper)gui.getComponent(400)).getText();
        if(GoldenGlow.customItemManager.keyItems.containsKey(name)){
            if(!(this.editMode&&this.keyItem.equalsIgnoreCase(name))){
                gui.addLabel(104, "This key item already exists", 40, 20, 128, 20, 16711680);
                gui.update(new PlayerWrapper(playerMP));
                return;
            }
            else{
                KeyItem newItem=new KeyItem(name, ((CustomGuiTextFieldWrapper)gui.getComponent(401)).getText(), ((CustomGuiTextFieldWrapper)gui.getComponent(402)).getText());
                GoldenGlow.customItemManager.keyItems.replace(name, newItem);
            }
        }
        else{
            KeyItem newItem=new KeyItem(name, ((CustomGuiTextFieldWrapper)gui.getComponent(401)).getText(), ((CustomGuiTextFieldWrapper)gui.getComponent(402)).getText());
            GoldenGlow.customItemManager.keyItems.put(name, newItem);
        }
        this.init(playerMP);
    }
}
