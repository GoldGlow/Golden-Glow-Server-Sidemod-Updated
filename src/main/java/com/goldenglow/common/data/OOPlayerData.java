package com.goldenglow.common.data;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.seals.Seal;
import com.pixelmonmod.pixelmon.items.ItemTM;
import net.minecraft.item.Item;
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
    private List<ItemStack> tms=new ArrayList<>();

    private int notification_scheme = 0;

    private String[] player_seals = new String[6];
    private List<String> unlocked_seals = new ArrayList<>();

    //Getters
    public Route getRoute() {
        return current_route;
    }
    public boolean getHasRouteDebug(){
        return this.hasRouteDebug;
    }
    public Route getSafezone() {
        return this.safezone;
    }
    public String getCurrentSong() {
        return this.current_song;
    }
    public String getWildTheme() {
        return this.theme_wild;
    }
    public String getTrainerTheme() {
        return this.theme_trainer;
    }
    public String getPVPTheme() {
        return this.theme_pvp;
    }
    public int getNotificationScheme() {
        return this.notification_scheme;
    }
    public String[] getEquippedSeals() {
        return this.player_seals.clone();
    }
    public List<String> getUnlockedSeals() {
        return this.unlocked_seals;
    }
    public List<ItemStack> getKeyItems(){return this.keyItems;}
    public List<ItemStack> getTMs(){return this.tms;}

    //Setters
    public void setRoute(Route route) {
        this.current_route = route;
    }
    public void setHasRouteDebug(boolean hasRouteDebug){
        this.hasRouteDebug=hasRouteDebug;
    }
    public void setSafezone(String safezoneName) {
        if (GoldenGlow.routeManager.doesRouteExist(safezoneName)) {
            this.safezone = GoldenGlow.routeManager.getRoute(safezoneName);
        }
    }
    public void setSong(String song) {
        this.current_song = song;
    }
    public void setWildTheme(String newTheme) {
        this.theme_wild = newTheme;
    }
    public void setTrainerTheme(String newTheme) {
        this.theme_trainer = newTheme;
    }
    public void setPVPTheme(String newTheme) {
        this.theme_pvp = newTheme;
    }
    public void setNotificationScheme(int id) {
        this.notification_scheme = id;
    }
    public void setPlayerSeals(String[] seals) {
        this.player_seals = seals;
    }

    //Others
    public boolean hasRoute() {
        return this.current_route != null;
    }
    public void clearRoute() {
        this.current_route = null;
    }
    public void unlockSeal(String name) {
        this.unlocked_seals.add(name);
    }
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
    public boolean unlockTM(ItemStack tm){
        if(tm.getItem() instanceof ItemTM) {
            ItemTM newTM = (ItemTM) tm.getItem();
            if (this.tms.size() == 0) {
                this.tms.add(tm);
                return true;
            }
            for (int i = 0; i < this.tms.size(); i++) {
                ItemTM tmOriginal = (ItemTM) this.tms.get(i).getItem();
                if (tmOriginal.isHM && newTM.isHM) {
                    if (newTM.index < tmOriginal.index) {
                        this.tms.add(i, tm);
                        return true;
                    } else if (newTM.index == tmOriginal.index) {
                        return false;
                    }
                } else if (tmOriginal.isHM) {
                    this.tms.add(i, tm);
                } else if (!newTM.isHM) {
                    if (newTM.index < tmOriginal.index) {
                        this.tms.add(i, tm);
                        return true;
                    } else if (newTM.index == tmOriginal.index) {
                        return false;
                    }
                }
            }
            this.tms.add(tm);
            return true;
        }
        return false;
    }
}
