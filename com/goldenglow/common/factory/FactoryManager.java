package com.goldenglow.common.factory;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.config.PixelmonItemsHeld;
import com.pixelmonmod.pixelmon.database.DatabaseMoves;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.EVsStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Gender;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.IVStore;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.Moveset;
import com.pixelmonmod.pixelmon.enums.EnumNature;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by JeanMarc on 6/1/2016.
 */
public class FactoryManager {
    ArrayList<Integer> party= new ArrayList<Integer>();
    public ArrayList<int[]> playerParties= new ArrayList<int[]>();
    public ArrayList<int[]> playerChoices= new ArrayList<int[]>();
    List<EntityPixelmon> pokemonListFirstTier=new ArrayList<EntityPixelmon>();
    List<EntityPixelmon> pokemonListSecondTier = new ArrayList<EntityPixelmon>();
    File firstTier = new File(Reference.configDir, "firstTier.cfg");
    File factoryParty=new File(Reference.configDir,"factoryParty.json");

    public FactoryManager(){
        this.pokemonListFirstTier=new ArrayList<EntityPixelmon>();
        this.pokemonListSecondTier=new ArrayList<EntityPixelmon>();
    }

    void addPokemonFirstTier(EntityPixelmon pixelmon){
        pokemonListFirstTier.add(pixelmon);
    }

    void addPokemonSecondTier(EntityPixelmon pixelmon){
        pokemonListSecondTier.add(pixelmon);
    }

    public EntityPixelmon getPokemonFirstTier (int index){
        return pokemonListFirstTier.get(index);
    }

    public EntityPixelmon getPokemonSecondTier (int index){
        return pokemonListSecondTier.get(index);
    }

    public void init(){
        if(!firstTier.exists())
            try {
                firstTier.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            loadFirstTier();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!factoryParty.exists())
            try {
                factoryParty.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        loadArrays();
    }

    public void loadArrays(){
        try{
            InputStream istream=new FileInputStream(factoryParty);
            JsonArray json = new JsonParser().parse(new InputStreamReader(istream, StandardCharsets.UTF_8)).getAsJsonArray();
            for(int i=0;i<json.size();i++){
                JsonArray array=json.get(i).getAsJsonArray();
                int[] arr=new int[3];
                for(int j=0;j<3;j++){
                    arr[j]=array.get(j).getAsInt();
                }
                int[] choice={0, 0, 0};
                playerChoices.add(choice);
                playerParties.add(arr);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void saveArrays(){
        GGLogger.info("Saving Factory data...");
        try {
            JsonWriter file= new JsonWriter(new FileWriter(factoryParty));
            file.beginArray();
            for(int[] user: playerParties){
                file.beginArray();
                for(int val: user)
                    file.value(val);
                file.endArray();
            }
            file.endArray();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadParty(int id){
        int[] party={-1, -1, -1, -1, -1, -1};
        int[] chosen={-1, -1, -1};
        for(int i=0;i<6;i++){
            do{
                int random=(int)(Math.random()*pokemonListFirstTier.size());
                party[i]=random;
            }while(!this.speciesClause(party, i));
        }
        if(playerChoices.size()>(id)){
            playerChoices.remove(id);
            playerParties.remove(id);
        }
        playerParties.add(id, chosen);
        playerChoices.add(id, party);
    }

    public boolean speciesClause(int[] party, int index){
        for(int i=0;i<index;i++) {
            EntityPixelmon tested = pokemonListFirstTier.get(party[index]);
            EntityPixelmon testing = pokemonListFirstTier.get(party[i]);
            if(tested.getName().equals(testing.getName()))
                return false;
        }
        return true;
    }

    public  void loadFirstTier() throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(firstTier));
        String readLine;
        String teamName = "";
        ArrayList<String> pokemon = new ArrayList<String>();
        boolean pokemonSequence = false;
        while ((readLine = reader.readLine()) != null) {
            if (readLine.contains("@")) {
                pokemonSequence = true;
                pokemon.add(readLine);
            }
            if (readLine.equals("")) {
                pokemonListFirstTier.add(convertPokemon(pokemon));
                pokemon.clear();
                pokemonSequence = false;
            }
            if (pokemonSequence) {
                pokemon.add(readLine);
            }
        }
        if(pokemon.size()>0)
            pokemonListFirstTier.add(convertPokemon(pokemon));
    }

    EntityPixelmon convertPokemon(ArrayList<String> pokemon){
        String line=pokemon.get(0);
        String name=line.split(" @ ")[0];
        String genderString="";
        if(line.contains("(F)")){
            name=name.replace(" (F)","");
            genderString="f";
        }
        else if(line.contains("(M)")){
            name=name.replace(" (M)","");
            GGLogger.info(name);
            genderString="m";
        }
        GGLogger.info("Creating Pokemon -"+name+"-");
        EntityPixelmon pixelmon=(EntityPixelmon) PixelmonEntityList.createEntityByName(name, DimensionManager.getWorld(0));
        if(genderString.equals("m")){
            pixelmon.gender= Gender.Male;
        }
        else if(genderString.equals("f"))
            pixelmon.gender=Gender.Female;
        if((PixelmonItemsHeld.getHeldItem(line.split(" @ ")[1]))!=null)
        {
            pixelmon.setHeldItem(new ItemStack(PixelmonItemsHeld.getHeldItem(line.split(" @ ")[1])));
        }else{
            GoldenGlow.instance.logger.error("HeldItem not found: '" + line.split(" @ ")[1] + "' on Pokemon: " + name);
        }
        Moveset moveset = new Moveset();
        for(int i=1;i<pokemon.size();i++){
            line=pokemon.get(i);
            line=line.replace("  ","");
            if(line.startsWith("Ability:"))
            {
                pixelmon.setAbility(line.replace("Ability: ",""));
            }
            else if(line.startsWith("Shiny: ")&&line.contains("Yes"))
            {
                pixelmon.setIsShiny(true);
                GoldenGlow.instance.logger.teamInfo("Made pokemon: "+pixelmon.getName()+" a shiny!");
            }
            else if(line.startsWith("Happiness:")){
                int happiness = Integer.parseInt(line.replace("Happiness: ",""));
                if(happiness<=255&&happiness>=0){
                    pixelmon.friendship.setFriendship(happiness);
                }
            }
            else if(line.startsWith("EVs")){
                line = line.replace("EVs: ","");
                EVsStore evStore = pixelmon.stats.EVs;
                for(String evs : line.split(" / ")) {
                    int ev = Integer.parseInt(evs.split(" ")[0]);
                    if (evs.split(" ")[1].equalsIgnoreCase("hp") && ev <= 255 && ev > 0)
                        evStore.HP = ev;
                    if (evs.split(" ")[1].equalsIgnoreCase("atk") && ev <= 255 && ev > 0)
                        evStore.Attack = ev;
                    if (evs.split(" ")[1].equalsIgnoreCase("def") && ev <= 255 && ev > 0)
                        evStore.Defence = ev;
                    if (evs.split(" ")[1].equalsIgnoreCase("spa") && ev <= 255 && ev > 0)
                        evStore.SpecialAttack = ev;
                    if (evs.split(" ")[1].equalsIgnoreCase("spd") && ev <= 255 && ev > 0)
                        evStore.SpecialDefence = ev;
                    if (evs.split(" ")[1].equalsIgnoreCase("spe") && ev <= 255 && ev > 0)
                        evStore.Speed = ev;
                    GoldenGlow.instance.logger.teamInfo("Added EV: '" + evs.split(" ")[1] + "-" + ev + "' to pokemon: " + pixelmon.getName());
                }
            }
            else if(line.startsWith("IVs:"))
            {
                line = line.replace("IVs: ","");
                IVStore ivStore = pixelmon.stats.IVs;
                for(String ivs : line.split(" / "))
                {
                    int iv = Integer.parseInt(ivs.split(" ")[0]);
                    if(ivs.split(" ")[1].equalsIgnoreCase("hp")&&iv<=31&&iv>0)
                        ivStore.HP=iv;
                    if(ivs.split(" ")[1].equalsIgnoreCase("atk")&&iv<=31&&iv>0)
                        ivStore.Attack=iv;
                    if(ivs.split(" ")[1].equalsIgnoreCase("def")&&iv<=31&&iv>0)
                        ivStore.Defence=iv;
                    if(ivs.split(" ")[1].equalsIgnoreCase("spa")&&iv<=31&&iv>0)
                        ivStore.SpAtt=iv;
                    if(ivs.split(" ")[1].equalsIgnoreCase("spd")&&iv<=31&&iv>0)
                        ivStore.SpDef=iv;
                    if(ivs.split(" ")[1].equalsIgnoreCase("spe")&&iv<=31&&iv>0)
                        ivStore.Speed=iv;
                    GoldenGlow.instance.logger.teamInfo("Added IV: '"+ivs.split(" ")[1]+"-"+iv+"' to pokemon: "+pixelmon.getName());
                }
                pixelmon.stats.IVs = ivStore;
            }
            else if(line.contains(" Nature")&&!line.contains("Nature Power"))
            {
                line = line.replace(" Nature","");
                if(EnumNature.hasNature(line))
                {
                    pixelmon.setNature(EnumNature.natureFromString(line));
                    GoldenGlow.instance.logger.teamInfo("Added Nature: '"+line+"' to pokemon: "+pixelmon.getName());
                }
            }
            else if(line.startsWith("- ")&&moveset.size()>4){
                String move=line.replace("- ","");
                if(GoldenGlow.instance.teamManager.otherMoves.containsKey(move))
                    move=GoldenGlow.instance.teamManager.otherMoves.get(move);

                Attack attack = DatabaseMoves.getAttack(move);
                GGLogger.info("Found move: -"+move+"-");
                if(attack!=null)
                    moveset.add(attack);
                else
                    GoldenGlow.instance.logger.error("Move not found: "+move);
            }
        }
        pixelmon.getLvl().setLevel(100);
        return pixelmon;
    }
}
