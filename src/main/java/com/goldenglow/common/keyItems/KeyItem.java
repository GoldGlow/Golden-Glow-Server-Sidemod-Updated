package com.goldenglow.common.keyItems;

public class KeyItem {
    String name;
    String resourceLocation;
    String description;

    public KeyItem(){
    }

    public KeyItem(String name, String resourceLocation, String description){
        this.name=name;
        this.description=description;
        this.resourceLocation=resourceLocation;
    }

    public String getName(){
        return this.name;
    }

    public String getResourceLocation(){
        return this.resourceLocation;
    }

    public String getDescription(){
        return this.description;
    }
}
