package com.goldenglow.common.inventory.shops;

import com.goldenglow.common.inventory.CustomItem;
import com.pixelmonessentials.common.api.action.ActionData;
import com.pixelmonessentials.common.api.action.datatypes.ActionStringData;
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
        ActionStringData buyItem;
        if(boughtCommand.startsWith("giveitem")){
            buyItem=new ActionStringData("GIVEITEM", boughtCommand.replace("giveitem ",""));
            buyItem.requirements=new RequirementData[]{amountRequirement};
        }
        else if(boughtCommand.startsWith("depository")){
            String[] args=boughtCommand.split(" ");
            buyItem=new ActionStringData("DEPOSITORY_POKEMON", boughtCommand.replace("depository ",""));
            buyItem.requirements=new RequirementData[]{amountRequirement};
        }
        else {
            buyItem=new ActionStringData("", boughtCommand);
            buyItem.requirements=new RequirementData[]{amountRequirement};
        }
        buyItem.closeInv=false;
        this.setLeftClickActions(new ActionData[]{buyItem});
    }

    public void setRightClickActions(int sellPrice, String item){
        this.sellPrice=sellPrice;
        RequirementData itemRequirement=new RequirementData();
        itemRequirement.name="ITEM";
        itemRequirement.value=item;
        ActionData sellItem=new ActionStringData("", "givemoney @dp "+sellPrice);
        sellItem.requirements=new RequirementData[]{itemRequirement};
        sellItem.closeInv=false;
        ActionStringData notEnough=new ActionStringData("", "tellraw @dp [\"\",{\"text\":\"You do not have the items to sell!\",\"color\":\"dark_red\"}]");
        notEnough.closeInv=false;
        this.setRightClickActions(new ActionData[]{sellItem, notEnough});
    }
}
