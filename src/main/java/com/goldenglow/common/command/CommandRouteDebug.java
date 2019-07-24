package com.goldenglow.common.command;

import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.util.Reference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import noppes.npcs.NoppesUtilServer;
import noppes.npcs.api.wrapper.PlayerWrapper;

public class CommandRouteDebug extends CommandBase {
    public String getName(){
        return "routedebug";
    }

    public String getUsage(ICommandSender sender){
        return "/routedebug <on/off>";
    }

    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        if(args.length != 1){
            throw new WrongUsageException(getUsage(sender), new Object[0]);
        }
        else{
            EntityPlayerMP playerMP=getPlayer(server, sender, sender.getName());
            IPlayerData playerData=getPlayer(server, sender, sender.getName()).getCapability(OOPlayerProvider.OO_DATA, null);
            if(args[0].equals("on")){
                if(!playerData.getHasRouteDebug()) {
                    playerData.setHasRouteDebug(true);
                    playerMP.getWorldScoreboard().addScoreObjective("RD_"+playerMP.getName(), IScoreCriteria.DUMMY);
                    if(playerData.getRoute()==null){
                        playerMP.getWorldScoreboard().getObjective("RD_"+playerMP.getName()).setDisplayName("null");
                    }
                    else{
                        playerMP.getWorldScoreboard().getObjective("RD_"+playerMP.getName()).setDisplayName(playerData.getRoute().unlocalizedName.substring(0, 15));
                    }
                    playerMP.connection.sendPacket(new SPacketDisplayObjective(1, playerMP.getWorldScoreboard().getObjective("RouteDebug"+playerMP.getName())));
                }
                else {
                    playerMP.sendMessage(new TextComponentString(Reference.red+"You already have the route debug tool running!"));
                }
            }else if(args[0].equals("off")){
                if(playerData.getHasRouteDebug()) {
                    playerData.setHasRouteDebug(false);
                    playerMP.connection.sendPacket(new SPacketDisplayObjective(1, null));
                    playerMP.getWorldScoreboard().removeObjective(playerMP.getWorldScoreboard().getObjective("RD_"+playerMP.getName()));
                }
                else {
                    playerMP.sendMessage(new TextComponentString(Reference.red+"You already have the route debug tool stopped!"));
                }
            }
            else {
                throw new WrongUsageException(getUsage(sender), new Object[0]);
            }
        }
    }
}
