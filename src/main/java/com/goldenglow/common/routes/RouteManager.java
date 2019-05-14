package com.goldenglow.common.routes;

import com.goldenglow.common.util.GGLogger;
import com.goldenglow.common.util.Reference;
import com.sk89q.worldedit.Vector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;

import java.io.File;
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
    }

    public void saveRoutes() {
        GGLogger.info("Saving Routes...");
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