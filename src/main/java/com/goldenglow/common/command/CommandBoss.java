package com.goldenglow.common.command;

import com.goldenglow.common.battles.BattleManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class CommandBoss extends CommandBase {

    public String getName() {
        return "boss";
    }

    public String getUsage(ICommandSender sender) {
        return "/boss [bossName] [player]";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        try {
            if (args.length >= 2) {
                String bossName = args[0];
                String playerName = args[1];
                if (args.length == 5) {
                    BattleManager.startBossBattle(server.getPlayerList().getPlayerByUsername(playerName), bossName, Integer.parseInt(args[2]), Integer.parseInt(args[3]), Integer.parseInt(args[4]));
                } else
                    BattleManager.startBossBattle(server.getPlayerList().getPlayerByUsername(playerName), bossName);
            } else {
                sender.sendMessage(new TextComponentString(BattleManager.getBosses()));
            }
        }
        catch (Exception e) {
            sender.sendMessage(new TextComponentString(getUsage(sender)).setStyle(new Style().setColor(TextFormatting.RED)));
        }
    }
}
