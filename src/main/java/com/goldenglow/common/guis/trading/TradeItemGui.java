package com.goldenglow.common.guis.trading;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.bag.BagCategory;
import com.goldenglow.common.keyItems.OOItem;
import com.goldenglow.common.trading.TradingOffer;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsScrollGui;
import com.pixelmonessentials.common.api.gui.bases.EssentialsFormGuiBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.entity.EntityCustomNpc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TradeItemGui extends EssentialsFormGuiBase implements EssentialsScrollGui {
    private ArrayList<OOItem> inventory=new ArrayList<OOItem>();
    private TradingOffer offer;
    private BagCategory bagCategory;
    private String[] items;
    private String[] offeredItems;
    private int offerIndex;
    private int bagIndex;

    public TradeItemGui(){
        super(6011, 500);
    }

    @Override
    public void init(EntityPlayerMP player){
        this.offer= GoldenGlow.tradeManager.getPlayerTrade(player).getOffer(player);
        this.bagCategory = GoldenGlow.categoryManager.getCategory("Inventory");
        this.offerIndex=-1;
        this.bagIndex=-1;
        getInventory(player);
        this.items=bagCategory.getItemNames(player);
        this.offeredItems=this.offer.getItemNames();
        this.addButton(new EssentialsButton(501, new ActionStringData("UPDATE_BAG", "Clothes")));
        this.addButton(new EssentialsButton(502, new ActionStringData("UPDATE_BAG", "Items")));
        this.addButton(new EssentialsButton(503, new ActionStringData("UPDATE_BAG", "Key Items")));
        this.addButton(new EssentialsButton(504, new ActionStringData("UPDATE_BAG", "TMs/HMs")));
        this.addButton(new EssentialsButton(505, new ActionStringData("UPDATE_BAG", "Berries")));
        this.addButton(new EssentialsButton(506, new ActionStringData("UPDATE_BAG", "Medicine")));
        this.addButton(new EssentialsButton(507, new ActionStringData("UPDATE_BAG", "Pokeballs")));
        this.addButton(new EssentialsButton(508, new ActionStringData("UPDATE_BAG", "Inventory")));
    }

    public CustomGuiWrapper getGui(EntityPlayerMP player){
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 256, 256, false);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 0, 0, 256, 20);
        gui.addLabel(200, "Items", 100, 0, 128, 20);
        gui.addLabel(201, "Offer", 48, 60, 128, 20);
        gui.addButton(501, "0", 6, 26, 20, 20);
        gui.addButton(502, "1", 38, 26, 20, 20);
        gui.addButton(503, "2", 70, 26, 20, 20);
        gui.addButton(504, "3", 102, 26, 20, 20);
        gui.addButton(505, "4", 134, 26, 20, 20);
        gui.addButton(506, "5", 166, 26, 20, 20);
        gui.addButton(507, "6", 198, 26, 20, 20);
        gui.addButton(508, "7", 230, 26, 20, 20);
        gui.addScroll(300, 130, 60, 115, 165, this.items);
        gui.addScroll(301, 8, 80, 115, 145, this.offeredItems);
        gui.addButton(500, "Confirm", 40, 230, 60, 20);
        if(this.bagIndex!=-1){
            gui.addButton(509, "Add", 160, 230, 60, 20);
        }
        else if(this.offerIndex!=-1){
            gui.addButton(509, "Remove", 160, 230, 60, 20);
        }
        return gui;
    }

    public CustomGuiWrapper getAddRemoveGui(EntityPlayerMP player){
        CustomGuiWrapper gui=new CustomGuiWrapper(this.getId(), 150, 85, false);
        return gui;
    }

    public void setCategory(BagCategory category){
        this.bagCategory=category;
        this.offerIndex=-1;
        this.bagIndex=-1;
    }

    @Override
    public void setIndex(int id, String index){
        if(id==300) {
            for(int i=0;i<this.items.length;i++){
                if(items[i].equals(index)){
                    this.bagIndex=i;
                    return;
                }
            }
        }
        else if(id==301){
            for(int i=0;i<this.offeredItems.length;i++){
                if(offeredItems[i].equals(index)){
                    this.offerIndex=i;
                    return;
                }
            }
        }
    }

    @Override
    public void updateScroll(int id, EntityPlayerMP playerMP){
        if(id==300){
            this.offerIndex=-1;
        }
        else if(id==301){
            this.bagIndex=-1;
        }
        update(playerMP);
    }

    public void update(EntityPlayerMP player){
        CustomGuiWrapper gui= this.getGui(player);
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        gui.update(playerWrapper);
    }

    private void getInventory(EntityPlayerMP player){
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

    @Override
    public void sendForm(EntityPlayerMP playerMP) {
    }
}
