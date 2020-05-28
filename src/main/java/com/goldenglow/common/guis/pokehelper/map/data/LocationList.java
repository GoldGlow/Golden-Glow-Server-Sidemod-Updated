package com.goldenglow.common.guis.pokehelper.map.data;

import java.util.HashMap;

public class LocationList {
    public HashMap<String, LocationDetails> locations=new HashMap<>();

    public void init(){
        this.locations.put("waterlil", new LocationDetails("Waterlil Town", true, true));
        this.locations.put("valleyhill", new LocationDetails("Valleyhill Trail", false, false));
        this.locations.put("acanthus", new LocationDetails("Acanthus Town", true, true));
        this.locations.put("chateau", new LocationDetails("Chateau Noir", true, false));
        this.locations.put("sakura", new LocationDetails("Sakura City", true, true));
    }
}
