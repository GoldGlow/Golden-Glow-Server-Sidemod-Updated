package com.goldenglow.common.command;

import com.goldenglow.common.inventory.CustomInventory;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

/**
 * Created by JeanMarc on 7/11/2019.
 */
public class CommandCustomChest extends CommandBase{
    public String getName() {
        return "cc";
    }

    public String getUsage(ICommandSender sender) {
        return "/cc <username> <chestname>";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length!=2){
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
        else{
            EntityPlayerMP player = getPlayer(server, sender, args[0]);
            CustomInventory.openInventory(args[1], player);
        }
    }
}
