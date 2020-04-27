package com.goldenglow.common.inventory.shops;

import com.goldenglow.common.inventory.CustomItem;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.requirement.RequirementData;
import net.minecraft.item.ItemStack;

public class CustomShopItem extends CustomItem {
    public int sellPrice=0;
    public int buyPrice=0;

    public CustomShopItem(ItemStack item, RequirementData[] requirements){
        super(item, requirements);
    }

    public void setLeftClickActions(int buyPrice, String boughtCommand){
        this.buyPrice=buyPrice;
        RequirementData amountRequirement=new RequirementData();
        amountRequirement.name="MONEY";
        amountRequirement.value=buyPrice+"";
        ActionData buyItem=new ActionData();
        if(boughtCommand.startsWith("giveitem")){
            buyItem.requirements=new RequirementData[]{amountRequirement};
            buyItem.name="GIVEITEM";
            buyItem.value=boughtCommand.replace("giveitem ","");
        }
        else if(boughtCommand.startsWith("depository")){
            String[] args=boughtCommand.split(" ");
            buyItem.requirements=new RequirementData[]{amountRequirement};
            buyItem.name= "DEPOSITORY_POKEMON";
            buyItem.value=boughtCommand.replace("depository ","");
        }
        else {
            buyItem.requirements=new RequirementData[]{amountRequirement};
            buyItem.value=boughtCommand;
        }
        buyItem.closeInv=false;
        this.setLeftClickActions(new ActionData[]{buyItem});
    }

    public void setRightClickActions(int sellPrice, String item){
        this.sellPrice=sellPrice;
        RequirementData itemRequirement=new RequirementData();
        itemRequirement.name="ITEM";
        itemRequirement.value=item;
        ActionData sellItem=new ActionData();
        sellItem.requirements=new RequirementData[]{itemRequirement};
        sellItem.value="givemoney @dp "+sellPrice;
        sellItem.closeInv=false;
        ActionData notEnough=new ActionData();
        notEnough.value="tellraw @dp [\"\",{\"text\":\"You do not have the items to sell!\",\"color\":\"dark_red\"}]";
        notEnough.closeInv=false;
        this.setRightClickActions(new ActionData[]{sellItem, notEnough});
    }
}
