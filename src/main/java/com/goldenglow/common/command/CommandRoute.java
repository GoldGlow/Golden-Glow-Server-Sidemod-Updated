package com.goldenglow.common.command;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Route;
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
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

import java.util.ArrayList;
import java.util.List;

public class CommandRoute extends CommandBase {

    public String getName() {
        return "route";
    }

    public String getUsage(ICommandSender sender) {
        return "/route [list|name] [song] [priority]";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length>0) {
            if(args.length==1) {
                if(args[0].equalsIgnoreCase("list")) {
                    List<String> names = new ArrayList<>();
                    for(Route r : GoldenGlow.routeManager.getRoutes()) {
                        names.add(r.unlocalizedName);
                    }
                    String list = String.join(", ", names);
                    sender.sendMessage(new TextComponentString("[Routes] Loaded Routes: "+list));
                }
                else if (args[0].equalsIgnoreCase("reload")) {
                    sender.sendMessage(new TextComponentString("[Routes] Reloading Routes..."));
                    GoldenGlow.routeManager.init();
                }
            } else {
                try {
                    LocalSession session = WorldEdit.getInstance().getSessionManager().findByName(sender.getName());
                    Region region = session.getRegionSelector(session.getSelectionWorld()).getRegion();
                    if (region != null && region instanceof Polygonal2DRegion) {
                        Polygonal2DRegion selection = (Polygonal2DRegion) region;
                        Route route;
                        if (args.length > 2 && Integer.parseInt(args[2]) > 0) {
                            route = new Route(args[0], args[1], selection, Integer.parseInt(args[2]));
                        } else if (args.length == 2) {
                            route = new Route(args[0], args[1], selection);
                        } else {
                            route = new Route(args[0], selection);
                        }
                        GoldenGlow.routeManager.addRoute(route);
                        TextComponentString msg = new TextComponentString("New Route '" + route.unlocalizedName + "' created successfully!");
                        msg.getStyle().setColor(TextFormatting.AQUA);
                        sender.sendMessage(msg);
                    } else {
                        TextComponentString msg = new TextComponentString("ERROR: Make sure you make a WorldEdit selection, using poly selection mode. (//sel poly)!");
                        msg.getStyle().setColor(TextFormatting.RED);
                        sender.sendMessage(msg);
                    }
                } catch (IncompleteRegionException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
    }
}
