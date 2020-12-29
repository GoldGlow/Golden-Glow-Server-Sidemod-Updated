package com.goldenglow.common.keyItems;

import com.pixelmonmod.pixelmon.items.ItemTM;
import net.minecraft.nbt.NBTTagCompound;

public class OOTMDummy {
    int tmIndex;
    boolean isHm;
    String attackName;

    public OOTMDummy(boolean isHm, int tmIndex, String attackName){
        this.isHm=isHm;
        this.tmIndex=tmIndex;
        this.attackName=attackName;
    }

    public OOTMDummy(ItemTM tm){
        this.isHm=tm.isHM;
        this.tmIndex=tm.index;
        this.attackName=tm.attackName;
    }

    public String getFullTMName(){
        String name;
        if(isHm){
            name="HM";
        }
        else{
            name="TM";
        }
        return name+tmIndex+": "+attackName;
    }

    public int getTmIndex(){
        return this.tmIndex;
    }

    public boolean isHm(){
        return this.isHm;
    }

    public String getAttackName(){
        return this.attackName;
    }

    public NBTTagCompound writeNBT(){
        NBTTagCompound nbt=new NBTTagCompound();
        nbt.setBoolean("isHm", isHm);
        nbt.setInteger("tmIndex", tmIndex);
        nbt.setString("attackName", attackName);
        return nbt;
    }

    public static OOTMDummy fromNBT(NBTTagCompound nbt){
        return new OOTMDummy(nbt.getBoolean("isHm"), nbt.getInteger("tmIndex"), nbt.getString("attackName"));
    }
}
