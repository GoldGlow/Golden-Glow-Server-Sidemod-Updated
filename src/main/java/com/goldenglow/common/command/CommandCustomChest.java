package com.goldenglow.common.command;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.inventory.CustomInventory;
import com.goldenglow.common.inventory.CustomInventoryData;
import io.netty.buffer.Unpooled;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
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
            for(CustomInventoryData inventoryData:GoldenGlow.customInventoryHandler.inventories) {
                if (inventoryData.getName().equals(args[1])) {
                    CustomInventory.openCustomInventory(player, inventoryData);
                    return;
                }
            }
        }
    }
}
