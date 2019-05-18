package com.goldenglow.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.NoppesUtilServer;

/**
 * Created by JeanMarc on 5/11/2019.
 */
public class CommandPhone extends CommandBase {
    public String getName(){
        return "phone";
    }

    public String getUsage(ICommandSender sender){
        return "/phone";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException{
        if(args.length > 0){
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
        else{
            EntityPlayerMP player = getPlayer(server, sender, sender.getName());
            Item item = getItemByText(sender, "variedcommodities:phone");
            ItemStack itemstack = new ItemStack(item);
            player.addItemStackToInventory(itemstack);
        }
    }
}