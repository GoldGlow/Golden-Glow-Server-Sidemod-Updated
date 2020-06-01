package com.goldenglow.common.guis.pokehelper.config.optionsTypes.music;

import com.goldenglow.GoldenGlow;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.SingleOptionData;
import com.goldenglow.common.guis.pokehelper.config.optionsTypes.UnlockableOptionData;
import net.minecraft.entity.player.EntityPlayerMP;

public class ThemeType implements UnlockableOptionData {
    private String name;
    private String unlockedDescription;
    private String lockedDescription;
    private String value;
    private String permissionNode;

    public ThemeType(String name, String unlockedDescription, String value){
        this.name=name;
        this.unlockedDescription=unlockedDescription;
        this.value=value;
        this.lockedDescription="";
        this.permissionNode="";
    }

    public ThemeType(String name, String unlockedDescription, String value, String lockedDescription, String permissionNode){
        this.name=name;
        this.unlockedDescription=unlockedDescription;
        this.value=value;
        this.lockedDescription=lockedDescription;
        this.permissionNode=permissionNode;
    }

    public String getName(){
        return this.name;
    }

    public String getDescription(){
        return this.unlockedDescription;
    }

    public boolean isUnlocked(EntityPlayerMP playerMP){
        if(this.permissionNode.equals("")){
            return true;
        }
        return GoldenGlow.permissionUtils.checkPermission(playerMP, this.permissionNode);
    }

    public String getProperName(EntityPlayerMP playerMP){
        return this.name;
    }

    public String getProperDescription(EntityPlayerMP playerMP){
        if(this.isUnlocked(playerMP)){
            return this.unlockedDescription;
        }
        return this.lockedDescription;
    }

    public String getValue(){
        return this.value;
    }
}
