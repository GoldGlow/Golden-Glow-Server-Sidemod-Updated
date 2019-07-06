package com.goldenglow.common.inventory;

import com.goldenglow.common.util.Requirement;

/**
 * Created by JeanMarc on 6/25/2019.
 */
public class CustomInventoryData {
    String name;
    CustomItem[][] items;
    Requirement[] requirements;
    int rows;

    public CustomInventoryData(int rows, String name, CustomItem[][] items, Requirement[] requirements)
    {
        this.name=name;
        this.rows=rows;
        this.items=items;
        this.requirements=requirements;
    }

    public String getName(){
        return this.name;
    }

    public int getRows(){return this.rows;}

    public CustomItem[][] getItems(){
        return this.items;
    }

    public Requirement[] getRequirements(){
        return this.requirements;
    }
}
