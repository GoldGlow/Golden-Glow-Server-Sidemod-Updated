package com.goldenglow.common.inventory.shops;

import com.goldenglow.common.inventory.CustomInventoryData;
import com.goldenglow.common.inventory.CustomItem;
import com.goldenglow.common.util.Requirement;
import com.pixelmonmod.pixelmon.entities.npcs.registry.ShopItem;

public class CustomShopData extends CustomInventoryData {
    CustomShopItem[][] shopItems;

    public CustomShopData(int rows, String name, CustomShopItem[][] items, Requirement[] requirements){
        super(rows, name, (CustomItem[][]) items, requirements);
        this.shopItems=items;
    }

    public CustomShopItem[][] getItems(){
        return this.shopItems;
    }
}
