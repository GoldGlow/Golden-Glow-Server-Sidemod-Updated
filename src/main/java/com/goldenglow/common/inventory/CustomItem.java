package com.goldenglow.common.inventory;

import com.goldenglow.common.util.Requirement;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;

/**
 * Created by JeanMarc on 6/18/2019.
 */
public class CustomItem  {
    ItemStack item;
    Requirement[] requirements;
    Action[] leftClickActions;
    Action[] rightClickActions;

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

    public Action[] getLeftClickActions(){
        return this.leftClickActions;
    }

    public CustomItem setLeftClickActions(Action[] actions){
        this.leftClickActions=actions;
        return this;
    }

    public Action[] getRightClickActions(){
        return this.rightClickActions;
    }

    public void setRightClickActions(Action[] actions){
        this.rightClickActions=actions;
    }

    public Action getAction(Action[] actions, EntityPlayerMP playerMP){
        for(Action action:actions){
            if(Requirement.checkRequirements(action.requirements, playerMP)){
                return action;
            }
        }
        return null;
    }
}
