package com.goldenglow.common.routes;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import com.sk89q.worldedit.BlockVector2D;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.forge.ForgeWorldEdit;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.DimensionManager;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RouteManager {

    List<Route> routes = new ArrayList<Route>();

    File dir;

    public void init() {
        dir = new File(Reference.routeDir);
        if(!dir.exists()) {
            if (!dir.getParentFile().exists())
                dir.getParentFile().mkdirs();
            dir.mkdirs();
        }
        else
            this.loadRoutes();
    }

    public void loadRoutes() {
        GGLogger.info("Loading Routes...");
        try {
            for (File f : Objects.requireNonNull(dir.listFiles())) {
                if (f.getName().endsWith(".json")) {
                    loadRoute(f.getName().replace(".json", ""));
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadRoute(String routeName) throws IOException {
        InputStream iStream = new FileInputStream(new File(dir, routeName+".json"));
        JsonObject json = new JsonParser().parse(new InputStreamReader(iStream, StandardCharsets.UTF_8)).getAsJsonObject();
        String name = json.get("Name").getAsString();
        String dName = json.get("DisplayName").getAsString();
        String song = json.get("Song").getAsString();
        int priority = json.get("Priority").getAsInt();

        List<BlockVector2D> points = new ArrayList<>();
        JsonObject regionObj = json.get("region").getAsJsonObject();
        int minY = regionObj.get("minY").getAsInt();
        int maxY = regionObj.get("maxY").getAsInt();
        JsonArray pointsArray = json.get("points").getAsJsonArray();
        for(JsonElement o : pointsArray) {
            int posX = o.getAsJsonObject().get("posX").getAsInt();
            int posZ = o.getAsJsonObject().get("posZ").getAsInt();
            BlockVector2D vec = new BlockVector2D(posX, posZ);
            points.add(vec);
        }
        Route route = new Route(name, song, new Polygonal2DRegion(ForgeWorldEdit.inst.getWorld(DimensionManager.getWorld(0)), points, minY, maxY), priority);
        if(!dName.isEmpty())
            route.displayName = dName;

        this.routes.add(route);
    }

    public List<Route> getRoutes(){
        return this.routes;
    }

    public void saveRoutes() {
        GGLogger.info("Saving Routes...");
        for (Route route : routes) {
            try {
                saveRoute(route);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addRoute(Route route) {
        try {
            saveRoute(route);
            this.routes.add(route);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveRoute(Route route) throws IOException {
        File dir = new File(Reference.routeDir, route.unlocalizedName + ".json");
        if (!dir.exists())
            dir.createNewFile();
        JsonWriter file = new JsonWriter(new FileWriter(dir));
        file.setIndent("\t");
        file.beginObject();
        file.name("Name").value(route.unlocalizedName);
        file.name("DisplayName").value(route.displayName);
        file.name("Song").value(route.song);
        file.name("Priority").value(route.priority);
        file.name("region");
        file.beginObject();
        file.name("minY").value(route.region.getMinimumY());
        file.name("maxY").value(route.region.getMaximumY());
        file.endObject();
        file.name("points");
        file.beginArray();
        for (BlockVector2D vec : route.region.getPoints()) {
            file.beginObject();
            file.name("posX").value(vec.getBlockX());
            file.name("posZ").value(vec.getBlockZ());
            file.endObject();
        }
        file.endArray();
        file.endObject();
        file.close();
    }

    public boolean doesRouteExist(String name) {
        for(Route route : this.routes) {
            if(route.unlocalizedName.equalsIgnoreCase(name))
                return true;
        }
        return false;
    }

    public Route getRoute(String name) {
        for(Route route : routes) {
            if(route.unlocalizedName.equalsIgnoreCase(name))
                return route;
        }
        return null;
    }

    public Route getRoute(EntityPlayer player) {
        return getRoute(player.getPosition());
    }

    public Route getRoute(BlockPos pos) {
        int highestPrio = -1;
        Route r = null;
        for(Route route : routes) {
            if(route.region.contains(new Vector(pos.getX(), pos.getY(), pos.getZ())) && route.priority>highestPrio)
                r = route;
        }
        return r;
    }
}