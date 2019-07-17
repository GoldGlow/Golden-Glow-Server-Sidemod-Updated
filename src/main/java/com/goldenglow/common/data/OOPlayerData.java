package com.goldenglow.common.data;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;

public class OOPlayerData implements IPlayerData {

    private Route current_route;
    private Route safezone;
    private String current_song = "";
    private String theme_wild,theme_trainer,theme_pvp = "";
    private int notification_scheme = 0;
    
    public Route getRoute() {
        return current_route;
    }
    
    public boolean hasRoute() {
        return this.current_route != null;
    }

    public void setRoute(String routeName) {
        if (GoldenGlow.routeManager.doesRouteExist(routeName)) {
            this.current_route = GoldenGlow.routeManager.getRoute(routeName);
        }
    }

    public void clearRoute() {
        this.current_route = null;
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
}
