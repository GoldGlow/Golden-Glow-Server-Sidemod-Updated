package com.goldenglow.common.guis.data;

public class LocationDetails {
    private String name;
    private boolean canTransportTo;
    private boolean hasMoreInfo;

    public LocationDetails(String name, boolean canTransportTo, boolean hasMoreInfo){
        this.name=name;
        this.canTransportTo=canTransportTo;
        this.hasMoreInfo=hasMoreInfo;
    }

    public String getName(){
        return this.name;
    }

    public boolean canTransportTo(){
        return this.canTransportTo;
    }

    public boolean hasMoreInfo(){
        return this.hasMoreInfo;
    }
}
