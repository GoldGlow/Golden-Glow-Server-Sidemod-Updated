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
    Action[] leftClickActions;
    Action[] rightClickActions;
    Action[] shiftLeftClickActions;
    Action[] shiftRightClickActions;

    public CustomItem(ItemStack item, Requirement[] requirements){
        this.item=item;
        this.requirements=requirements;
    }

    public ItemStack getItem(){
        return this.item;
    }

    public Requirement[] getRequirements(){
        return this.requirements;
    }
}
