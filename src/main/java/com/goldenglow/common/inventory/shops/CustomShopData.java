package com.goldenglow.common.inventory.shops;

import com.goldenglow.common.inventory.CustomInventoryData;
import com.goldenglow.common.inventory.CustomItem;
import com.goldenglow.common.util.requirements.RequirementData;

public class CustomShopData extends CustomInventoryData {
    boolean pixelmonGui=false;
    CustomShopItem[][] shopItems;

    public CustomShopData(int rows, String name, String displayName, CustomShopItem[][] items, RequirementData[] requirements){
        super(rows, name, displayName, (CustomItem[][]) items, requirements);
        this.shopItems=items;
    }

    public void setPixelmonGui(boolean value){
        this.pixelmonGui=value;
    }

    public CustomShopItem[][] getItems(){
        return this.shopItems;
    }
}
