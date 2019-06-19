package com.goldenglow.common.inventory;

import com.goldenglow.common.util.Requirement;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Created by JeanMarc on 6/18/2019.
 */
public class CustomItem  {
    ItemStack item;
    Requirement[] requirements;

    public CustomItem(ItemStack item, Requirement[] requirements){
        this.item=item;
        this.requirements=requirements;
    }
}
