package com.goldenglow.common.util;

import net.minecraft.entity.player.EntityPlayerMP;
import noppes.npcs.api.entity.IPlayer;

import java.util.ArrayList;

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
    public String type;
    //id of the dialog/quest if that's the requirement type, unused otherwise
    public int id;
    //night/day if time is the requirement, permission node if the type is permission, unused otherwise
    public String value;
    //permission to override the requirement. Defaults to *
    public String override="*";

    public Requirement(){
    }

    public Requirement(String type, int id){
        this.type=type;
        this.id=id;
    }

    public Requirement(String type, String value){
        this.type=type;
        this.value=value;
    }

    public static boolean checkRequirement(Requirement requirement, EntityPlayerMP player){
        if(requirement.type.equalsIgnoreCase("quest-started")){
            return ((IPlayer)player).hasActiveQuest(requirement.id);
        }
        else if(requirement.type.equalsIgnoreCase("quest-finished")){
            return ((IPlayer)player).hasFinishedQuest(requirement.id);
        }
        else if(requirement.type.equalsIgnoreCase("dialog")){
            return ((IPlayer)player).hasReadDialog(requirement.id);
        }
        else if(requirement.type.equalsIgnoreCase("permission")){
            return ((IPlayer)player).hasPermission(requirement.value);
        }
        else if(requirement.type.equalsIgnoreCase("time")){
            if(requirement.value.equals("day")) {
                return player.getEntityWorld().isDaytime();
            }
            else if(requirement.value.equals("night")) {
                return !player.getEntityWorld().isDaytime();
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

    public static boolean checkRequirements(ArrayList<Requirement> requirements, EntityPlayerMP player){
        for(Requirement requirement:requirements){
            if(!checkRequirement(requirement, player)){
                return false;
            }
        }
        return true;
    }
}
