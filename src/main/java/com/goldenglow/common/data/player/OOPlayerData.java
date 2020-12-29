package com.goldenglow.common.data.player;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.keyItems.OOItem;
import com.goldenglow.common.keyItems.OOTMDummy;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.util.FullPos;
import com.goldenglow.common.util.Scoreboards;
import com.pixelmonmod.pixelmon.api.pokemon.Pokemon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.items.ItemTM;
import moe.plushie.armourers_workshop.common.library.LibraryFile;
import moe.plushie.armourers_workshop.common.skin.cache.CommonSkinCache;
import moe.plushie.armourers_workshop.common.skin.data.Skin;
import moe.plushie.armourers_workshop.common.skin.data.SkinDescriptor;
import moe.plushie.armourers_workshop.common.skin.data.SkinIdentifier;
import moe.plushie.armourers_workshop.utils.SkinIOUtils;
import moe.plushie.armourers_workshop.utils.SkinNBTHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OOPlayerData implements IPlayerData {

    private Route current_route;
    private Route safezone;
    private FullPos backupFullPos;

    private String current_song = "";
    private String theme_wild,theme_trainer,theme_pvp = "";
    private int pvpThemeOption=0;
    private String shopName="";

    private List<String> keyItems=new ArrayList<>();
    private List<OOTMDummy> tms=new ArrayList<>();
    private List<String> awItems=new ArrayList<>();
    private List<OOItem> bagItems=new ArrayList<>();
    private List<OOItem> berries=new ArrayList<>();
    private List<OOItem> medicine=new ArrayList<>();
    private List<OOItem> pokeballs=new ArrayList<>();

    private int notification_scheme = 0;
    private Scoreboards.EnumScoreboardType scoreboardType= Scoreboards.EnumScoreboardType.NONE;
    private String helperOption="";

    private String[] player_seals = new String[6];
    private List<String> unlocked_seals = new ArrayList<>();

    private EnumSpecies lastCaughtSpecies = null;
    private int captureChain;
    private EnumSpecies lastKOSpecies = null;
    private int KOChain;

    private boolean isEvolvingPokemon=false;
    private List<Pokemon> waitToEvolve=new ArrayList<Pokemon>();

    private Instant loginTime;

    private int dialogTicks;

    private List<UUID> friendRequests=new ArrayList<>();
    private List<UUID> friendList=new ArrayList<>();

    private boolean seesAnyone=true;
    private boolean seesFriends=true;

    private boolean globalChat=true;
    private boolean canAnyoneDm=true;
    private boolean canFriendsDm=true;

    //route methods
    public Route getRoute() {
        return current_route;
    }
    public Route getSafezone() {
        return this.safezone;
    }
    public FullPos getBackupFullpos(){return this.backupFullPos;}

    public void setRoute(Route route) {
        this.current_route = route;
    }
    public void setSafezone(String safezoneName) {
        if (GoldenGlow.routeManager.doesRouteExist(safezoneName)) {
            this.safezone = GoldenGlow.routeManager.getRoute(safezoneName);
        }
    }
    public void setBackupFullpos(FullPos fullPos){this.backupFullPos=fullPos;}

    public boolean hasRoute() {
        return this.current_route != null;
    }
    public void clearRoute() {
        this.current_route = null;
    }

    //song methods
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

    //visual option methods
    public int getNotificationScheme() {
        return this.notification_scheme;
    }
    public Scoreboards.EnumScoreboardType getScoreboardType(){return this.scoreboardType;}
    public String getHelperOption(){return this.helperOption;}

    public void setNotificationScheme(int id) {
        this.notification_scheme = id;
    }
    public void setScoreboardType(Scoreboards.EnumScoreboardType scoreboardType){this.scoreboardType=scoreboardType;}
    public void setHelperOption(String option){
        this.helperOption=option;
    }

    //bag-related methods
    public String[] getEquippedSeals() {
        return this.player_seals.clone();
    }
    public List<String> getUnlockedSeals() {
        return this.unlocked_seals;
    }
    public List<String> getAWItems(){return this.awItems;}
    public List<OOItem> getBagItems(){return this.bagItems;}
    public List<String> getKeyItems(){return this.keyItems;}
    public List<OOTMDummy> getTMs(){return this.tms;}
    public List<OOItem> getBerries(){
        return this.berries;
    }
    public List<OOItem> getMedicine(){
        return this.medicine;
    }
    public List<OOItem> getPokeballs(){
        return this.pokeballs;
    }

    public void setPlayerSeals(String[] seals) {
        this.player_seals = seals;
    }

    public void unlockSeal(String name) {
        this.unlocked_seals.add(name);
    }
    public void addKeyItem(String item){this.keyItems.add(item);}
    public void removeKeyItem(String item){this.keyItems.remove(item);}
    public void addAWItem(String awItem){
        LibraryFile file=new LibraryFile(awItem);
        Skin skin = SkinIOUtils.loadSkinFromLibraryFile(file);
        CommonSkinCache.INSTANCE.addEquipmentDataToCache(skin, file);
        SkinIdentifier identifier = new SkinIdentifier(0, file, 0, skin.getSkinType());
        ItemStack itemStack = SkinNBTHelper.makeEquipmentSkinStack(new SkinDescriptor(identifier));
        if(itemStack!=null){
            if(!this.awItems.contains(awItem)){
                this.awItems.add(awItem);
            }
        }
    }
    public void clearAWItems(){
        this.awItems.clear();
    }
    public void addBagItem(OOItem item){
        for(OOItem bagItem:this.bagItems){
            if(bagItem.getItemId().equals(item.getItemId())){
                bagItem.changeQuantity(item.getQuantity());
                return;
            }
        }
        this.bagItems.add(item);
    }
    public void removeBagItem(OOItem item){
        for(int i=0;i<this.bagItems.size();i++){
            if(bagItems.get(i).getItemId().equals(item.getItemId())){
                if(item.getQuantity()==bagItems.get(i).getQuantity()){
                    this.bagItems.remove(bagItems.get(i));
                }
                else{
                    OOItem newItem=new OOItem(item.getItemId(), this.bagItems.get(i).getQuantity()-item.getQuantity());
                    this.bagItems.remove(i);
                    this.bagItems.add(newItem);
                }
                return;
            }
        }
    }
    public void consumeBagItem(OOItem item){
        for(OOItem bagItem: this.bagItems){
            if(bagItem.equals(item)){
                bagItem.changeQuantity(-1*item.getQuantity());
                return;
            }
        }
    }
    public boolean unlockTM(OOTMDummy tm){
        if (this.tms.size() == 0) {
            this.tms.add(tm);
            return true;
        }
        for (int i = 0; i < this.tms.size(); i++) {
            OOTMDummy tmOriginal = this.tms.get(i);
            if (tmOriginal.isHm() && tm.isHm()) {
                if (tm.getTmIndex() < tmOriginal.getTmIndex()) {
                        this.tms.add(i, tm);
                        return true;
                } else if (tm.getTmIndex() == tmOriginal.getTmIndex()) {
                    return false;
                }
            } else if (tmOriginal.isHm()) {
                this.tms.add(i, tm);
            } else if (!tm.isHm()) {
                if (tm.getTmIndex() < tmOriginal.getTmIndex()) {
                    this.tms.add(i, tm);
                    return true;
                } else if (tm.getTmIndex() == tmOriginal.getTmIndex()) {
                    return false;
                }
            }
        }
        this.tms.add(tm);
        return true;
    }

    public void addBerries(OOItem berries){
        for(int i=0;i<this.berries.size();i++){
            if(this.berries.get(i).getItemId().equals(berries.getItemId())){
                this.berries.get(i).changeQuantity(berries.getQuantity());
                return;
            }
        }
        this.berries.add(berries);
    }
    public void removeBerries(OOItem berries){
        for(int i=0;i<this.berries.size();i++){
            if(this.berries.get(i).getItemId().equals(berries.getItemId())){
                if(berries.getQuantity()==this.berries.get(i).getQuantity()){
                    this.berries.remove(i);
                }
                else{
                    OOItem newItem=new OOItem(berries.getItemId(), this.berries.get(i).getQuantity()-berries.getQuantity());
                    this.medicine.remove(i);
                    this.medicine.add(newItem);
                }
                return;
            }
        }
    }

    public void addMedicine(OOItem item){
        for(int i=0;i<this.medicine.size();i++){
            if(this.medicine.get(i).getItemId().equals(item.getItemId())){
                this.medicine.get(i).changeQuantity(item.getQuantity());
                return;
            }
        }
        this.medicine.add(item);
    }
    public void removeMedicine(OOItem medicine){
        for(int i=0;i<this.medicine.size();i++){
            if(this.medicine.get(i).getItemId().equals(medicine.getItemId())){
                if(medicine.getQuantity()==this.medicine.get(i).getQuantity()){
                    this.medicine.remove(i);
                }
                else{
                    OOItem newItem=new OOItem(medicine.getItemId(), this.medicine.get(i).getQuantity()-medicine.getQuantity());
                    this.medicine.remove(i);
                    this.medicine.add(newItem);
                }
                return;
            }
        }
    }

    public void addPokeball(OOItem item){
        for(int i=0;i<this.pokeballs.size();i++){
            if(this.pokeballs.get(i).getItemId().equals(item.getItemId())){
                this.pokeballs.get(i).changeQuantity(item.getQuantity());
                return;
            }
        }
        this.pokeballs.add(item);
    }
    public void removePokeball(OOItem pokeballs){
        for(int i=0;i<this.pokeballs.size();i++){
            if(this.pokeballs.get(i).getItemId().equals(pokeballs.getItemId())){
                if(pokeballs.getQuantity()==this.pokeballs.get(i).getQuantity()){
                    this.pokeballs.remove(i);
                }
                else{
                    OOItem newItem=new OOItem(pokeballs.getItemId(), this.pokeballs.get(i).getQuantity()-pokeballs.getQuantity());
                    this.pokeballs.remove(i);
                    this.pokeballs.add(newItem);
                }
                return;
            }
        }
    }

    //chain methods
    public EnumSpecies getChainSpecies() {
        return this.lastCaughtSpecies;
    }
    public EnumSpecies getLastKOPokemon() {
        return this.lastKOSpecies;
    }
    public int getCaptureChain() {
        return this.captureChain;
    }
    public int getKOChain() { return this.KOChain; }

    public void setCaptureChain(int i) {
        this.captureChain = i;
    }
    public void setKOChain(int i) {
        this.KOChain = i;
    }
    public void setChainSpecies(EnumSpecies species) {
        this.lastCaughtSpecies = species;
    }
    public void setLastKOPokemon(EnumSpecies species) {
        this.lastKOSpecies = species;
    }

    public int increaseCaptureChain(int i) {
        return this.captureChain += i;
    }
    public int increaseKOChain(int i) {
        return this.KOChain += i;
    }

    //Friend list methods
    public List<UUID> getFriendRequests(){return this.friendRequests;}
    public List<UUID> getFriendList(){return this.friendList;}

    public void addFriendRequest(UUID player){
        this.friendRequests.add(player);
    }
    public void acceptFriendRequest(UUID player){
        this.friendRequests.remove(player);
        this.friendList.add(player);
    }
    public void addFriend(UUID player){
        this.friendList.add(player);
    }
    public void denyFriendRequest(UUID player){
        this.friendRequests.remove(player);
    }
    public void removeFriend(UUID player){
        this.friendList.remove(player);
    }

    //Visibility methods
    public boolean getSeesAnyone(){return this.seesAnyone;}
    public boolean getSeesFriends(){return this.seesFriends;}

    public void setSeesAnyone(boolean seesAnyone){this.seesAnyone=seesAnyone;}
    public void setSeesFriends(boolean seesFriends){this.seesFriends=seesFriends;}

    //Chat methods
    public boolean seesGlobalChat(){
        return this.globalChat;
    }
    public boolean canFriendsDm(){
        return this.canFriendsDm;
    }
    public boolean canAnyoneDm(){
        return this.canAnyoneDm;
    }

    public void setGlobalChat(boolean canSee){
        this.globalChat=canSee;
    }
    public void setCanFriendsDm(boolean canFriendsDm){
        this.canFriendsDm=canFriendsDm;
    }
    public void setCanAnyoneDm(boolean canAnyoneDm){
        this.canAnyoneDm=canAnyoneDm;
    }

    //misc
    public String getShopName(){return this.shopName;}
    public List<Pokemon> getWaitToEvolve(){return this.waitToEvolve;}
    public Pokemon getPokemonWaiting(int index){return this.waitToEvolve.get(index);}
    public boolean isEvolvingPokemon(){return this.isEvolvingPokemon;}
    public Instant getLoginTime() {
        return loginTime;
    }
    public int getDialogTicks() { return dialogTicks; }

    public void setShopName(String name){this.shopName=name;}
    public void setEvolvingPokemon(boolean evolvingPokemon){this.isEvolvingPokemon=evolvingPokemon;}
    public void setLoginTime(Instant loginTime) {
        this.loginTime = loginTime;
    }
    public void setDialogTicks(int ticks) { this.dialogTicks = ticks; }

    public void removePokemonWaiting(int index){this.waitToEvolve.remove(index);}
    public void removePokemonWaiting(Pokemon pokemon){this.waitToEvolve.remove(pokemon);}
    public void addPokemonWaiting(Pokemon pokemon){this.waitToEvolve.add(pokemon);}



}
