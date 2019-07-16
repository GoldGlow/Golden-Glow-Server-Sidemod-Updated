package com.goldenglow.common.command;

import com.sk89q.minecraft.util.commands.Console;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.NpcAPI;
import noppes.npcs.api.wrapper.PlayerWrapper;

/**
 * Created by JeanMarc on 7/1/2019.
 */
public class CommandBattleReward extends CommandBase {
    public String getName(){
        return "battlereward";
    }

    public String getUsage(ICommandSender sender){
        return "/battlereward <amount> <player>";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length != 2){
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
        else{
            int amount=Integer.parseInt(args[0]);
            EntityPlayerMP player = getPlayer(server, sender, args[1]);
            PlayerWrapper playerWrapper=new PlayerWrapper(player);
            if(playerWrapper.hasPermission("hard")){
                amount*=1.25;
            }
            NoppesUtilServer.runCommand(sender, sender.getName(), "givemoney "+args[1]+" "+args[0], (EntityPlayerMP)null);
            playerWrapper.sendNotification("Reward", "Obtained $"+args[0], Integer.valueOf(playerWrapper.getMCEntity().getEntityData().getInteger("RouteNotification")));
        }
    }
}
