package com.goldenglow.common.util;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import noppes.npcs.api.entity.IPlayer;
import noppes.npcs.api.wrapper.PlayerWrapper;

import java.util.List;

/**
 * Created by JeanMarc on 6/17/2019.
 */
public class Requirement {
    //Different types:
    //quest-started
    //quest-finished
    //dialog
    //permission
    //time (day/night values only)
    public RequirementType type;
    //id of the dialog/quest if that's the requirement type, unused otherwise
    public int id;
    //night/day if time is the requirement, permission node if the type is permission, unused otherwise
    public String value;
    //permission to override the requirement. Defaults to *
    public String override="*";

    public Requirement(){
    }

    public Requirement(RequirementType type, int id){
        this.type=type;
        this.id=id;
    }

    public Requirement(RequirementType type, String value){
        this.type=type;
        this.value=value;
    }

    public static boolean checkRequirement(Requirement requirement, EntityPlayerMP playerEntity){
        PlayerWrapper player = new PlayerWrapper(playerEntity);
        if(((IPlayer)player).hasPermission(requirement.override)){
            return true;
        }
        if(requirement.type == RequirementType.QUEST_STARTED) {
            return ((IPlayer)player).hasActiveQuest(requirement.id);
        }
        else if(requirement.type == RequirementType.QUEST_FINISHED) {
            return ((IPlayer)player).hasFinishedQuest(requirement.id);
        }
        else if(requirement.type == RequirementType.DIALOG) {
            return ((IPlayer)player).hasReadDialog(requirement.id);
        }
        else if(requirement.type == RequirementType.PERMISSION) {
            return ((IPlayer)player).hasPermission(requirement.value);
        }
        else if(requirement.type == RequirementType.TIME) {
            if(requirement.value.equals("day")) {
                return playerEntity.getEntityWorld().isDaytime();
            }
            else if(requirement.value.equals("night")) {
                return !playerEntity.getEntityWorld().isDaytime();
            }
        }
        return false;
    }

    public static boolean checkRequirements(Requirement[] requirements, EntityPlayerMP player){
        for(Requirement requirement:requirements){
            if(!checkRequirement(requirement, player)){
                return false;
            }
        }
        return true;
    }

    public static boolean checkRequirements(List<Requirement> requirements, EntityPlayerMP player){
        for(Requirement requirement:requirements) {
            if(!checkRequirement(requirement, player)){
                return false;
            }
        }
        return true;
    }

    public enum RequirementType {
        QUEST_STARTED,
        QUEST_FINISHED,
        DIALOG,
        PERMISSION,
        TIME
    }
}
