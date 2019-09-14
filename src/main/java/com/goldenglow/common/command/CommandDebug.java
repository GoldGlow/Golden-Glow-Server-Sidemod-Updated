package com.goldenglow.common.command;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.api.overlay.notice.EnumOverlayLayout;
import com.pixelmonmod.pixelmon.client.gui.custom.overlays.ScoreboardLocation;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomNoticePacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomScoreboardDisplayPacket;
import com.pixelmonmod.pixelmon.comm.packetHandlers.customOverlays.CustomScoreboardUpdatePacket;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;

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
        //Pixelmon Custom Scoreboard Overlay
        ArrayList<String> lines = new ArrayList<>();
        lines.add("[ ]");
        lines.add("");
        ArrayList<String> scores = new ArrayList<>();
        scores.add("This is a test for a bit of");
        scores.add("Score2");
        Pixelmon.network.sendTo(new CustomScoreboardUpdatePacket("Quest Title", lines, scores), (EntityPlayerMP)sender);
        Pixelmon.network.sendTo(new CustomScoreboardDisplayPacket(ScoreboardLocation.RIGHT_MIDDLE, true), (EntityPlayerMP)sender);

        //Pixelmon Custom Text + Sprite Overlay
//        Pixelmon.network.sendTo(new CustomNoticePacket().setEnabled(false), (EntityPlayerMP)sender);
//        Pixelmon.network.sendTo(new CustomNoticePacket().setEnabled(true).setLines(new String[] {"§0§lThis is a test for potential dialgue!","§0§lLet's see how this looks..."}).setPokemonSprite(PokemonSpec.from("rotom"), EnumOverlayLayout.LEFT), (EntityPlayerMP)sender);
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("id", "minecraft:skull");
        nbt.setShort("Damage", (short)3);
        Pixelmon.network.sendTo(new CustomNoticePacket().setEnabled(true).setLines(new String[] {"§0§lThis is a test for potential dialgue!","§0§lLet's see how this looks..."}).setItemStack(new ItemStack(nbt), EnumOverlayLayout.LEFT), (EntityPlayerMP)sender);
    }
}
