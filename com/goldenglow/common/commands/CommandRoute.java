package com.goldenglow.common.commands;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.routes.Area;
import com.goldenglow.common.routes.Route;
import com.goldenglow.common.routes.RouteManager;
import com.goldenglow.common.routes.SpawnPokemon;
import com.goldenglow.common.util.Reference;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.PlayerNotFoundException;
import net.minecraft.command.server.CommandBlockLogic;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;

public class CommandRoute extends CommandBase {

    @Override
    public String getCommandName() {
        return "route";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {
        return "/route <create:remove:list:pokemon>";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    public void processCommand(ICommandSender commandSender, String[] args) throws PlayerNotFoundException {
        if(isAdmin(commandSender)) {
            if(args[0].equalsIgnoreCase("list")) {
                if(GoldenGlow.instance.routeManager.routes.isEmpty()) {
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "No routes."));
                    return;
                }
                StringBuilder list = new StringBuilder();
                for(Route route : GoldenGlow.instance.routeManager.routes) {
                    list.append(route.name+" ["+route.areas.get(0).pos1+", "+route.areas.get(0).pos2+"], ");
                }
                list.delete(list.length() - 2, list.length() - 1);
                commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + list.toString()));
            } else if(args[0].equalsIgnoreCase("create")) {
                if(args.length<3) {
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed+"/route create <name> <subTitle>"));
                    return;
                }
                EntityPlayer player = getPlayer(commandSender, commandSender.getName());
                if (GoldenGlow.instance.tempHandler.playerBlocks.containsKey(player)) {
                    Area area = GoldenGlow.instance.tempHandler.playerBlocks.get(player);
                    if (area==null || area.pos1 == null || area.pos2 == null) {
                        commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "You need to have 2 points set before creating a route."));
                        return;
                    }
                    int x;
                    int otherX;
                    int y;
                    int otherY;
                    int z;
                    int otherZ;
                    if(area.pos1.getX() > area.pos2.getX()) {
                        x = area.pos1.getX();
                        otherX= area.pos2.getX();
                    }
                    else{
                        x=area.pos2.getX();
                        otherX=area.pos1.getX();
                    }
                    if(area.pos1.getY() > area.pos2.getY()) {
                        y = area.pos1.getY();
                        otherY = area.pos2.getY();
                    }
                    else{
                        y=area.pos2.getY();
                        otherY=area.pos1.getY();
                    }
                    if(area.pos1.getZ() > area.pos2.getZ()) {
                        z = area.pos1.getZ();
                        otherZ=area.pos2.getZ();
                    }
                    else{
                        z=area.pos2.getZ();
                        otherZ=area.pos1.getZ();
                    }
                    area.pos1=new BlockPos(otherX, otherY, otherZ);
                    area.pos2=new BlockPos(x,y,z);
                    Route route = new Route(args[1], args[2], area);
                    GoldenGlow.instance.routeManager.routes.add(route);
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + "Route successfully created."));
                }else{
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "You need to have 2 points set before creating a route."));
                }
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(args.length<2) {
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "/route remove <name>"));
                    return;
                }
                if(GoldenGlow.instance.routeManager.routes.contains(args[1])) {
                    GoldenGlow.instance.routeManager.routes.remove(args[1]);
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + "Route successfully removed."));
                    return;
                }
            }
            else if (args[0].equalsIgnoreCase("addPoke")) {
                if(args.length<5) {
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "/route addPoke <route> <PokeName> <minLvl> <maxLvl>"));
                    return;
                }
                if(!RouteManager.routeWithNameExists(args[1])) {
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed + "Route does not exist."));
                    return;
                }
                Route route = RouteManager.getRoute(args[1]);
                String name = args[2];
                if(EnumPokemon.hasPokemonAnyCase(name)) {
                    SpawnPokemon pokemon = new SpawnPokemon();
                    pokemon.species=name;
                    if(isInt(args[3])){
                        int minLvl=Integer.parseInt(args[3]);
                        pokemon.minLvl=minLvl;
                    }
                    if(isInt(args[4])){
                        int maxLvl=Integer.parseInt(args[4]);
                        pokemon.maxLvl=maxLvl;
                    }
                    route.pokeList.add(pokemon);
                }
            }
            else if (args[0].equalsIgnoreCase("pokemon")){
                if(args.length!=2){
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + Reference.colorRed+"/route pokemon <route>"));
                }
                if(RouteManager.getRoute(args[1])!=null){
                    Route route=RouteManager.getRoute(args[1]);
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + "-----"+route.name+"-----"));
                    commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix + "Species        min    max"));
                    for(SpawnPokemon pokemon: route.pokeList){
                        String msg=pokemon.species;
                        if(pokemon.species.length()<15){
                            for(int i=0;i<(15-pokemon.species.length());i++)
                                msg+=" ";
                        }
                        msg+=pokemon.minLvl;
                        if(pokemon.minLvl<10)
                            msg+=" ";
                        msg+=pokemon.maxLvl;
                        commandSender.addChatMessage(new ChatComponentText(Reference.messagePrefix+msg));
                    }
                }
            }
        }
    }

    boolean isInt(String value){
        try {
            Integer.parseInt(value);
            return true;
        }
        catch(NumberFormatException e){
            return false;
        }
    }

    boolean isAdmin(ICommandSender commandSender) throws PlayerNotFoundException {
        if(commandSender instanceof EntityPlayer) {
            EntityPlayer player = getPlayer(commandSender, commandSender.getName());
            if(player.canCommandSenderUseCommand(getRequiredPermissionLevel(), "com.goldenglow.commands.CommandRoute")) {
                return true;
            }
        }
        if(commandSender instanceof CommandBlockLogic)
            return true;
        return false;
    }
}
