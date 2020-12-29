package com.goldenglow.common.data.player;

        import com.goldenglow.common.keyItems.OOItem;
        import com.goldenglow.common.keyItems.OOTMDummy;
        import com.goldenglow.common.routes.Route;
        import com.goldenglow.common.seals.Seal;
        import com.goldenglow.common.util.FullPos;
        import com.goldenglow.common.util.Scoreboards;
        import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
        import com.pixelmonmod.pixelmon.enums.EnumSpecies;
        import net.minecraft.item.Item;
        import net.minecraft.item.ItemStack;

        import java.time.Instant;
        import java.util.List;
        import java.util.UUID;

public interface IPlayerData {

    Route getRoute();
    boolean hasRoute();
    void setRoute(Route routeName);
    void clearRoute();

    Route getSafezone();
    void setSafezone(String safezoneName);
    FullPos getBackupFullpos();
    void setBackupFullpos(FullPos pos);

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
    String getHelperOption();
    void setHelperOption(String option);

    Scoreboards.EnumScoreboardType getScoreboardType();
    void setScoreboardType(Scoreboards.EnumScoreboardType scoreboardType);

    String[] getEquippedSeals();
    List<String> getUnlockedSeals();
    void unlockSeal(String name);
    void setPlayerSeals(String[] seals);

    List<String> getKeyItems();
    void addKeyItem(String item);
    void removeKeyItem(String displayName);

    List<OOTMDummy> getTMs();
    boolean unlockTM(OOTMDummy tm);

    List<String> getAWItems();
    void addAWItem(String itemName);
    void clearAWItems();

    List<OOItem> getBagItems();
    void addBagItem(OOItem item);
    void removeBagItem(OOItem item);
    void consumeBagItem(OOItem item);

    List<OOItem> getBerries();
    void addBerries(OOItem berries);
    void removeBerries(OOItem berries);

    List<OOItem> getMedicine();
    void addMedicine(OOItem item);
    void removeMedicine(OOItem item);

    List<OOItem> getPokeballs();
    void addPokeball(OOItem item);
    void removePokeball(OOItem item);

    int getCaptureChain();
    int increaseCaptureChain(int i);
    void setCaptureChain(int i);
    EnumSpecies getChainSpecies();
    void setChainSpecies(EnumSpecies species);
    int getKOChain();
    EnumSpecies getLastKOPokemon();
    int increaseKOChain(int i);
    void setKOChain(int i);
    void setLastKOPokemon(EnumSpecies species);

     List<UUID> getFriendRequests();
     List<UUID> getFriendList();
    void addFriendRequest(UUID player);
    void acceptFriendRequest(UUID player);
    void denyFriendRequest(UUID player);
    void addFriend(UUID player);
    void removeFriend(UUID player);

    boolean getSeesAnyone();
    void setSeesAnyone(boolean seesAnyone);
    boolean getSeesFriends();
    void setSeesFriends(boolean seesFriends);

    boolean canFriendsDm();
    void setCanFriendsDm(boolean canFriendsDm);
    boolean canAnyoneDm();
    void setCanAnyoneDm(boolean canAnyoneDm);

    List<Pokemon> getWaitToEvolve();
    Pokemon getPokemonWaiting(int index);
    void removePokemonWaiting(int index);
    void removePokemonWaiting(Pokemon pokemon);
    void addPokemonWaiting(Pokemon pokemon);
    boolean isEvolvingPokemon();
    void setEvolvingPokemon(boolean evolvingPokemon);

    boolean seesGlobalChat();
    void setGlobalChat(boolean canSee);

    void setLoginTime(Instant loginTime);
    Instant getLoginTime();

    void setDialogTicks(int ticks);
    int getDialogTicks();

    String getShopName();
    void setShopName(String name);
}
