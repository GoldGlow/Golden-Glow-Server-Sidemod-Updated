package com.goldenglow.common.teams;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
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
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.DimensionManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TeamManager {
    File teamFile = new File(Reference.configDir, "teams.cfg");
    ArrayList<Team> teams = new ArrayList<Team>();
    public Map<String, String> otherMoves = new HashMap();
    public ArrayList<String> otherPixelmon = new ArrayList<String>();
    
    public TeamManager() {
        otherMoves.put("Soft-Boiled", "Softboiled");
        otherMoves.put("Ancient Power", "AncientPower");
        otherMoves.put("ThunderPunch", "Thunder Punch");
        otherMoves.put("Dynamic Punch", "DynamicPunch");
        otherMoves.put("Bubble Beam", "BubbleBeam");
        otherMoves.put("High Jump Kick", "Hi Jump Kick");
        otherMoves.put("Extreme Speed", "ExtremeSpeed");
        otherMoves.put("Dragon Breath", "DragonBreath");
        otherMoves.put("Grass Whistle", "GrassWhistle");

        otherPixelmon.add("Ralts");
        otherPixelmon.add("Misdreavus");
        otherPixelmon.add("Froslass");
        otherPixelmon.add("Cubchoo");
        otherPixelmon.add("Beartic");
    }
    
    public void init() {
        if(!teamFile.exists())
            try {
                teamFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        try {
            loadTeams();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadTeams()throws IOException{
        BufferedReader reader = new BufferedReader(new FileReader(teamFile));
        String readLine;
        String teamName = "";
        ArrayList<ArrayList<String>> team= new ArrayList<ArrayList<String>>();
        ArrayList<String> pokemon = new ArrayList<String>();
        boolean pokemonSequence=false;
        teams.clear();
        while((readLine=reader.readLine())!=null)
        {
            if(readLine.startsWith("===")){
                if(team.size()>0){
                    for(int i=0;i<team.size();i++){
                        getTeam(teamName).addMember(convertPokemon(team.get(i)));
                    }
                    teams.add(getTeam(teamName));
                    team.clear();
                }
                teamName = readLine.replace("===","");
                teamName = teamName.replace(" ","");
                createTeam(teamName);
                GoldenGlow.instance.logger.teamInfo("Created new Team: " + teamName + "!");
            }
            if(readLine.contains("@")){

                pokemonSequence=true;
                pokemon.add(readLine);
            }
            if(readLine.equals("")){
                team.add(pokemon);
                pokemon.clear();
                pokemonSequence=false;
            }
            if(pokemonSequence){
                pokemon.add(readLine);
            }
        }
        team.add(pokemon);
        for(int i=0;i<team.size();i++){
            getTeam(teamName).addMember(convertPokemon(team.get(i)));
        }
        teams.add(getTeam(teamName));
    }

    public EntityPixelmon convertPokemon(ArrayList<String> pokemon){
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
            pixelmon.gender=Gender.Male;
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
            else if(line.startsWith("Level")){
                int lvl = Integer.parseInt(line.replace("Level: ",""));
                if(lvl<=100&&lvl>0){
                    pixelmon.getLvl().setLevel(lvl);
                    GoldenGlow.instance.logger.info("Set level: "+lvl+" to pokemon: "+pixelmon.getName());
                }
                else
                    GoldenGlow.instance.logger.error("Could not set a pokemons level!");
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
                if(otherMoves.containsKey(move))
                    move=otherMoves.get(move);

                Attack attack = DatabaseMoves.getAttack(move);
                GGLogger.info("Found move: -"+move+"-");
                if(attack!=null)
                        moveset.add(attack);
                else
                    GoldenGlow.instance.logger.error("Move not found: "+move);
            }
        }
        return pixelmon;
    }

    public void createTeam(String name)
    {
        Team team = new Team(name);
        this.teams.add(team);
    }

    public Team getTeam(String name)
    {
        for(Team team : this.teams)
        {
            if(team.name.equalsIgnoreCase(name)){
                return team;
            }
        }
        return null;
    }

    public ArrayList<Team> getTeams()
    {
        return this.teams;
    }
}
