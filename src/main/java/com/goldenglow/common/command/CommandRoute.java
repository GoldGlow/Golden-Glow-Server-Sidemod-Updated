package com.goldenglow.common.command;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandRoute extends CommandBase {

    public String getName() {
        return "route";
    }

    public String getUsage(ICommandSender sender) {
        return "/route name [priority]";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length>0) {
            try {
                LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(sender.getName());
                Region region = session.getRegionSelector(session.getSelectionWorld()).getRegion();
                if(region!=null && region instanceof Polygonal2DRegion) {
                    Polygonal2DRegion selection = (Polygonal2DRegion)region;
                    Route route;
                    if(args.length>1 && Integer.parseInt(args[1])>0) {
                         route = new Route(args[0], selection, Integer.parseInt(args[1]));
                    }else {
                        route = new Route(args[0], selection);
                    }
                    GoldenGlow.routeManager.addRoute(route);
                }
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }
        }
    }
}
