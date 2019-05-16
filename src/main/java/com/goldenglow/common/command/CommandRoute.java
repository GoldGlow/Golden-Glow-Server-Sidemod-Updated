package com.goldenglow.common.command;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.util.GGLogger;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.regions.Region;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;

public class CommandRoute extends CommandBase {

    public String getName() {
        return "route";
    }

    public String getUsage(ICommandSender sender) {
        return "/route name [song] [priority]";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length>0) {
            try {
                LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(sender.getName());
                Region region = session.getRegionSelector(session.getSelectionWorld()).getRegion();
                if(region!=null && region instanceof Polygonal2DRegion) {
                    Polygonal2DRegion selection = (Polygonal2DRegion)region;
                    Route route;
                    if(args.length>2 && Integer.parseInt(args[2])>0) {
                         route = new Route(args[0], args[1], selection, Integer.parseInt(args[2]));
                    }else if(args.length==2){
                        route = new Route(args[0], args[1], selection);
                    }
                    else {
                        route = new Route(args[0], selection);
                    }
                    GoldenGlow.routeManager.addRoute(route);
                    GGLogger.info("Added route "+args[0]);
                }
            } catch (IncompleteRegionException e) {
                e.printStackTrace();
            }
        }
        else {
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
    }
}
