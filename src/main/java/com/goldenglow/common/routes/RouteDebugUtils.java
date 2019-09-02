package com.goldenglow.common.routes;

import com.goldenglow.common.data.IPlayerData;
import com.goldenglow.common.data.OOPlayerProvider;
import net.minecraft.entity.player.EntityPlayerMP;

public class RouteDebugUtils {
    public static String getRouteScoreboardName(EntityPlayerMP playerMP){
        String playerName=playerMP.getName();
        if(playerName.length()>13){
            playerName=playerName.substring(0,12);
        }
        return "RD_"+playerName;
    }

    public static String getRouteDebugName(IPlayerData playerData){
        String routeName="null";
        if(playerData.getRoute()!=null){
            if(playerData.getRoute().unlocalizedName.length()>16){
                routeName=playerData.getRoute().unlocalizedName.substring(0, 15);
            }
            else {
                routeName=playerData.getRoute().unlocalizedName;
            }
        }
        return routeName;
    }

    public static void updateRouteDisplayName(EntityPlayerMP playerMP){
        IPlayerData playerData=playerMP.getCapability(OOPlayerProvider.OO_DATA, null);
        if(playerData!=null)
            if(playerData.getHasRouteDebug())
                playerMP.getWorldScoreboard().getObjective(RouteDebugUtils.getRouteScoreboardName(playerMP)).setDisplayName(RouteDebugUtils.getRouteDebugName(playerData));
    }
}
