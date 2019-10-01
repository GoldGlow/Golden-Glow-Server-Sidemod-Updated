package com.goldenglow.common.util.scripting;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import com.goldenglow.common.routes.Route;
import net.minecraft.command.ICommandManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import noppes.npcs.api.entity.data.IData;
import noppes.npcs.api.wrapper.BlockScriptedWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.api.wrapper.WorldWrapper;

public class WorldFunctions {
    private static long lastDailyRefresh = 0;
    public static void warpToSafeZone(PlayerWrapper playerWrapper){
        Route safeZone= GoldenGlow.routeManager.getRoute(playerWrapper.getMCEntity().getEntityData().getString("safeZone"));
        safeZone.warp((EntityPlayerMP)playerWrapper.getMCEntity());
        playerWrapper.message("You whited out!");
        ICommandManager icommandmanager = playerWrapper.getMCEntity().getEntityWorld().getMinecraftServer().getCommandManager();
        icommandmanager.executeCommand(new RConConsoleSource(playerWrapper.getMCEntity().getEntityWorld().getMinecraftServer()), "pokeheal "+playerWrapper.getName());
    }

    public static void checkRoute(EntityPlayerMP playerMP, int lastPosX, int lastPosY, int lastPosZ) {
        IPlayerData playerData = playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
        Route currentRoute = null;
        Route actualRoute = GoldenGlow.routeManager.getRoute(playerMP);

        if(playerData.hasRoute())
            currentRoute = playerData.getRoute();

        if(actualRoute!=null) {
            //If entering Different Route
            if(currentRoute==null || !currentRoute.unlocalizedName.equalsIgnoreCase(actualRoute.unlocalizedName)) {

                //Check if player is restricted from entering
                if (!actualRoute.canPlayerEnter(playerMP)) {
                    playerMP.setPositionAndUpdate(lastPosX, lastPosY, lastPosZ);
                    playerMP.sendMessage(actualRoute.getRequirementMessage(playerMP));
                }
                //If player is allowed to enter
                else {
                    //Clear old route if it exists
                    if (currentRoute != null)
                        currentRoute.removePlayer(playerMP);

                    actualRoute.addPlayer(playerMP); //Add player to new route

                    //If route has a safeZone, set player's last safeZone to this Route's safeZone
                    if (actualRoute.isSafeZone)
                        playerData.setSafezone(actualRoute.unlocalizedName);
                    if(playerData.getHasRouteDebug())
                        playerMP.getWorldScoreboard().getObjective("RD_"+playerMP.getName()).setDisplayName(playerData.getRoute().unlocalizedName == null ? "Null" : playerData.getRoute().unlocalizedName);
                }
            }
        }
        else {
            if(playerData.getHasRouteDebug())
                playerMP.getWorldScoreboard().getObjective("RD_"+playerMP.getName()).setDisplayName("null");
	        /* Ensure players are always in a route. (Stop players leaving the map)
            playerMP.setPositionAndUpdate(lastPosX, lastPosY, lastPosZ);
            playerMP.sendMessage(new TextComponentString("You can't go this way!").setStyle(new Style().setBold(true)));
	         */
        }
    }

    public static int getCurrentDay(WorldWrapper world) {
        return (int)(world.getTime() / 24000L % 2147483647L);
    }

    public static boolean newRLDay(WorldWrapper world) {
        IData worldData = world.getStoreddata();
        if(lastDailyRefresh==0) {
            if(worldData.has("dailyRefresh")) {
                lastDailyRefresh = (long)worldData.get("dailyRefresh");
            }
            else {
                lastDailyRefresh = System.currentTimeMillis();
                worldData.put("dailyRefresh", lastDailyRefresh);
            }
        }
        if(System.currentTimeMillis() - lastDailyRefresh >= 86400000L) {
            lastDailyRefresh = System.currentTimeMillis();
            worldData.put("dailyRefresh", lastDailyRefresh);
            return true;
        }
        return false;
    }

    public static boolean hasWaitedForDay(PlayerWrapper player, BlockScriptedWrapper scriptedBlock){
        if(scriptedBlock.getMCTileEntity().getTileData().hasKey(player.getUUID())) {
            if (scriptedBlock.getMCTileEntity().getTileData().getLong(player.getUUID()) == getCurrentDay((WorldWrapper)player.getWorld())){
                return false;
            }
        }
        scriptedBlock.getMCTileEntity().getTileData().setLong(player.getUUID(), getCurrentDay((WorldWrapper)player.getWorld()));
        return true;
    }
}
