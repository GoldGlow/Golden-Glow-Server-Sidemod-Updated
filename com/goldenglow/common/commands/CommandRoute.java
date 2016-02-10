package com.goldenglow.common.commands;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.handlers.TempHandler;
import com.goldenglow.common.routes.Area;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.util.BlockPos;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import java.util.List;
import java.util.Map;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CommandRoute
        extends CommandBase
{
    public String func_71517_b()
    {
        return "route";
    }

    public String func_71518_a(ICommandSender p_71518_1_)
    {
        return "/route <create:remove:list>";
    }

    public int func_82362_a()
    {
        return 2;
    }

    public void func_71515_b(ICommandSender commandSender, String[] args)
    {
        if (isAdmin(commandSender)) {
            if (args[0].equalsIgnoreCase("list"))
            {
                if (RouteManager.routes.isEmpty())
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "No routes."));
                    return;
                }
                StringBuilder list = new StringBuilder();
                for (Route route : RouteManager.routes) {
                    list.append(route.name + " [" + ((Area)route.areas.get(0)).pos1 + ", " + ((Area)route.areas.get(0)).pos2 + "], ");
                }
                list.delete(list.length() - 2, list.length() - 1);
                commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + list.toString()));
            }
            else if (args[0].equalsIgnoreCase("create"))
            {
                if (args.length < 3)
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "/route create <name> <subTitle>"));
                    return;
                }
                EntityPlayer player = func_82359_c(commandSender, commandSender.func_70005_c_());
                if (GoldenGlow.instance.tempHandler.playerBlocks.containsKey(player))
                {
                    Area area = (Area)GoldenGlow.instance.tempHandler.playerBlocks.get(player);
                    if ((area == null) || (area.pos1 == null) || (area.pos2 == null))
                    {
                        commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "You need to have 2 points set before creating a route."));
                        return;
                    }
                    if (area.pos1.x > area.pos2.x)
                    {
                        int x = area.pos1.x;
                        area.pos1.x = area.pos2.x;
                        area.pos2.x = x;
                    }
                    if (area.pos1.y > area.pos2.y)
                    {
                        int y = area.pos1.y;
                        area.pos1.y = area.pos2.y;
                        area.pos2.y = y;
                    }
                    if (area.pos1.z > area.pos2.z)
                    {
                        int z = area.pos1.z;
                        area.pos1.z = area.pos2.z;
                        area.pos2.z = z;
                    }
                    Route route = new Route(args[1], args[2], area);
                    if (args.length >= 5)
                    {
                        route.minLvl = Integer.parseInt(args[3]);
                        route.maxLvl = Integer.parseInt(args[4]);
                    }
                    RouteManager.routes.add(route);
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + "Route successfully created."));
                }
                else
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "You need to have 2 points set before creating a route."));
                }
            }
            else if (args[0].equalsIgnoreCase("remove"))
            {
                if (args.length < 2)
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "/route remove <name>"));
                    return;
                }
                if (RouteManager.routes.contains(args[1]))
                {
                    RouteManager.routes.remove(args[1]);
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + "Route successfully removed."));
                }
            }
            else if (args[0].equalsIgnoreCase("addPoke"))
            {
                if (args.length < 3)
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "/route addPoke <route> <PokeName>"));
                    return;
                }
                if (!RouteManager.routeWithNameExists(args[1]))
                {
                    commandSender.func_145747_a(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "Route does not exist."));
                    return;
                }
                Route route = RouteManager.getRoute(args[1]);
                String name = args[2];
                if (EnumPokemon.hasPokemonAnyCase(name)) {
                    route.pokeList.add(name);
                }
            }
        }
    }

    boolean isAdmin(ICommandSender commandSender)
    {
        if ((commandSender instanceof EntityPlayer))
        {
            EntityPlayer player = func_82359_c(commandSender, commandSender.func_70005_c_());
            if ((Bukkit.getPlayer(player.getPersistentID()).hasPermission("com.goldenglow.commands.CommandRoute")) || (Bukkit.getPlayer(player.getPersistentID()).isOp())) {
                return true;
            }
        }
        if ((commandSender instanceof CommandBlockLogic)) {
            return true;
        }
        return false;
    }
}
