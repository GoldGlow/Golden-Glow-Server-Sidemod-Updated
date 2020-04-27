package com.goldenglow.common.inventory;

import com.pixelmonessentials.common.api.requirement.RequirementData;

/**
 * Created by JeanMarc on 6/25/2019.
 */
public class CustomInventoryData {
    String name;
    String displayName;
    CustomItem[][] items;
    RequirementData[] requirements;
    int rows;

    public CustomInventoryData(int rows, String name, String displayName, CustomItem[][] items, RequirementData[] requirements)
    {
        this.name=name;
        this.displayName=displayName;
        this.rows=rows;
        this.items=items;
        this.requirements=requirements;
    }

    public String getName(){
        return this.name;
    }

    public int getRows(){return this.rows;}

    public String getDisplayName(){return this.displayName;}

    public CustomItem[][] getItems(){
        return this.items;
    }

    public RequirementData[] getRequirements(){
        return this.requirements;
    }
}