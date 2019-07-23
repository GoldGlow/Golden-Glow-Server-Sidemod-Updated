package com.goldenglow.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class CommandRouteDebug extends CommandBase {
    public String getName(){
        return "routedebug";
    }

    public String getUsage(ICommandSender sender){
        return "/routedebug <on/off>";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length != 1){
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
        else{
            if(args[0].equals("on")){
            }else if(args[0].equals("off")){
            }
            else {
                throw new WrongUsageException(getUsage(sender), new Object[0]);
            }
        }
    }
}
