package com.goldenglow.common.util.requirements;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.util.requirements.types.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.FMLCommonHandler;
import java.util.List;

import java.util.ArrayList;

public class RequirementHandler {
    ArrayList<Requirement> requirementTypes=new ArrayList<Requirement>();

    public void init(){
        this.requirementTypes.add(new DialogRequirement());
        this.requirementTypes.add(new FriendOnlyRequirement());
        this.requirementTypes.add(new ItemRequirement());
        this.requirementTypes.add(new MoneyRequirement());
        this.requirementTypes.add(new QuestFinishedRequirement());
        this.requirementTypes.add(new QuestStartedRequirement());
        this.requirementTypes.add(new TimeRequirement());
    }

    public Requirement getRequirementType(String type){
        for(Requirement requirementType: this.requirementTypes){
            if(requirementType.getName().equals(type)){
                return requirementType;
            }
        }
        return null;
    }

    public Requirement getRequirementType(RequirementData data){
        for(Requirement requirementType: this.requirementTypes){
            if(requirementType.getName().equals(data.name)){
                return requirementType;
            }
        }
        return null;
    }

    public boolean checkRequirement(RequirementData requirementData, EntityPlayerMP playerMP){
        if(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(playerMP.getGameProfile())!=null){
            return true;
        }
        else{
            Requirement requirement= GoldenGlow.requirementHandler.getRequirementType(requirementData);
            if(requirement!=null) {
                return requirement.hasRequirement(requirementData.value, playerMP);
            }
            return true;
        }
    }

    public boolean checkRequirements(RequirementData[] requirements, EntityPlayerMP player){
        if(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile())!=null){
            return true;
        }
        if(requirements!=null) {
            for (RequirementData requirement : requirements) {
                if (!checkRequirement(requirement, player)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkRequirements(List<RequirementData> requirements, EntityPlayerMP player){
        if(FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile())!=null){
            return true;
        }
        if(requirements!=null) {
            for (RequirementData requirement : requirements) {
                if (!checkRequirement(requirement, player)) {
                    return false;
                }
            }
        }
        return true;
    }

    public void addRequirement(Requirement requirement){
        this.requirementTypes.add(requirement);
    }
}
