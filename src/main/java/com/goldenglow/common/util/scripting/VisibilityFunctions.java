package com.goldenglow.common.util.scripting;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.data.player.IPlayerData;
import com.goldenglow.common.data.player.OOPlayerProvider;
import com.goldenglow.common.util.GGLogger;
import com.pixelmonessentials.PixelmonEssentials;
import com.pixelmonessentials.common.api.requirement.RequirementData;
import jdk.nashorn.api.scripting.ScriptObjectMirror;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketSpawnMob;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.wrapper.NPCWrapper;
import noppes.npcs.api.wrapper.PlayerWrapper;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.entity.EntityCustomNpc;

import javax.script.ScriptEngine;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class VisibilityFunctions {
    public static void refreshPlayerVisibility(PlayerWrapper playerWrapper){
        EntityPlayerMP player=(EntityPlayerMP) playerWrapper.getMCEntity();
        IPlayerData playerData=player.getCapability(OOPlayerProvider.OO_DATA, null);
        IEntity[] players=playerWrapper.getWorld().getNearbyEntities(playerWrapper.getPos(), 512, 1);
        for(IEntity otherPlayer:players){
            EntityPlayerMP otherPlayerMP=(EntityPlayerMP)otherPlayer.getMCEntity();
            if(!canPlayerSeeOtherPlayer(player, otherPlayerMP)){
                if(!player.equals(otherPlayerMP))
                    player.removeEntity(otherPlayerMP);
            }
            else if(!player.equals(otherPlayerMP)) {
                player.removeEntity(otherPlayerMP);
                player.connection.sendPacket(new SPacketSpawnPlayer(otherPlayerMP));
            }
        }
    }

    public static boolean canPlayerSeeOtherPlayer(EntityPlayerMP seeingPlayer, EntityPlayerMP targettedPlayer){
        IPlayerData playerData=seeingPlayer.getCapability(OOPlayerProvider.OO_DATA, null);
        if(playerData.getSeesAnyone()){
            return true;
        }
        else if(playerData.getSeesFriends()&&playerData.getFriendList().contains(targettedPlayer.getUniqueID())){
            return true;
        }
        else if(GoldenGlow.permissionUtils.checkPermission(targettedPlayer, "group.staff")){
            return true;
        }
        return false;
    }

    public static boolean canPlayerDmOtherPlayer(EntityPlayerMP sendingPlayer, EntityPlayerMP targettedPlayer){
        if(GoldenGlow.permissionUtils.checkPermission(sendingPlayer, "group.staff")||GoldenGlow.permissionUtils.checkPermission(targettedPlayer, "group.staff")){
            return true;
        }
        IPlayerData playerData=sendingPlayer.getCapability(OOPlayerProvider.OO_DATA, null);
        IPlayerData otherPlayerData=targettedPlayer.getCapability(OOPlayerProvider.OO_DATA, null);
        if(playerData.canAnyoneDm()&&otherPlayerData.canAnyoneDm()){
            return true;
        }
        else if((playerData.canFriendsDm()&&otherPlayerData.canFriendsDm())&&(playerData.getFriendList().contains(targettedPlayer.getUniqueID())&&otherPlayerData.getFriendList().contains(sendingPlayer.getUniqueID()))){
            return true;
        }
        return false;
    }

    public static void refreshNPCVisibility(PlayerWrapper playerWrapper){
        EntityPlayerMP player=(EntityPlayerMP) playerWrapper.getMCEntity();
        IPlayerData playerData=player.getCapability(OOPlayerProvider.OO_DATA, null);
        IEntity[] npcs=playerWrapper.getWorld().getNearbyEntities(playerWrapper.getPos(), 512, 2);
        for(IEntity npc:npcs){
            NPCWrapper customNpc=(NPCWrapper)npc;
            boolean visible=isNpcVisible(playerWrapper, customNpc);
            if(visible){
                player.removeEntity(customNpc.getMCEntity());
                player.connection.sendPacket(new SPacketSpawnMob((EntityLivingBase) customNpc.getMCEntity()));
            }
            else{
                player.removeEntity(customNpc.getMCEntity());
            }
        }
    }

    public static boolean isNpcVisible(PlayerWrapper player, NPCWrapper npc){
        Field f = null;
        try {
            f = ScriptContainer.class.getDeclaredField("engine");
            f.setAccessible(true);
            try {
                ScriptEngine engine = (ScriptEngine)f.get(((EntityCustomNpc)npc.getMCEntity()).script.getScripts().get(0));
                ScriptObjectMirror visibilityRequirements=(ScriptObjectMirror)engine.getContext().getAttribute("visibilityRequirements");
                ScriptObjectMirror invisibilityRequirements=(ScriptObjectMirror)engine.getContext().getAttribute("invisibilityRequirements");
                if(invisibilityRequirements!=null){
                    ArrayList<RequirementData> requirements=new ArrayList<RequirementData>();
                    for(int i=0;i<invisibilityRequirements.size();i++){
                        ScriptObjectMirror requirementObject=(ScriptObjectMirror) invisibilityRequirements.getSlot(i);
                        RequirementData requirement=new RequirementData();
                        requirement.name= ((String)requirementObject.getMember("type")).toUpperCase();
                        Object value=requirementObject.getMember("value");
                        requirement.value=(String) value;
                        requirements.add(requirement);
                    }
                    if(PixelmonEssentials.requirementHandler.checkRequirements(requirements, (EntityPlayerMP)player.getMCEntity())){
                        return false;
                    }
                }
                if(visibilityRequirements!=null){
                    ArrayList<RequirementData> requirements=new ArrayList<RequirementData>();
                    for(int i=0;i<visibilityRequirements.size();i++){
                        ScriptObjectMirror requirementObject=(ScriptObjectMirror) visibilityRequirements.getSlot(i);
                        RequirementData requirement=new RequirementData();
                        requirement.name= ((String)requirementObject.getMember("type")).toUpperCase();
                        Object value=requirementObject.getMember("value");
                        requirement.value=(String) value;
                        requirements.add(requirement);
                    }
                    if(!PixelmonEssentials.requirementHandler.checkRequirements(requirements, (EntityPlayerMP)player.getMCEntity())){
                        return false;
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return true;
    }
}
