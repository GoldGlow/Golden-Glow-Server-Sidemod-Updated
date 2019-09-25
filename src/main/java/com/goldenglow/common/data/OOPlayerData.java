package com.goldenglow.common.data;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.seals.Seal;
import net.minecraft.item.ItemStack;
import noppes.npcs.api.item.IItemStack;

import java.util.ArrayList;
import java.util.List;

public class OOPlayerData implements IPlayerData {

    private Route current_route;
    private Route safezone;
    private boolean hasRouteDebug=false;

    private String current_song = "";
    private String theme_wild,theme_trainer,theme_pvp = "";

    private List<ItemStack> keyItems=new ArrayList<>();

    private int notification_scheme = 0;

    private String[] player_seals = new String[6];
    private List<String> unlocked_seals = new ArrayList<>();

    public Route getRoute() {
        return current_route;
    }
    public boolean hasRoute() {
        return this.current_route != null;
    }
    public void setRoute(Route route) {
        this.current_route = route;
    }
    public void clearRoute() {
        this.current_route = null;
    }

    public boolean getHasRouteDebug(){
        return this.hasRouteDebug;
    }
    public void setHasRouteDebug(boolean hasRouteDebug){
        this.hasRouteDebug=hasRouteDebug;
    }

    public Route getSafezone() {
        return this.safezone;
    }
    public void setSafezone(String safezoneName) {
        if (GoldenGlow.routeManager.doesRouteExist(safezoneName)) {
            this.safezone = GoldenGlow.routeManager.getRoute(safezoneName);
        }
    }

    public String getCurrentSong() {
        return this.current_song;
    }
    public void setSong(String song) {
        //Do more here
        this.current_song = song;
    }

    public String getWildTheme() {
        return this.theme_wild;
    }
    public void setWildTheme(String newTheme) {
        this.theme_wild = newTheme;
    }
    public String getTrainerTheme() {
        return this.theme_trainer;
    }
    public void setTrainerTheme(String newTheme) {
        this.theme_trainer = newTheme;
    }
    public String getPVPTheme() {
        return this.theme_pvp;
    }
    public void setPVPTheme(String newTheme) {
        this.theme_pvp = newTheme;
    }

    public int getNotificationScheme() {
        return this.notification_scheme;
    }
    public void setNotificationScheme(int id) {
        this.notification_scheme = id;
    }

    public String[] getEquippedSeals() {
        return this.player_seals.clone();
    }
    public List<String> getUnlockedSeals() {
        return this.unlocked_seals;
    }
    public void unlockSeal(String name) {
        this.unlocked_seals.add(name);
    }
    public void setPlayerSeals(String[] seals) {
        this.player_seals = seals;
    }

    public List<ItemStack> getKeyItems(){return this.keyItems;}
    public void addKeyItem(ItemStack item){this.keyItems.add(item);}
    public void removeKeyItem(String displayName){
        for(ItemStack item:this.keyItems){
            if(item.getDisplayName().equals(displayName)){
                this.keyItems.remove(item);
                return;
            }
        }
    }
    public void removeKeyItem(ItemStack item){this.keyItems.remove(item);}

}
