package com.goldenglow.common.data;

        import com.goldenglow.common.routes.Route;
        import com.goldenglow.common.seals.Seal;
        import com.goldenglow.common.util.Scoreboards;
        import net.minecraft.item.Item;
        import net.minecraft.item.ItemStack;

        import java.util.List;

public interface IPlayerData {

    Route getRoute();
    boolean hasRoute();
    void setRoute(Route routeName);
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
    int getPvpThemeOption();
    void setPvpThemeOption(int option);

    int getNotificationScheme();
    void setNotificationScheme(int id);

    Scoreboards.EnumScoreboardType getScoreboardType();
    void setScoreboardType(Scoreboards.EnumScoreboardType scoreboardType);

    String[] getEquippedSeals();
    List<String> getUnlockedSeals();
    void unlockSeal(String name);
    void setPlayerSeals(String[] seals);

    List<ItemStack> getKeyItems();
    void addKeyItem(ItemStack item);
    void removeKeyItem(String displayName);
    void removeKeyItem(ItemStack item);

    List<ItemStack> getTMs();
    boolean unlockTM(ItemStack tm);
}
