package com.goldenglow.common.battles.raids;

import com.goldenglow.GoldenGlow;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandRaidDebug extends CommandBase {
    @Override
    public String getName() {
        return "raid";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        GoldenGlow.raidHandler.startOrJoinRaid((EntityPlayerMP)sender);
    }
}
