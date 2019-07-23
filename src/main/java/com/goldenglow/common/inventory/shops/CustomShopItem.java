package com.goldenglow.common.inventory.shops;

import com.goldenglow.common.inventory.Action;
import com.goldenglow.common.inventory.CustomItem;
import com.goldenglow.common.util.Requirement;
import net.minecraft.item.ItemStack;

public class CustomShopItem extends CustomItem {
    public int sellPrice;
    public int buyPrice;

    public CustomShopItem(ItemStack item, Requirement[] requirements){
        super(item, requirements);
    }

    public void setLeftClickActions(int buyPrice, String boughtCommand){
        this.buyPrice=buyPrice;
        Requirement amountRequirement=new Requirement(Requirement.RequirementType.MONEY, buyPrice);
        Action buyItem=new Action();
        if(boughtCommand.startsWith("giveitem")){
            buyItem.setRequirements(new Requirement[]{amountRequirement});
            buyItem.actionType= Action.ActionType.GIVEITEM;
            buyItem.setValue(boughtCommand.replace("giveitem ",""));
        }
        else {
            buyItem.setRequirements(new Requirement[]{amountRequirement});
            buyItem.setValue(boughtCommand);
        }
        buyItem.closeInv=false;
        this.setLeftClickActions(new Action[]{buyItem});
    }

    public void setRightClickActions(int sellPrice, String item){
        this.sellPrice=sellPrice;
        Requirement itemRequirement=new Requirement(Requirement.RequirementType.ITEM, item);
        Action sellItem=new Action();
        sellItem.setRequirements(new Requirement[]{itemRequirement});
        sellItem.setValue("givemoney @dp "+sellPrice);
        sellItem.closeInv=false;
        Action notEnough=new Action();
        notEnough.setValue("tellraw @dp [\"\",{\"text\":\"You do not have the items to sell!\",\"color\":\"dark_red\"}]");
        notEnough.closeInv=false;
        this.setRightClickActions(new Action[]{sellItem, notEnough});
    }
}
