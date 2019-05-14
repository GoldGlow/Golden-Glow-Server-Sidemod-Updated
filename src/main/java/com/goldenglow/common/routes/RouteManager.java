package com.goldenglow.common.routes;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import jdk.nashorn.internal.parser.JSONParser;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    public static List<Route> routes = new ArrayList<Route>();
    File dir;
    public void init() {
        dir = new File(Reference.configDir, "routes.json");
        if(!dir.exists()) {
            if (!dir.getParentFile().exists())
                dir.getParentFile().mkdirs();
        }
        else
            loadRoutes();
    }

    public void loadRoutes() {
        GGLogger.info("Loading Routes...");
        try {
            InputStream iStream = new FileInputStream(dir);
            JsonArray json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonArray();
            for(int i=0;i<json.size();i++) {
                JsonObject routeObj = json.get(i).getAsJsonObject();
                String name = routeObj.get("Name").getAsString();
                String desc = routeObj.get("Desc").getAsString();
                String display=routeObj.get("Display").getAsString();
                JsonArray pokesObj = routeObj.get("Pokes").getAsJsonArray();
                List<SpawnPokemon> pokeList = new ArrayList<SpawnPokemon>();
                for(int j=0;j<pokesObj.size();j++) {
                    JsonObject poke = pokesObj.get(j).getAsJsonObject();
                    String species= poke.get("name").getAsString();
                    if(EnumSpecies.hasPokemonAnyCase(species)) {
                        SpawnPokemon spawn= new SpawnPokemon();
                        spawn.species=species;
                        spawn.minLvl=poke.get("MinLvl").getAsInt();
                        spawn.maxLvl=poke.get("MaxLvl").getAsInt();
                        pokeList.add(spawn);
                    }
                }
                JsonArray areasObj = routeObj.get("Areas").getAsJsonArray();
                List<Area> areas = new ArrayList<Area>();
                for(int j=0; j<areasObj.size();j++) {
                    JsonObject areaObj = areasObj.get(j).getAsJsonObject();
                    JsonObject pos1Obj = areaObj.get("Pos1").getAsJsonObject();
                    Long xLong = pos1Obj.get("PosX").getAsLong();
                    int x = xLong.intValue();
                    Long yLong = pos1Obj.get("PosY").getAsLong();
                    int y = yLong.intValue();
                    Long zLong = pos1Obj.get("PosZ").getAsLong();
                    int z = zLong.intValue();
                    BlockPos bp1 = new BlockPos(x,y,z);
                    JsonObject pos2Obj = areaObj.get("Pos2").getAsJsonObject();
                    xLong = pos2Obj.get("PosX").getAsLong();
                    x = xLong.intValue();
                    yLong = pos2Obj.get("PosY").getAsLong();
                    y = yLong.intValue();
                    zLong = pos2Obj.get("PosZ").getAsLong();
                    z = zLong.intValue();
                    BlockPos bp2 = new BlockPos(x,y,z);
                    Area area = new Area(bp1, bp2);
                    areas.add(area);
                }

                Route route = new Route(name, display, desc);
                route.pokeList = pokeList;
                route.areas = areas;

                routes.add(route);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Route getRoutes(String name){
        for(Route route: GoldenGlow.routeManager.routes){
            if(route.name.equals(name))
                return route;
        }
        return null;
    }

    public void saveRoutes() {
        GGLogger.info("Saving Routes...");
        try{
            File dir = new File(Reference.configDir, "routes.json");
            if(!dir.exists())
                dir.createNewFile();
            JsonWriter file = new JsonWriter( new FileWriter(dir));
            file.setIndent("\t");
            file.beginArray();
            for(Route route : routes){
                file.beginObject();
                file.name("Name").value(route.name);
                file.name("Desc").value(route.desc);
                file.name("Pokes");
                file.beginArray();
                for(SpawnPokemon pokemon: route.pokeList){
                    file.beginObject();
                    file.name("name").value(pokemon.species);
                    file.name("MinLvl").value(pokemon.minLvl);
                    file.name("MaxLvl").value(pokemon.maxLvl);
                    file.endObject();
                }
                file.endArray();
                file.name("Areas");
                file.beginArray();
                for(Area area: route.areas){
                    file.beginObject();
                    file.name("Pos1");
                    file.beginObject();
                    file.name("PosX").value(area.pos1.getX());
                    file.name("PosY").value(area.pos1.getY());
                    file.name("PosZ").value(area.pos1.getZ());
                    file.endObject();
                    file.name("Pos2");
                    file.beginObject();
                    file.name("PosX").value(area.pos2.getX());
                    file.name("PosY").value(area.pos2.getY());
                    file.name("PosZ").value(area.pos2.getZ());
                    file.endObject();
                    file.endObject();
                }
                file.endArray();
                file.endObject();
            }
            file.endArray();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(World world) {
        for(Route route : routes) {
            route.playerCheck(world);
        }
    }

    public static int hasRoute(int posX, int posY, int posZ){
        for(Route route: routes){
            for(Area area: route.areas){
                if(posX>=area.pos1.getX()&&posX<=area.pos2.getX()){
                    if(posY>=area.pos1.getY()&&posY<=area.pos2.getY()){
                        if(posZ>=area.pos1.getZ()&&posZ<=area.pos2.getZ()){
                            return routes.indexOf(route);
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static boolean routeWithNameExists(String name) {
        for(Route route : routes) {
            if(route.name.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public static Route getRoute(String name) {
        for(Route route : routes) {
            if(route.name.equalsIgnoreCase(name))
                return route;
        }
        return null;
    }

    public static Route getRoute(EntityPlayer player) {
        for(Route route : routes) {
            if(route.playerList.contains(player))
                return route;
        }
        return null;
    }
}