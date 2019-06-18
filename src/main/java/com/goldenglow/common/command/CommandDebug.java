package com.goldenglow.common.command;

import com.goldenglow.GoldenGlow;
import io.netty.buffer.Unpooled;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.SPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import noppes.npcs.CustomNpcs;

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
        PacketBuffer packetbuffer = new PacketBuffer(Unpooled.buffer());
        packetbuffer.writeString("Example");
        ((EntityPlayerMP)sender).connection.sendPacket(new SPacketCustomPayload("goldenglow", packetbuffer));
    }
}
