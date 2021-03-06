package com.goldenglow.common.guis.pokehelper.config.optionsTypes.social;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.UnlockableOptionData;
import net.minecraft.entity.player.EntityPlayerMP;

public class TitleType implements UnlockableOptionData {
    private String unlockedName;
    private String unlockedDescription;
    private String lockedDescription;
    private String permissionNode;

    public TitleType(String unlockedName, String unlockedDescription, String lockedDescription, String permissionNode){
        this.unlockedName=unlockedName;
        this.unlockedDescription=unlockedDescription;
        this.lockedDescription=lockedDescription;
        this.permissionNode=permissionNode;
    }

    public String getName(){
        return "???";
    }

    public String getUnlockedName(){
        return this.unlockedName;
    }

    public String getDescription(){
        return this.unlockedDescription;
    }

    public boolean isUnlocked(EntityPlayerMP playerMP){
        return GoldenGlow.permissionUtils.checkPermission(playerMP, this.permissionNode);
    }

    public String getProperName(EntityPlayerMP playerMP){
        if(this.isUnlocked(playerMP)){
            return this.unlockedName;
        }
        return this.getName();
    }

    public String getProperDescription(EntityPlayerMP playerMP){
        if(this.isUnlocked(playerMP)){
            return this.unlockedDescription;
        }
        return this.lockedDescription;
    }
}
