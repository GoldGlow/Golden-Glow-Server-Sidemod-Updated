package com.goldenglow.common.command;

import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.routes.RouteDebugUtils;
import com.goldenglow.common.util.Reference;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketDisplayObjective;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;

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
            IPlayerData playerData=playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
            if(args[0].equals("on")){
                if(!playerData.getHasRouteDebug()) {
                    playerData.setHasRouteDebug(true);
                    playerMP.getWorldScoreboard().addScoreObjective(RouteDebugUtils.getRouteScoreboardName(playerMP), IScoreCriteria.DUMMY);
                    RouteDebugUtils.updateRouteDisplayName(playerMP);
                    playerMP.getWorldScoreboard().getOrCreateScore(playerMP.getName(), playerMP.getWorldScoreboard().getObjective(RouteDebugUtils.getRouteScoreboardName(playerMP))).setScorePoints(1);
                    playerMP.connection.sendPacket(new SPacketDisplayObjective(1, playerMP.getWorldScoreboard().getObjective(RouteDebugUtils.getRouteScoreboardName(playerMP))));
                }
                else {
                    playerMP.sendMessage(new TextComponentString(Reference.red+"You already have the route debug tool running!"));
                }
            }else if(args[0].equals("off")){
                if(playerData.getHasRouteDebug()) {
                    playerData.setHasRouteDebug(false);
                    playerMP.connection.sendPacket(new SPacketDisplayObjective(1, null));
                    playerMP.getWorldScoreboard().removeObjective(playerMP.getWorldScoreboard().getObjective(RouteDebugUtils.getRouteScoreboardName(playerMP)));
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
