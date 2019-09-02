package com.goldenglow.common.battles.raids;

import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.battles.attacks.Attack;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumBossMode;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import noppes.npcs.api.wrapper.PlayerWrapper;

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
        PokemonSpec spec = new PokemonSpec("rotom f:0 lvl:60");
        EntityPixelmon poke = spec.create(sender.getEntityWorld());
        poke.setPositionAndUpdate(sender.getPosition().getX(), sender.getPosition().getY(), sender.getPosition().getZ());
        poke.setBoss(EnumBossMode.Legendary);
        sender.getEntityWorld().spawnEntity(poke);
        if(poke!=null) {
            RaidController controller = new RaidController(poke);
//            controller.startRaidBattle((EntityPlayerMP)sender);
            controller.startExperimentalRaid(server.getPlayerList().getPlayers());
        } else {
            sender.sendMessage(new TextComponentString("[ERROR] Something went wrong making the raid... Awkward..."));
        }
    }
}
