package com.goldenglow.common.data;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.seals.Seal;
import com.goldenglow.common.util.Scoreboards;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
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
    private int pvpThemeOption=0;

    private List<ItemStack> keyItems=new ArrayList<>();
    private List<ItemStack> tms=new ArrayList<>();

    private int notification_scheme = 0;
    private Scoreboards.EnumScoreboardType scoreboardType= Scoreboards.EnumScoreboardType.NONE;

    private String[] player_seals = new String[6];
    private List<String> unlocked_seals = new ArrayList<>();

    private EnumSpecies lastCaughtSpecies = null;
    private int captureChain;

    private boolean isEvolvingPokemon=false;
    private List<Pokemon> waitToEvolve=new ArrayList<Pokemon>();

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
    public int getPvpThemeOption(){return this.pvpThemeOption;}
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
    public Scoreboards.EnumScoreboardType getScoreboardType(){return this.scoreboardType;}
    public EnumSpecies getLastCaughtPokemon() {
        return this.lastCaughtSpecies;
    }
    public int getCaptureChain() {
        return this.captureChain;
    }

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
    public void setPvpThemeOption(int option){this.pvpThemeOption=option;}
    public void setNotificationScheme(int id) {
        this.notification_scheme = id;
    }
    public void setPlayerSeals(String[] seals) {
        this.player_seals = seals;
    }
    public void setScoreboardType(Scoreboards.EnumScoreboardType scoreboardType){this.scoreboardType=scoreboardType;}
    public void setCaptureChain(int i) {
        this.captureChain = i;
    }
    public void setLastCaughtPokemon(EnumSpecies species) {
        this.lastCaughtSpecies = species;
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
    public int increaseCaptureChain(int i) {
        return this.captureChain += i;
    }

    public List<Pokemon> getWaitToEvolve(){return this.waitToEvolve;}
    public Pokemon getPokemonWaiting(int index){return this.waitToEvolve.get(index);}
    public void removePokemonWaiting(int index){this.waitToEvolve.remove(index);}
    public void removePokemonWaiting(Pokemon pokemon){this.waitToEvolve.remove(pokemon);}
    public void addPokemonWaiting(Pokemon pokemon){this.waitToEvolve.add(pokemon);}
    public boolean isEvolvingPokemon(){return this.isEvolvingPokemon;}
    public void setEvolvingPokemon(boolean evolvingPokemon){this.isEvolvingPokemon=evolvingPokemon;}
}
