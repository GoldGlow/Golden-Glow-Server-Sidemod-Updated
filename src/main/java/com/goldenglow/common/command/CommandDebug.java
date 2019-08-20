package com.goldenglow.common.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.effect.particle.ParticleEffect;
import org.spongepowered.api.effect.particle.ParticleTypes;
import org.spongepowered.api.entity.living.player.Player;

public class CommandDebug extends CommandBase {
    @Override
    public String getName() {
        return "rvdebug";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        ParticleEffect test = ParticleEffect.builder()
                .type(ParticleTypes.FLAME)
                .quantity(1)
                .build();
        Player player = Sponge.getServer().getPlayer(sender.getName()).get();
        player.spawnParticles(test, player.getPosition().add(1,1,1));
    }
}
