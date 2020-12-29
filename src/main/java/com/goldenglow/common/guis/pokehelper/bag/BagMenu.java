package com.goldenglow.common.guis.pokehelper.bag;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.guis.pokehelper.bag.category.InventoryItems;
import com.goldenglow.common.keyItems.OOItem;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionIdData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsFormGui;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonessentials.common.api.gui.EssentialsScrollGui;
import com.pixelmonessentials.common.api.gui.bases.EssentialsScrollGuiBase;
import com.pixelmonmod.pixelmon.items.ItemTM;
import moe.plushie.armourers_workshop.api.common.skin.type.ISkinType;
import moe.plushie.armourers_workshop.common.init.items.ItemSkin;
import moe.plushie.armourers_workshop.common.skin.type.chest.SkinChest;
import moe.plushie.armourers_workshop.common.skin.type.feet.SkinFeet;
import moe.plushie.armourers_workshop.common.skin.type.head.SkinHead;
import moe.plushie.armourers_workshop.common.skin.type.legs.SkinLegs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.*;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.*;

public class BagMenu extends EssentialsScrollGuiBase implements EssentialsFormGui {
    private ArrayList<OOItem> inventory=new ArrayList<OOItem>();
    private BagCategory bagCategory;
    private String[] items;
    private int index;
    private EntityPlayerMP player;

    public BagMenu() {
        super(6200);
        this.bagCategory = GoldenGlow.categoryManager.getCategory("Inventory");
        this.index = -1;
        this.addButton(new EssentialsButton(501, new ActionStringData("UPDATE_BAG", "Clothes")));
        this.addButton(new EssentialsButton(502, new ActionStringData("UPDATE_BAG", "Items")));
        this.addButton(new EssentialsButton(503, new ActionStringData("UPDATE_BAG", "Key Items")));
        this.addButton(new EssentialsButton(504, new ActionStringData("UPDATE_BAG", "TMs/HMs")));
        this.addButton(new EssentialsButton(505, new ActionStringData("UPDATE_BAG", "Berries")));
        this.addButton(new EssentialsButton(506, new ActionStringData("UPDATE_BAG", "Medicine")));
        this.addButton(new EssentialsButton(507, new ActionStringData("UPDATE_BAG", "Pokeballs")));
        this.addButton(new EssentialsButton(508, new ActionStringData("UPDATE_BAG", "Inventory")));
    }

    public BagCategory getCategory(){
        return this.bagCategory;
    }

    @Override
    public void init(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        this.player=player;
        this.bagCategory=GoldenGlow.categoryManager.getCategory("Items");
        CustomGuiWrapper gui=getGui(player);
        this.items=bagCategory.getItemNames(player);
        if(this.getIndex()==-1) {
            gui.addScroll(300, 130, 60, 115, 180, this.items);
        }
        else{
            gui.addScroll(300, 130, 60, 115, 180, this.items).setDefaultSelection(this.index);
        }
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
        playerWrapper.showCustomGui(gui);
    }


    public CustomGuiWrapper getGui(EntityPlayerMP player){
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 0, 0, 256, 20);
        gui.addLabel(200, bagCategory.getName(), 100, 0, 128, 20);
        gui.addButton(501, "0", 6, 26, 20, 20);
        gui.addButton(502, "1", 38, 26, 20, 20);
        gui.addButton(503, "2", 70, 26, 20, 20);
        gui.addButton(504, "3", 102, 26, 20, 20);
        gui.addButton(505, "4", 134, 26, 20, 20);
        gui.addButton(506, "5", 166, 26, 20, 20);
        gui.addButton(507, "6", 198, 26, 20, 20);
        gui.addButton(508, "7", 230, 26, 20, 20);
        return gui;
    }

    public CustomGuiWrapper getStoreWithdrawGui(EntityPlayerMP player){
        String action="withdraw";
        OOItem item;
        if(this.bagCategory instanceof InventoryItems){
            action="store";
            item=this.inventory.get(this.index);
        }
        else {
            item=this.bagCategory.getItem(player, this.index);
        }
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 150, 85, false);
        gui.setBackgroundTexture("customnpcs:textures/gui/bgfilled.png");
        gui.addLabel(200, "How many will you "+action+"?", 10, 5, 128, 20);
        gui.addLabel(201, "Max: "+item.getQuantity(), 15, 15, 128, 20);
        gui.addTextField(400, 30, 33, 100, 20);
        gui.addButton(512, "Submit", 28, 58, 48, 20);
        gui.addButton(513, "Cancel", 85, 58, 48, 20);
        return gui;
    }

    public void update(EntityPlayerMP player){
        CustomGuiWrapper gui= this.getGui(player);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        this.setIndex(-1);
        this.clearCategoryButtons();
        String category=bagCategory.getName();
        if(category.equals("Inventory")){
            getInventory(player);
        }
        this.items=bagCategory.getItemNames(player);
        gui.removeComponent(200);
        gui.removeComponent(300);
        gui.addLabel(200, category, 100, 0, 128, 20);
        gui.addScroll(300, 130, 60, 115, 180, this.items);
        gui.update(playerWrapper);
    }

    public void updateScroll(int id, EntityPlayerMP player){
        if(this.index!=-1) {
            PlayerWrapper playerWrapper = new PlayerWrapper(player);
            CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
            gui.removeComponent(300);
            gui.addScroll(300, 130, 60, 115, 180, this.items).setDefaultSelection(this.index);
            gui=bagCategory.generateButtons(player, gui, this);
            gui=bagCategory.displayItem(player, gui);
            this.addButton(new EssentialsButton(509, new ActionData("CATEGORY_USE")));
            this.addButton(new EssentialsButton(510, new ActionData("SELECT_AMOUNT")));
            gui.update(playerWrapper);
        }
        else{
            this.update(player);
        }
    }

    public int getIndex(){
        return this.index;
    }

    public void setIndex(int id, String index){
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

    public void setCategory(BagCategory category){
        this.clearCategoryButtons();
        this.bagCategory=category;
        this.index=-1;
    }

    public void clearCategoryButtons(){
        int[] buttons=this.bagCategory.getButtonIds();
        CustomGuiWrapper gui=CustomGuiController.getOpenGui(player);
        for(int i=0;i<buttons.length;i++){
            gui.removeComponent(buttons[i]);
            for (int j=0;j<this.getButtons().size();j++){
                if(this.getButtons().get(j).getId()==buttons[i]){
                    this.getButtons().remove(this.getButtons().get(j));
                    return;
                }
            }
        }
        gui.update(new PlayerWrapper(player));
    }

    private void getInventory(EntityPlayerMP playerMP){
        this.inventory.clear();
        HashMap<String, Integer> items=new HashMap<String, Integer>();
        Iterator<ItemStack> itemSlots=player.inventory.mainInventory.iterator();
        while(itemSlots.hasNext()){
            ItemStack itemStack=itemSlots.next();
            if(itemStack.getCount()!=0){
                if(!items.containsKey(itemStack.getItem().getRegistryName().toString())){
                    items.put(itemStack.getItem().getRegistryName().toString(), itemStack.getCount());
                }
                else{
                    items.replace(itemStack.getItem().getRegistryName().toString(), items.get(itemStack.getItem().getRegistryName().toString())+itemStack.getCount());
                }
            }
        }
        Iterator<Map.Entry<String, Integer>> itemValues=items.entrySet().iterator();
        while (itemValues.hasNext()){
            Map.Entry<String, Integer> entry=itemValues.next();
            this.inventory.add(new OOItem(entry.getKey(), entry.getValue()));
        }
    }

    public void sendForm(EntityPlayerMP player){
        boolean withdrawing=true;
        OOItem item;
        CustomGuiWrapper gui=CustomGuiController.getOpenGui(player);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        if(this.bagCategory instanceof InventoryItems){
            withdrawing=false;
            item=this.inventory.get(this.index);
        }
        else {
            item=this.bagCategory.getItem(player, this.index);
        }
        try{
            int quantity=Integer.parseInt(((CustomGuiTextFieldWrapper)gui.getComponent(400)).getText());
            if(quantity>item.getQuantity()){
                gui.addLabel(201, "Max: "+item.getQuantity(), 15, 15, 128, 20);
                gui.update(playerWrapper);
            }
            else{
                OOItem transactionItem=new OOItem(item.getItemId(), quantity);
                if(withdrawing){
                    this.bagCategory.withdrawItem(player, transactionItem);
                }
                else{
                    GoldenGlow.categoryManager.getCategory(Item.getByNameOrId(item.getItemId())).storeItem(player, transactionItem);
                }
                this.update(player);
            }
        }catch (NumberFormatException e){
            gui.addLabel(201, "Max: "+item.getQuantity(), 15, 15, 128, 20);
            gui.update(playerWrapper);
        }
    }

    public int getSubmitButton(){
        return 512;
    }

    public ArrayList<OOItem> getInventory(){
        return this.inventory;
    }
}
