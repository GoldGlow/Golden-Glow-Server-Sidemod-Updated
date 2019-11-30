package com.goldenglow.common.command;

import com.goldenglow.common.battles.bosses.BossRegistry;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandBoss extends CommandBase {

    public String getName() {
        return "boss";
    }

    public String getUsage(ICommandSender sender) {
        return "/boss [bossName] [player]";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length>=2) {
            String bossName = args[0];
            String playerName = args[1];
            BossRegistry.startBossBattle(server.getPlayerList().getPlayerByUsername(playerName), bossName);
        }
    }
}
