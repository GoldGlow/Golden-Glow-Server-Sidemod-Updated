package com.goldenglow.common.data;

import com.goldenglow.common.routes.Route;

public interface IPlayerData {

    Route getRoute();
    boolean hasRoute();
    void setRoute(String routeName);
    void clearRoute();
    boolean getHasRouteDebug();
    void setHasRouteDebug(boolean hasRouteDebug);

    Route getSafezone();
    void setSafezone(String safezoneName);

    String getCurrentSong();
    void setSong(String song);

    String getWildTheme();
    void setWildTheme(String newTheme);
    String getTrainerTheme();
    void setTrainerTheme(String newTheme);
    String getPVPTheme();
    void setPVPTheme(String newTheme);

    int getNotificationScheme();
    void setNotificationScheme(int id);

}
