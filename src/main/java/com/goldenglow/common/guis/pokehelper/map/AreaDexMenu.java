package com.goldenglow.common.guis.pokehelper.map;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.OptionTypeManager;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.UnlockableOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.music.ThemeType;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.social.OptionTitle;
import com.goldenglow.common.music.SongManager;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.SpawnPokemon;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.gui.EssentialsButton;
import com.pixelmonessentials.common.api.gui.EssentialsGuis;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.gui.CustomGuiWrapper;
import noppes.npcs.controllers.CustomGuiController;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class AreaDexMenu implements EssentialsGuis {
    private static final int id=6009;
    private ArrayList<EssentialsButton> buttons=new ArrayList<EssentialsButton>();
    private Route route;
    private String[] items=new String[0];
    private int index=-1;

    public AreaDexMenu(){
    }

    public int getId(){
        return this.id;
    }

    public ArrayList<EssentialsButton> getButtons(){
        return this.buttons;
    }

    public void addButton(EssentialsButton button) {
        this.buttons.add(button);
    }

    public void setIndex(int index){
        this.index=index;
    }

    public void setIndex(String index){
        for(int i=0;i<this.items.length;i++){
            if(items[i].equals(index)){
                this.index=i;
                return;
            }
        }
    }

    public int getIndex(){
        return this.index;
    }

    public Route getRoute(){
        return this.route;
    }

    public void setRoute(Route route){
        this.route=route;
    }

    public String[] getItems(){
        return this.items;
    }

    public void init(EntityPlayerMP player, EntityNPCInterface npc){
        PlayerWrapper playerWrapper = new PlayerWrapper(player);
        CustomGuiWrapper gui = new CustomGuiWrapper(id, 256, 256, false);
        this.items=this.getFullSpawnsList(player);
        gui.setBackgroundTexture("obscureobsidian:textures/gui/black_square.png");
        gui.addLabel(201, "Area Dex - "+this.route.displayName, 50, 0, 200, 20);
        gui.addScroll(300, 128, 30, 118, 216, this.items);
        gui.addTexturedRect(100, "obscureobsidian:textures/gui/grey_square.png", 20, 30, 90, 98);
        playerWrapper.showCustomGui(gui);
        PixelmonEssentials.essentialsGuisHandler.addOrReplaceGui(player, this);
    }

    public void updateScroll(EntityPlayerMP player){
        PlayerWrapper playerWrapper=new PlayerWrapper(player);
        CustomGuiWrapper gui= CustomGuiController.getOpenGui(player);
        gui.removeComponent(202);
        gui.removeComponent(203);
        gui.removeComponent(204);
        gui.removeComponent(300);
        if(this.index==-1){
            gui.addScroll(300, 128, 30, 118, 216, this.items);
            gui.update(playerWrapper);
        }
        else{
            String speciesName=this.getItems()[this.index];
            gui.addLabel(202, speciesName, 26, 83, 118, 20);
            if(!speciesName.equals("???")){
                gui.addTexturedRect(101, "pixelmon:textures/sprites/pokemon/"+EnumSpecies.getFromName(speciesName).get().getNationalPokedexNumber()+".png", 30, 20, 256, 256).setScale(0.25F);
                EnumSpawnMethod spawnMethod=this.getMethodFromName(speciesName);
                String methodName=this.getNameFromEnum(spawnMethod);
                gui.addLabel(203, methodName, 26, 95, 118, 20);
                gui.addLabel(204, this.getLevelRange(speciesName, spawnMethod), 26, 107, 118, 20);
            }
            gui.addScroll(300, 128, 30, 118, 216, this.items).setDefaultSelection(this.index);
            gui.update(playerWrapper);
        }
    }

    public String getLevelRange(String species, EnumSpawnMethod spawnMethod){
        SpawnPokemon spawn=null;
        switch (spawnMethod){
            case FISHING:
                for(SpawnPokemon pokemon:this.route.fishingPokemon.getOldRodSpawns()){
                    if(pokemon.species.equalsIgnoreCase(species)){
                        spawn=pokemon;
                        break;
                    }
                }
                if(spawn!=null){
                    break;
                }
                for(SpawnPokemon pokemon:this.route.fishingPokemon.getGoodRodSpawns()){
                    if(pokemon.species.equalsIgnoreCase(species)){
                        spawn=pokemon;
                        break;
                    }
                }
                if(spawn!=null){
                    break;
                }
                for(SpawnPokemon pokemon:this.route.fishingPokemon.getSuperRodSpawns()){
                    if(pokemon.species.equalsIgnoreCase(species)){
                        spawn=pokemon;
                        break;
                    }
                }
            case SPECIAL:
                for(SpawnPokemon pokemon:this.route.specialSpawnList){
                    if(pokemon.species.equalsIgnoreCase(species)){
                        spawn=pokemon;
                        break;
                    }
                }
                break;
            case APRICORN:
                for(SpawnPokemon pokemon:this.route.apricornPokemon){
                    if(pokemon.species.equalsIgnoreCase(species)){
                        spawn=pokemon;
                        break;
                    }
                }
                break;
            case STANDARD:
                for(SpawnPokemon pokemon:this.route.spawnList){
                    if(pokemon.species.equalsIgnoreCase(species)){
                        spawn=pokemon;
                        break;
                    }
                }
                break;
            default:
                break;
        }
        if(spawn!=null){
            if(spawn.minLvl==spawn.maxLvl){
                return "level "+spawn.minLvl;
            }
            else {
                return "levels "+spawn.minLvl+"-"+spawn.maxLvl;
            }
        }
        return "";
    }

    public String getNameFromEnum(EnumSpawnMethod spawnMethod){
        switch (spawnMethod){
            case FISHING:
                return "Fishing encounter";
            case SPECIAL:
                return "Special encounter";
            case APRICORN:
                return "Apricorn encounter";
            case STANDARD:
                return "Normal encounter";
            default:
                return "???";
        }
    }

    public String[] getFullSpawnsList(EntityPlayerMP playerMP){
        ArrayList<Integer> spawnsNDex=new ArrayList<Integer>();
        for(SpawnPokemon pokemon:this.route.spawnList){
            spawnsNDex.add(EnumSpecies.getPokedexNumber(pokemon.species));
        }
        for(SpawnPokemon pokemon:this.route.apricornPokemon){
            spawnsNDex.add(EnumSpecies.getPokedexNumber(pokemon.species));
        }
        for(SpawnPokemon pokemon:this.route.specialSpawnList){
            spawnsNDex.add(EnumSpecies.getPokedexNumber(pokemon.species));
        }
        for(SpawnPokemon pokemon:this.route.fishingPokemon.getOldRodSpawns()){
            spawnsNDex.add(EnumSpecies.getPokedexNumber(pokemon.species));
        }
        for(SpawnPokemon pokemon:this.route.fishingPokemon.getGoodRodSpawns()){
            spawnsNDex.add(EnumSpecies.getPokedexNumber(pokemon.species));
        }
        for(SpawnPokemon pokemon:this.route.fishingPokemon.getSuperRodSpawns()){
            spawnsNDex.add(EnumSpecies.getPokedexNumber(pokemon.species));
        }
        Collections.sort(spawnsNDex);
        String[] species=new String[spawnsNDex.size()];
        for(int i=0;i<species.length;i++){
             EnumPokedexRegisterStatus dexStatus=Pixelmon.storageManager.getParty(playerMP).pokedex.get(spawnsNDex.get(i));
             if(dexStatus==EnumPokedexRegisterStatus.unknown){
                 species[i]="???";
             }
             else{
                 species[i]=EnumSpecies.getFromDex(spawnsNDex.get(i)).name;
             }
        }
        return species;
    }

    public EnumSpawnMethod getMethodFromName(String name){
        if(name.equals("???")){
            return EnumSpawnMethod.NONE;
        }
        for(SpawnPokemon pokemon:this.route.spawnList){
            if(pokemon.species.equalsIgnoreCase(name)){
                return EnumSpawnMethod.STANDARD;
            }
        }
        for(SpawnPokemon pokemon:this.route.apricornPokemon){
            if(pokemon.species.equalsIgnoreCase(name)){
                return EnumSpawnMethod.APRICORN;
            }
        }
        for(SpawnPokemon pokemon:this.route.specialSpawnList){
            if(pokemon.species.equalsIgnoreCase(name)){
                return EnumSpawnMethod.SPECIAL;
            }
        }
        for(SpawnPokemon pokemon:this.route.fishingPokemon.getOldRodSpawns()){
            if(pokemon.species.equalsIgnoreCase(name)){
                return EnumSpawnMethod.FISHING;
            }
        }
        for(SpawnPokemon pokemon:this.route.fishingPokemon.getGoodRodSpawns()){
            if(pokemon.species.equalsIgnoreCase(name)){
                return EnumSpawnMethod.FISHING;
            }
        }
        for(SpawnPokemon pokemon:this.route.fishingPokemon.getSuperRodSpawns()){
            if(pokemon.species.equalsIgnoreCase(name)){
                return EnumSpawnMethod.FISHING;
            }
        }
        return EnumSpawnMethod.NONE;
    }

    public enum EnumSpawnMethod{
        STANDARD,
        SPECIAL,
        APRICORN,
        FISHING,
        NONE
    }
}
