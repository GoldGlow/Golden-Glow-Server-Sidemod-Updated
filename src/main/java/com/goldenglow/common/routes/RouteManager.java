package com.goldenglow.common.routes;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.forge.ForgeWorld;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class RouteManager {

    List<Route> routes = new ArrayList<Route>();

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
                List<BlockVector2D> points=new ArrayList<>();
                JsonObject routeObj = json.get(i).getAsJsonObject();
                String name = routeObj.get("Name").getAsString();
                String song = routeObj.get("Song").getAsString();
                int priority=routeObj.get("Priority").getAsInt();
                JsonObject pos1Obj = routeObj.get("Pos1").getAsJsonObject();
                    int x = pos1Obj.get("PosX").getAsInt();
                    int firstY = pos1Obj.get("PosY").getAsInt();
                    int z = pos1Obj.get("PosZ").getAsInt();
                points.add(new BlockVector2D(x, z));
                JsonObject pos2Obj = routeObj.get("Pos2").getAsJsonObject();
                    x = pos2Obj.get("PosX").getAsInt();
                    int secondY = pos2Obj.get("PosY").getAsInt();
                    z = pos2Obj.get("PosZ").getAsInt();
                points.add(new BlockVector2D(x, z));
                int minY=Math.min(firstY, secondY);
                int maxY=Math.max(firstY, secondY);

                Route route = new Route(name, song, new Polygonal2DRegion(ForgeWorldEdit.inst.getWorld(DimensionManager.getWorld(0)), points, minY, maxY), priority);

                routes.add(route);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                file.name("Name").value(route.routeName);
                file.name("Song").value(route.song);
                file.name("Priority").value(route.priority);
                file.name("Pos1");
                file.beginObject();
                    file.name("PosX").value(route.region.getPoints().get(0).getBlockX());
                    file.name("PosY").value(route.region.getMinimumY());
                    file.name("PosZ").value(route.region.getPoints().get(0).getBlockZ());
                file.endObject();
                file.name("Pos2");
                file.beginObject();
                    file.name("PosX").value(route.region.getPoints().get(1).getBlockX());
                    file.name("PosY").value(route.region.getMaximumY());
                    file.name("PosZ").value(route.region.getPoints().get(1).getBlockZ());
                file.endObject();
            }
            file.endArray();
            file.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addRoute(Route route) {
        this.routes.add(route);
        saveRoute(route);
    }

    public void saveRoute(Route route) {}

    public boolean doesRouteExist(String name) {
        for(Route route : this.routes) {
            if(route.routeName.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public Route getRoute(String name) {
        for(Route route : routes) {
            if(route.routeName.equalsIgnoreCase(name))
                return route;
        }
        return null;
    }

    public Route getRoute(EntityPlayer player) {
        return getRoute(player.getPosition());
    }

    public Route getRoute(BlockPos pos) {
        Route highestPrio = null;
        for(Route route : routes) {
            if(route.region.contains(new Vector(pos.getX(), pos.getY(), pos.getZ())) && (highestPrio==null || route.priority>highestPrio.priority))
                highestPrio = route;
        }
        return highestPrio;
    }
}